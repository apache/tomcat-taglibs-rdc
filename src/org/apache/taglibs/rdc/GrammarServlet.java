/*
 *    
 *   Copyright 2004 The Apache Software Foundation.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */
//$Id$
package org.apache.taglibs.rdc;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>GrammarServlet serves out grammars from the RDC jar.</p>
 * @version 1.0
 */

public class GrammarServlet
    extends HttpServlet {
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
        grammarDirectory = config.getInitParameter("grammarDirectory");
        if (grammarDirectory == null) {
            grammarDirectory = ".grammar";
        } // end of if (grammarDirectory == null)
        ServletContext application = getServletContext();
        jar = application.getRealPath(config.getInitParameter("jar"));
        if (jar == null) {
            jar = application.getRealPath("/WEB-INF/lib/taglibs-rdc.jar");
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
        try {
            JarFile j = new JarFile (jar);
            //locate desired entry name from PATHINFO
            // Notice that are servlet mapping rule consumes
            // component "grammar" from the URL
            String p = grammarDirectory + request.getPathInfo();
            JarEntry e = j.getJarEntry(p);
            if (e == null) {
                PrintWriter out = response.getWriter();
                out.println("Could not locate jar entry: " +p);
                return;
            } // end of if (e != null)
            InputStream i = j.getInputStream(e);
            if (i == null) {
                PrintWriter out = response.getWriter();
                out.println("Could not get input stream from located Jar entry.");
                return;
            } // end of if (i == nul)
            //copy entry to output
            response.setContentLength((int)e.getSize());
            response.setContentType( "application/xml+srgs" );
            copy (i,  response);
        } catch (SecurityException e) {
            PrintWriter out = response.getWriter();
            out.println("Do not have permission to access " +jar);
            e.printStackTrace(out);
        } // end of try-catch
    }

	/**
	 * This method is called once by the container just before the servlet
	 * is taken out of service.
	 *
	 * 
	 */
	public void destroy() {
		jar = null;
		grammarDirectory = null;
	}

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
