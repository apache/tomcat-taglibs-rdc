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

import java.util.Stack;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Push an object on to the stack.
 * 
 * @author Abhishek Verma
 * @author Rahul Akolkar
 */

public class StackPush extends SimpleTagSupport {
    // A stack of rdc datamodels
    private Stack stack;

    // The object to be pushed on to the stack. It is an rdc datamodel
    private Object element;

    /**
     * Set the stack of rdc datamodels
     * 
     * @param stack -
     *            the stack of rdc datamodels
     */
    public void setStack(Stack stack) {
        this.stack = stack;
    }

    /**
     * Sets the object to be pushed on to the stack
     * 
     * @param element -
     *            rdc datamodel to be pushed on to the stack
     */
    public void setElement(Object element) {
        this.element = element;
    }

    public StackPush() {
        super();
    } // StackPush constructor

    public void doTag() throws JspException, java.io.IOException {
        if (stack != null) {
            stack.push(element);
        }
    }
}
