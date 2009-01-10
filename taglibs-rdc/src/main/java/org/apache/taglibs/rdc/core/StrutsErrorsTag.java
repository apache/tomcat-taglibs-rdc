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
import java.util.Iterator;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * <p>Tag implementation of the &lt;rdc:struts-errors&gt; tag.
 * Play out any errors messages from the preceding struts action.
 * This basic implementation reports all errors at once and disregards
 * any values specified via the message.</p>
 *  
 * @author Rahul Akolkar
 */

public class StrutsErrorsTag
    extends SimpleTagSupport {

    /*
     * Constructor
     */    
    public StrutsErrorsTag() {
        super();
    }

    /**
      * Play out any errors messages from the preceding struts action.
      * This basic implementation reports all errors at once and disregards
      * any values specified via the message.
      * 
     */
    public void doTag()
        throws IOException, JspException, JspTagException   {

        JspWriter out = getJspContext().getOut();
        Object errors = getJspContext().findAttribute(Globals.ERROR_KEY);
        if (errors == null) {
            return;
        } else if (errors instanceof ActionErrors) {
            // deprecated in Struts 1.2
            out.print("<block>");
            ActionErrors actionErrors = (ActionErrors) errors;
            if (!actionErrors.isEmpty()) {
                Iterator iter = actionErrors.get();
                while (iter.hasNext()) {
                    ActionMessage msg = (ActionMessage)iter.next();
                    if (msg != null) {
                        out.print("<prompt>" + msg.getKey() + "</prompt>");
                    }
                }
            }
            out.print("</block>");            
        } else if (errors instanceof ActionMessages) {
            out.print("<block>");
            ActionMessages actionMsgs = (ActionMessages) errors;
            if (!actionMsgs.isEmpty()) {
                Iterator iter = actionMsgs.get();
                while (iter.hasNext()) {
                    ActionMessage msg = (ActionMessage)iter.next();
                    if (msg != null) {
                        out.print("<prompt>" + msg.getKey() + "</prompt>");
                    }
                }
            }
            out.print("</block>");            
        } else if (errors instanceof String[]) {
            out.print("<block>");
            String[] errorsArr = (String []) errors;
            for (int i = 0; i < errorsArr.length; i++) {
                if (errorsArr[i] != null) {
                    out.print("<prompt>" + errorsArr[i] + "</prompt>");
                }
            }
            out.print("</block>");
        } else if (errors instanceof String && errors != null) {
            out.print("<block><prompt>" + errors + "</prompt></block>");
        }

    }
    
}
