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
package org.apache.taglibs.rdc.core;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import org.apache.taglibs.rdc.RDCUtils;
/**
 * <p>Tag implementation of the &lt;rdc:struts-submit&gt; tag
 * Collects data from the RDC layer and posts it according to the RDC-struts
 * interface contract.</p>
 *  
 * @author Rahul Akolkar
 */

public class StrutsSubmitTag
    extends SimpleTagSupport {
    
    /**
     * Attribute name of map that will store form data from 
     * multiple submits this session
     */
    public static final String ATTR_VIEWS_MAP = "viewsMap";
	/**
	 * Attribute name of key that will be used to retrieve 
	 * form data for this submission
	 */
	public static final String ATTR_VIEWS_MAP_KEY = "key";
	
    // URI to be submitted to the struts ActionServlet
    private String submit;
    // Namelist to be forwarded to the struts layer
    private String namelist;
	// The list of RDCs to be whose state will be "cleared"
	// from the existing session. Must be a space-separated list
	// of "dialogMap" keys.
	private String clearlist;
    // Page context for the RDC data collection
    private PageContext context;
	// The "dialogMap" object from the host JSP
	private Map dialogMap;

	// Error messages (to be i18n'zed)
	private static final String ERR_NO_DIALOGMAP = "<rdc:struts-submit>: The" +
		"\"dialogMap\" attribute must accompany the \"clearlist\"" +
		" attribute; and refer to the dialogMap of the host JSP.";
	private static final String ERR_CANNOT_CLEAR = "Could not clear " +
		"token \"{0}\" specified in <rdc:struts-submit> clearlist. " +
		"Check the clearlist.\n";
	private static final String ERR_POP_FORM_BEAN = "Struts Submit" +
		" Populating Form Bean";
	private static final String ERR_FORWARD_FAILED = "<!-- Error after " +
		"struts submit forward to: \"{0}\" with namelist \"{1}\" -->\n";
	private static final String ERR_ILLEGAL_ACCESS = "IllegalAccessException" +
		" while populating form bean";
	private static final String ERR_ILLEGAL_INVOC = "InvocationTargetException" +
		" while populating form bean";
	
	// Logging
	private static Log log = LogFactory.getLog(StrutsSubmitTag.class);
			
	/*
	 * Constructor
	 */    
    public StrutsSubmitTag() {
    	super();
    	this.submit = null;
    	this.namelist = null;
    	this.clearlist = null;
    	this.context = null;
    	this.dialogMap = null;
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
	 * Set the clearlist
	 * 
	 * @param String clearlist
	 */
	public void setClearlist(String clearlist) {
		this.clearlist = clearlist;
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
	 * Set the "dialogMap" object [of the host JSP]
	 * 
	 * @param LinkedHashMap the dialogMap object from the host JSP
	 */
	public void setDialogMap(Map dialogMap) {
		this.dialogMap = dialogMap;
	}
	
	/**
 	 * Collect data from the RDC layer and post it into the viewsMap
 	 * according to the RDC-struts interface contract 
 	 * 
	 */
    public void doTag()
        throws IOException, JspException, JspTagException   {

        JspWriter out = context.getOut();

		if (!RDCUtils.isStringEmpty(namelist)) {
			// (1) Access/create the views map 
			Map viewsMap = (Map) context.getSession().
				getAttribute(ATTR_VIEWS_MAP);
			if (viewsMap == null) {
				viewsMap = new HashMap();
				context.getSession().setAttribute(ATTR_VIEWS_MAP, viewsMap);
			}
			
			// (2) Populate form data 
			Map formData = new HashMap();
			StringTokenizer nameToks = new StringTokenizer(namelist, " ");
			while (nameToks.hasMoreTokens()) {
				String name = nameToks.nextToken();
				formData.put(name, context.getAttribute(name));
			}
			
			// (3) Store the form data according to the RDC-struts 
			//     interface contract
			String key = "" + context.hashCode();
			viewsMap.put(key, formData);
			context.getRequest().setAttribute(ATTR_VIEWS_MAP_KEY, key);
		}

		if (!RDCUtils.isStringEmpty(clearlist)) {		
			// (4) Clear session state based on the clearlist
			if (dialogMap == null) {
				throw new IllegalArgumentException(ERR_NO_DIALOGMAP);
			}
			StringTokenizer clearToks = new StringTokenizer(clearlist, " ");
			outer:
			while (clearToks.hasMoreTokens()) {
				String clearMe = clearToks.nextToken();
				String errMe = clearMe;
				boolean cleared = false;
				int dot = clearMe.indexOf('.');
				if (dot == -1) {
					if(dialogMap.containsKey(errMe)) {
						dialogMap.remove(errMe);
						cleared = true;
					}
				} else {
					// TODO - Nested data model re-initialization
					BaseModel target = null;
					String childId = null;
					while (dot != -1) {
						try {
							childId = clearMe.substring(0,dot);
							if (target == null) {
								target = (BaseModel) dialogMap.get(childId);
							} else {
								if ((target = RDCUtils.getChildDataModel(target,
									childId)) == null) {
									break;
								}
							}
							clearMe = clearMe.substring(dot+1);
							dot = clearMe.indexOf('.');
						} catch (Exception e) {
							MessageFormat msgFormat =
								new MessageFormat(ERR_CANNOT_CLEAR);
							log.warn(msgFormat.format(new Object[] {errMe}));
							continue outer;
						}
					}
					if (target != null) {
						cleared = RDCUtils.clearChildDataModel(target,
							clearMe);
					}
				}
				if (!cleared) {
					MessageFormat msgFormat = 
						new MessageFormat(ERR_CANNOT_CLEAR);
					log.warn(msgFormat.format(new Object[] {errMe}));
				}
			}
		}

		// (5) Forward request
		try {
			context.forward(submit);
        } catch (ServletException e) {
        	// Need to investigate whether refactoring this
        	// try to provide blanket coverage makes sense
			MessageFormat msgFormat = new MessageFormat(ERR_FORWARD_FAILED);
			// Log error and send error message to JspWriter 
			out.write(msgFormat.format(new Object[] {submit, namelist}));
			log.error(msgFormat.format(new Object[] {submit, namelist}));
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

		Map viewsMap = (Map) req.getSession().getAttribute(ATTR_VIEWS_MAP);
		Map formData = (Map) viewsMap.get(req.getAttribute(ATTR_VIEWS_MAP_KEY));

		try {
			BeanUtils.populate(formBean, formData);
		} catch (IllegalAccessException iae) {
			log.error(ERR_ILLEGAL_ACCESS);
			errors.add(ERR_POP_FORM_BEAN, new ActionMessage(ERR_ILLEGAL_ACCESS));
		} catch (InvocationTargetException ite) {
			log.error(ERR_ILLEGAL_INVOC);
			errors.add(ERR_POP_FORM_BEAN, new ActionMessage(ERR_ILLEGAL_INVOC));
		}
    }
}
