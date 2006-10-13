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

import java.util.Stack;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * <p>
 * Peek into the top of a stack and make it available as a page scoped object.
 * </p>
 * 
 * @author Abhishek Verma
 * @author Rahul Akolkar
 */

public class StackPeek extends SimpleTagSupport {
    // Name of the exported page scoped variable for the top of stack.
    private String var;

    // A stack of rdc datamodels
    private Stack stack;

    /**
     * Set the name of exported page scoped variable
     * 
     * @param var -
     *            name of exported page scoped variable
     */
    public void setVar(String var) {
        this.var = var;
    }

    /**
     * Set the stack of rdc datamodels
     * 
     * @param stack -
     *            the stack of rdc datamodels
     */
    public void setStack(Stack stack) {
        this.stack = stack;
    }

    public StackPeek() {
        super();
    } // StackPeek constructor

    public void doTag() throws JspException, java.io.IOException {
        if (stack == null) {
            getJspContext().setAttribute(var, null);
        } else {
            getJspContext().setAttribute(var, stack.peek());
        } // end of else
    }
}
