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
     * Set the GroupTag instance whose dialog is to be managed
     * 
     * @param groupTag the group tag whose dialog is to be managed
     */
    public void setGroupTag(GroupTag groupTag);
    
    /**
     * Initialization for this invocation of the doTag()
     * 
     * @param ctx the JspContext
     * @param bodyFragment The JspFragment indicating the body of this group
     */
    public boolean initialize(JspContext ctx, JspFragment bodyFragment) 
    throws JspException, IOException;
    
    /**
     * Collect input from the user 
     * 
     * @param ctx the JspContext
     * @param bodyFragment The JspFragment indicating the body of this group
     */
    public void collect(JspContext ctx, JspFragment bodyFragment) 
    throws JspException, IOException;

    /**
     * Get user to confirm the input
     * 
     * @deprecated This behavior will be deprecated from the next minor
     *             version (RDC 1.1), and will be removed in the next
     *             major version (2.0) of the RDC tag library. A container
     *             should not take on the responsibility to confirm
     *             for its children, since it does not attempt to decipher
     *             the meaning of their public data models, and hence,
     *             cannot produce meaningful prompts without significant
     *             configuration. Confirmation is best handled by components.
     */    
    public void confirm() throws JspException, IOException;
    
    /**
     * Cleanup for this invocation of the doTag()
     * 
     * @param ctx the JspContext
     */
    public void finish(JspContext ctx) throws JspException, IOException;
        
} // DialogManager
