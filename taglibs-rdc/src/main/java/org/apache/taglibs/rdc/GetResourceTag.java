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
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * <p>This tag is used to retrieve a resource from a (property)
 * resource bundle. Avoids <code>LocalizationContext</code> / JSTL
 * dependency for RDC core.</p>
 * 
 * @author Rahul Akolkar
 */
public class GetResourceTag extends SimpleTagSupport {
    
    /**
     * The ResourceBundle
     */
    private ResourceBundle bundle;
    
    /**
     * The key in which the String resource is stored
     */
    private String key;
    
    /**
     * The variable which will hold the value of the resource
     */
    private String var;
    
    /**
     * Whether the resource is a grammar URI
     */
    private Boolean isGrammarURI;
    
    /**
     * @param bundle The bundle to set.
     */
    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }
    
    /**
     * @param key The key to set.
     */
    public void setKey(String key) {
        this.key = key;
    }
    
    /**
     * @param var The var to set.
     */
    public void setVar(String var) {
        this.var = var;
    }
    
    /**
     * @param isGrammarURI The isGrammarURI to set.
     */
    public void setIsGrammarURI(Boolean isGrammarURI) {
        this.isGrammarURI = isGrammarURI;
    }
    
    /**
     * Get the resource and assign it to the variable in the JSP context
     */
    public void doTag()
        throws IOException, JspException {

        Object retVal = bundle.getObject(key);
        
        if (isGrammarURI != null && isGrammarURI.booleanValue() &&
                retVal != null && ((String)retVal).startsWith(".grammar/")) {
            // .grammar is a reserved directory for grammar URIs
            // @see GrammarServlet
               StringBuffer buf = new StringBuffer(((HttpServletRequest)
                   ((PageContext)getJspContext()).getRequest()).getContextPath());
            buf.append('/').append(retVal);
            retVal = buf.toString();
        }
        
        getJspContext().setAttribute(var, retVal);
    }
    
}

