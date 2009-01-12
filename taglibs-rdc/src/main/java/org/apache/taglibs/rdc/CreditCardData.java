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

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The public data model of the credit card info composite.
 * 
 * @author Rahul Akolkar
 */
public class CreditCardData implements Serializable {

    // Serial Version UID
    private static final long serialVersionUID = 1L;
    // Properties, one for each child component
    private String type;
    private String number;
    private Date expiry;
    private String securityCode;
    // Formatter for date
    private SimpleDateFormat formatter;

    public CreditCardData() {
        this.type = null;
        this.number = null;
        this.expiry = null;
        this.securityCode = null;
        this.formatter = new SimpleDateFormat("MMyyyy");
    }
    
    /**
     * Get the type of credit card
     *
     * @return the card type
     */
    public String getType() {
        return type;
    }

    /**
     * Set the type of credit card
     *
     * @param type The card type.
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * Get the card number as a String
     *
     * @return the card number
     */
    public String getNumber() {
        return number;
    }
    
    /**
     * Set the card number as a String 
     *
     * @param number The card number.
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Gets the expiry date
     * 
     * @return Returns the expiry date.
     */
    public Date getExpiry() {
        return expiry;
    }
    
    /**
     * Sets the expiry date
     * 
     * @param expiry The expiry to set.
     */
    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }
    
    /**
     * Gets the security code
     * 
     * @return Returns the securityCode.
     */
    public String getSecurityCode() {
        return securityCode;
    }
    
    /**
     * Sets the security code
     * 
     * @param securityCode The securityCode to set.
     */
    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }
    
    /**
     * A serialized version of CreditCardData object
     */
    public String toString() {
        String expStr = formatter.format(expiry);
        StringBuffer buf = new StringBuffer();
        buf.append("type=").append(this.type).append(";number=").
            append(this.number).append(";expiry=").append(expStr).
            append(";securityCode=").append(this.securityCode);
        return buf.toString();
    }
    
}
