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

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.taglibs.rdc.core.BaseModel;

/**
 * Datamodel for the zipCode RDC. The zipCode RDC is associated with 
 * the zip code input, the length which the inputs should have, and 
 * a pattern to which the input must conform.  
 *
 * @author Raghavendra Udupa
 * @author Rahul Akolkar
 */

public class ZipCode extends BaseModel {
    // The zipCode RDC is associated with the zip code
    // input, the length  which the 
    // inputs should have, and a pattern to which the input
    // must conform.

    // Serial Version UID
    private static final long serialVersionUID = 1L;
    //  Length of the input; -1 indicates no constraint on length.
    private int length;
    // The zip code input must conform to this pattern
    private String pattern;

    // Error codes, defined in configuration file
    /**A constant for Error Code stating Invalid Zip Code */
    public static final int ERR_INVALID_ZIP_CODE = 1;

    /**A constant for Error Code stating Invalid length of Zip Code */
    public static final int ERR_INCORRECT_LENGTH_ZIP_CODE = 2;

    /**
      * Sets default values for all data members
      */
    public ZipCode() {
        super();
        this.length = -1;
        // Default pattern allows any combination of digits
        this.pattern = "[0-9]+";
    }

    /**
     * Sets the allowed length of input
     * 
     * @param length the allowed length of input
     */
    public void setLength(String length) {
        if (length != null) {
            try {
                this.length = Integer.parseInt(length);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("length attribute of \"" +
                        getId() + "\" zipCode tag is not an integer.");
            }
        }
    }
    
    /**
     * Gets the length as a string
     * 
     * @return the length string
     */
    public String getLength() {
        return String.valueOf(this.length);
    }

    /**
     * Sets the pattern string to which the input must conform
     * 
     * @param pattern the pattern string to which the input must conform
     */
    public void setPattern(String pattern) {
        if (pattern != null) {
            try {
                Pattern.compile(pattern);
                this.pattern = pattern;
            } catch (PatternSyntaxException e) {
                throw new IllegalArgumentException("pattern attribute " +
                    "of \"" + getId() + "\" zipCode tag has invalid " +
                    "pattern syntax.");
            }
        }
    }

    /**
     * Gets the pattern string
     * 
     * @return the pattern string
     */
    public String getPattern() {
        return this.pattern;
    }

    /**
     * Validates the input against the given constraints
     * 
     * @return TRUE if valid, FALSE otherwise
     */
    protected Boolean validate(Object newValue, boolean setErrorCode) {

        if (pattern != null && !(Pattern.matches(pattern, (String)newValue))) {
            if (setErrorCode) setErrorCode(ERR_INVALID_ZIP_CODE);
            return Boolean.FALSE;
        }
        if (length > 0 && (((String) newValue).length() != length)) {
            if (setErrorCode) setErrorCode(ERR_INCORRECT_LENGTH_ZIP_CODE);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
