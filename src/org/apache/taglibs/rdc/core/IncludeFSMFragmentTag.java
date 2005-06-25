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
package org.apache.taglibs.rdc.core;

import java.io.IOException;
import java.text.MessageFormat;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.taglibs.rdc.core.RDCTemplate;

/**
 * <p>Tag implementation of the &lt;rdc:include-fsm-fragment&gt; tag.</p>
 *  
 * @author Rahul Akolkar
 */
public class IncludeFSMFragmentTag
    extends SimpleTagSupport {
	
	// Error messages (to be i18n'zed)
	private static final String ERR_INCLUDE = "<!-- Error after " +
		"rdc:include to: \"{0}\" with message: \"{1}\" -->\n";
	
	// Logging
	private static Log log = LogFactory.getLog(IncludeFSMFragmentTag.class);
    
    // The RDC simple template bean
    RDCTemplate template;
    // Page context for the RDC data collection
    PageContext context;

	/*
	 * Constructor
	 */    
    public IncludeFSMFragmentTag() {
    	super();
    	template = null;
    	context = null;
    }
	
	/**
	 * Set the RDCTemplate
	 * 
	 * @param template
	 */
	public void setTemplate(RDCTemplate template) {
		this.template = template;
	}
	
	/**
	 * Set the PageContext
	 * 
	 * @param PageContext context
	 */
	public void setContext(PageContext context) {
		this.context = context;
	}

	/**
 	 * Collect data from the RDC layer and post it into the viewsMap
 	 * according to the RDC-struts interface contract 
 	 * 
	 */
    public void doTag()
        throws IOException, JspException, JspTagException   {

        JspWriter out = context.getOut();
		try {
			context.getRequest().setAttribute("model",template);
			context.getRequest().setAttribute("constants",new Constants());
			context.include(template.getFsmFragment());
        } catch (Exception e) {
        	MessageFormat msgFormat = new MessageFormat(ERR_INCLUDE);
        	String errMsg = msgFormat.format(new Object[] {template.
        		getFsmFragment(), e.getMessage()});
        	log.error(errMsg);
            out.write(errMsg);
        } // end of try-catch
    }

}
