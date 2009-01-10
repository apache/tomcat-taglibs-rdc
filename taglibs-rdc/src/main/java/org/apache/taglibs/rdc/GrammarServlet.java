/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.taglibs.rdc;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>GrammarServlet serves out grammars from the RDC jar.</p>
 * @version 1.0
 */

public class GrammarServlet
    extends HttpServlet {

    //// Constants
    // Init params and default values
    private static final String INIT_PARAM_GRAM_DIR = "grammarDirectory";
    private static final String DEFAULT_GRAM_DIR = ".grammar";
    
    private static final String INIT_PARAM_JAR = "jar";
    private static final String DEFAULT_JAR = "/WEB-INF/lib/taglibs-rdc.jar";
    
    // Mimes and extensions
    private static final String MIME_JAVASCRIPT = "application/x-javascript";
    private static final String EXT_JAVASCRIPT = "js";
    private static final String MIME_SRGS_GRAM = "application/srgs+xml";
    
    // Error messages (to be i18n'zed)
    private static final String ERR_NO_SUCH_ENTRY = "Could not locate jar " +
        "entry: \"{0}\" in jar: \"{1}\"";
    private static final String ERR_NO_INPUT_STREAM = "Could not obtain " +
        "input stream from located Jar entry: \"{0}\" in jar: \"{1}\"";
    private static final String ERR_NO_PERMISSION = "Do not have " +
        "permission to access: \"{0}\"";
    
    // Logging
    private static Log log = LogFactory.getLog(GrammarServlet.class);
    
    /* Records name of the jar from which we extract the grammar */
    private String jar;

    /* Name of directory within the jar that holds grammar files */
    private String grammarDirectory;
    

    /**
     * This method is called once by the container when the servlet is
     * initialized, before any requests are delivered to the servlet.
     *
     *  GrammarServlet uses init params  jar and grammarDirectory
     *  These params are recorded in member variables.
     *  if no init params are specified, we use some reasonable defaults.
     */
    
    public void init() {
        ServletConfig config = getServletConfig();
        grammarDirectory = config.getInitParameter(INIT_PARAM_GRAM_DIR);
        if (grammarDirectory == null) {
            grammarDirectory = DEFAULT_GRAM_DIR;
        }
        ServletContext application = getServletContext();
        jar = application.getRealPath(config.getInitParameter(INIT_PARAM_JAR));
        if (jar == null) {
            jar = application.getRealPath(DEFAULT_JAR);
        }
    }

    /**
     * This method is called by the container for each GET request.
     *
     * Grammar to extract is specified as part of the URL
     *  We extract this from PathInfo, and serve the specified
     *  grammar from the RDC jar  file.
     */
    
    public void doGet(HttpServletRequest request, 
                      HttpServletResponse response) 
            throws ServletException, IOException {
        MessageFormat msgFormat;
        String errMsg;
        try {
            JarFile j = new JarFile (jar);
            // locate desired entry name from PATHINFO
            // Notice that are servlet mapping rule consumes
            // component "grammar" from the URL
            String p = grammarDirectory + request.getPathInfo();
            JarEntry e = j.getJarEntry(p);
            if (e == null) {
                msgFormat = new MessageFormat(ERR_NO_SUCH_ENTRY);
                errMsg = msgFormat.format(new Object[] {p, jar});
                // Log error and send bad response
        log.error(errMsg);
        PrintWriter out = response.getWriter();
                out.println(errMsg);
                return;
            } // end of if (e != null)
            InputStream i = j.getInputStream(e);
            if (i == null) {
                msgFormat = new MessageFormat(ERR_NO_INPUT_STREAM);
                errMsg = msgFormat.format(new Object[] {p, jar});
        log.error(errMsg);
        PrintWriter out = response.getWriter();
                out.println(errMsg);
                return;
            } // end of if (i == nul)
            //copy entry to output
            response.setContentLength((int)e.getSize());
            // We only serve JavaScript files or SRGS grammar files
            // out of the .grammar directory. 
            // Thanks Stu for the mime types!
            if (p.endsWith( EXT_JAVASCRIPT )) {
                response.setContentType( MIME_JAVASCRIPT );
            } else {
                response.setContentType( MIME_SRGS_GRAM );
            }
            copy (i,  response);
        } catch (SecurityException e) {
            msgFormat = new MessageFormat(ERR_NO_PERMISSION);
            errMsg = msgFormat.format(new Object[] {jar});
        log.error(errMsg);
        PrintWriter out = response.getWriter();
        out.println(errMsg);
        } // end of try-catch
    }

    /**
     * This method is called once by the container just before the servlet
     * is taken out of service.
     * 
     */
    public void destroy() {
        jar = null;
        grammarDirectory = null;
    }

    /**
     * This way please
     */
    private void copy (InputStream i,  HttpServletResponse response)
            throws IOException  {
        ServletOutputStream out = response.getOutputStream();
        byte[] b = new byte[ 1024 ];
        while( true ) {
            int bytes = i.read( b);
            if( bytes == -1 ) {
                break;
            }
            out.write( b, 0, bytes ); 
        }
        i.close();
        out.flush();
    }
    
}
