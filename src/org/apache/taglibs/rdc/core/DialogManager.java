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
/*$Id$*/
package org.apache.taglibs.rdc.core; 

import java.io.IOException;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;

/**
 *
 * <p>The interface for implementing pluggable dialog
 * management strategies for the RDC group helper tag.</p>
 * 
 * @author Rahul Akolkar
 */

public interface DialogManager {
	
	/**
	 * Initialization for this invocation of the doTag()
	 * 
	 * @param ctx the JspContext
	 */
	public boolean initialize(JspContext ctx, JspFragment bodyFrag) 
	throws JspException, IOException;
	
	/**
	 * Collect input from the user 
	 * 
	 * @param ctx the JspContext
	 * @param bodyFragment the JspFragment indicating the body of this group
	 */
	public void collect(JspContext ctx, JspFragment bodyFrag) 
	throws JspException, IOException;

	/**
	 * Get user to confirm the input
	 * 
	 */	
	public void confirm() throws JspException, IOException;
	
	/**
	 * Cleanup for this invocation of the doTag()
	 * 
	 * @param ctx the JspContext
	 */
	public void finish(JspContext ctx) throws JspException, IOException;
		
} // DialogManager
