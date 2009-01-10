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

import javax.servlet.jsp.PageContext;
import org.apache.taglibs.rdc.core.ComponentModel;

/**
 * DataModel for CreditCardInfo Composite RDC
 *
 * @author Rahul Akolkar
 */
public class CreditCardInfo extends ComponentModel {

    // The mode this instance will work in
    private String mode;
    
    public CreditCardInfo(){
        super();
        mode = MODE_LONG;
    }

    /** 
      * Stores the id and file attributes from the config xml to the 
      * configMap
      * 
      * @see ComponentModel#configHandler()
      */
    public void configHandler() {
        this.configMap = RDCUtils.configHandler(this.config, 
            (PageContext) this.context);
    }
    
    /**
     * Get the current mode
     * 
     * @return Returns the mode.
     */
    public String getMode() {
        return mode;
    }
    
    /**
     * Set the mode
     * 
     * @param mode The mode to set.
     */
    public void setMode(String mode) {
        if (mode != null) {
            this.mode = mode;
        }
    }

    //// Mode definitions
    // Everything
    private static final String MODE_LONG = "long";
    // number + expiry
    private static final String MODE_SHORT = "short";
    // type only
    private static final String MODE_TYPE = "type";
    // number only
    private static final String MODE_NUMBER = "number";
    // expiry only
    private static final String MODE_EXPIRY = "expiry";
    // security code only
    private static final String MODE_SECURITY = "security";
    
    //// Getters for the modes
    /**
     * @return Returns MODE_EXPIRY
     */
    public String getMODE_EXPIRY() {
        return MODE_EXPIRY;
    }
    /**
     * @return Returns MODE_LONG.
     */
    public String getMODE_LONG() {
        return MODE_LONG;
    }
    /**
     * @return Returns MODE_NUMBER.
     */
    public String getMODE_NUMBER() {
        return MODE_NUMBER;
    }
    /**
     * @return Returns MODE_SECURITY.
     */
    public String getMODE_SECURITY() {
        return MODE_SECURITY;
    }
    /**
     * @return Returns MODE_SHORT.
     */
    public String getMODE_SHORT() {
        return MODE_SHORT;
    }
    /**
     * @return Returns MODE_TYPE.
     */
    public String getMODE_TYPE() {
        return MODE_TYPE;
    }
}
