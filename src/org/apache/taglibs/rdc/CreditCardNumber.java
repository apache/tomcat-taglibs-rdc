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
package org.apache.taglibs.rdc;

import org.apache.taglibs.rdc.core.BaseModel;
import org.apache.taglibs.rdc.core.Constants;

/**
 * Datamodel for the credit card number RDC.
 *
 * @author Rahul Akolkar
 */
public class CreditCardNumber extends BaseModel {
    
    // Maximum number of denials before a graceful exit
    private int maxDenials;
    
    // Number of denials
    private int denialCount;

    /**
     * Constructor
     */
    public CreditCardNumber() {
        super();
        this.maxDenials = 2;
        this.denialCount = 0;
    }
    
    /**
     * Record user response to confirmation
     *
     * @param confirmed The user confirmation
     */
    public void setConfirmed(Boolean confirmed) {
        super.setConfirmed(confirmed);
        if (!confirmed.booleanValue()) {
            denialCount++;
            if (maxDenials > 0 && denialCount == maxDenials) {
                setState(Constants.FSM_DONE);
            }
        }
    }

    /**
     * Get the maximum denials allowed before graceful exit
     * 
     * @return Returns the maxDenials.
     */
    public int getMaxDenials() {
        return maxDenials;
    }
    /**
     * Set the maximum denials allowed before graceful exit
     * 
     * @param maxDenials The maxDenials to set.
     */
    public void setMaxDenials(int maxDenials) {
        this.maxDenials = maxDenials;
    }
}
