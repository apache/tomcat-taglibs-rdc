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
import java.util.HashMap;
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

/**
 * <p>Tag implementation of the &lt;rdc:struts-submit&gt; tag
 * Collects data from the RDC layer and posts it according to the RDC-struts
 * interface contract.</p>
 *  
 * @author Rahul Akolkar
 */

public class StrutsSubmitTag
    extends SimpleTagSupport {
    
    // Attribute name of map that will store form data from 
    // multiple submits this session
    public static final String ATTR_VIEWS_MAP = "viewsMap";
	// Attribute name of key that will be used to retrieve 
	// form data for this submission
	public static final String ATTR_VIEWS_MAP_KEY = "key";
    // URI to be submitted to the struts ActionServlet
    String submit;
    // Namelist to be forwarded to the struts layer
    String namelist;
    // Page context for the RDC data collection
    PageContext context;

	/*
	 * Constructor
	 */    
    public StrutsSubmitTag() {
    	super();
    	submit = null;
    	namelist = null;
    	context = null;
    }
	
	/**
	 * Set the submit URI
	 * 
	 * @param String submit
	 */
	public void setSubmit(String submit) {
		this.submit = submit;
	}
	
	
	/**
	 * Set the namelist
	 * 
	 * @param String namelist
	 */
	public void setNamelist(String namelist) {
		this.namelist = namelist;
	}
	
	/**
	 * Set the page context 
	 * 
	 * @param PageContext context the supplied page context
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

		HashMap viewsMap = (HashMap)context.getSession().getAttribute(ATTR_VIEWS_MAP);
		if (viewsMap == null) {
			viewsMap = new HashMap();
			context.getSession().setAttribute(ATTR_VIEWS_MAP, viewsMap);
		}
		HashMap formData = new HashMap();
		StringTokenizer strTok = new StringTokenizer(namelist, " ");
		while (strTok.hasMoreTokens()) {
			String tok = strTok.nextToken();
			formData.put(tok, context.getAttribute(tok));
		}
		String key = "" + context.hashCode();
		viewsMap.put(key, formData);
		context.getRequest().setAttribute(ATTR_VIEWS_MAP_KEY, key);
		try {
			context.forward(submit);
        } catch (ServletException e) {
        	// Need to investigate whether refactoring this
        	// try to provide blanket coverage makes sense -Rahul
            out.write("<!-- Error after struts submit forward to: " + submit +
                      " with namelist " + namelist + "-->\n");
            e.printStackTrace();
        } // end of try-catch
    }

	/**
	 * Retrieve data posted to viewsMap for this request according to the
	 * RDC-struts interface contract, and populate the ActionForm using
	 * this data.
	 * 
	 */    
    public static void populate(ActionForm formBean, HttpServletRequest req,
    	ActionErrors errors) {

		HashMap viewsMap = (HashMap) req.getSession().getAttribute(ATTR_VIEWS_MAP);
		HashMap formData = (HashMap) viewsMap.get(req.getAttribute(ATTR_VIEWS_MAP_KEY));

		try {
			BeanUtils.populate(formBean, formData);
		} catch (IllegalAccessException iae) {
			iae.printStackTrace();
			errors.add("Struts Submit Populating Form Bean", new ActionMessage(
					"IllegalAccessException while populating form bean"));
		} catch (InvocationTargetException ite) {
			ite.printStackTrace();
			errors.add("Struts Submit Populating Form Bean", new ActionMessage(
					"InvocationTargetException while populating form bean"));
		}
    }
}
