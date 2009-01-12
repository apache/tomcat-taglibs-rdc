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
 *
 * Datamodel for the percent RDC. The percent RDC will be associated
 * with the percentages (say 10%), the maximum and minimum
 * percentage length within which the input's length must lie, and a 
 * pattern to which the input must conform.
 *
 * @author Sindhu Unnikrishnan
 * @author Rahul Akolkar
 */
public class Percent extends BaseModel {
    // The percent RDC will be associated with the percent input,
    // the maximum and minimum values within which the input must
    // lie .

    // Serial Version UID
    private static final long serialVersionUID = 1L;
    // This is the pattern for percent
    private String pattern;
    // Maximum allowed value for the percentage
    private int maxPercent;
    // Minimum allowed value for the percentage
    private int minPercent;

    // Error codes, defined in configuration file
    /**A constant for Error Code stating Invalid percent */
    public static final int ERR_INVALID_PERCENT = 1;

    /**A constant for Error Code stating the percent entered is
     * larger than allowed */
    public static final int ERR_NEED_LOWER_VALUE = 2;

    /**A constant for Error Code stating the percent entered is
     * smaller than allowed */
    public static final int ERR_NEED_HIGHER_VALUE = 3;

    /**
     *
     * Sets default values for all data model members
     */
    public Percent() {
        super();
        this.minPercent = 0;
        this.maxPercent = 0;
        this.pattern = "[0-9]{1,2}";
    }

    /**
     * Gets the maximum allowed value for the percentage.
     *
     * @return the maximum allowed value for the percentage
     */
    public String getMaxPercent() {
        return String.valueOf(maxPercent);
    } // end getMaxPercent

    /**
     * Sets the maximum allowed value for the percentage
     *
     * @param maxPercent The maximum allowed value for the percentage
     */
    public void setMaxPercent(String maxPercent) {
        if (maxPercent != null) {
            try {
                this.maxPercent = Integer.parseInt(maxPercent);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("maxPercent attribute " +
                    "of \"" + getId() + "\" percent tag is not an integer.");
            }
        }
    } // end setMaxPercent

    /**
     * Gets the minimum allowed value for the percentage
     *
     * @return the minimum allowed value for the percentage
     */
    public String getMinPercent() {
        return String.valueOf(minPercent);
    } // end getMinPercent

    /**
     * Sets the minimum allowed value for the percentage
     *
     * @param minPercent The minimum allowed value.
     */
    public void setMinPercent(String minPercent) {
        if (minPercent != null) {
            try {
                this.minPercent = Integer.parseInt(minPercent);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("minPercent attribute " +
                    "of \"" + getId() + "\" percent tag is not an integer.");
            }
        }
    } // end setMinPercent

    /**
     * Gets the pattern string
     *
     * @return the pattern string
     */
    public String getPattern() {
        return this.pattern;
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
                throw new IllegalArgumentException("pattern attribute of \"" +
                    getId()    + "\" percent tag not in proper format.");
            }
        }
    }
    
    /**
     * Validates the percent value against the given constraints
     *
     * @return TRUE if valid, FALSE otherwise
     */
    protected Boolean validate(Object newValue, boolean setErrorCode) {

        int val = Integer.parseInt((String) newValue);
        if (pattern != null && !(Pattern.matches(pattern, (String)newValue))) {
            if (setErrorCode) setErrorCode(ERR_INVALID_PERCENT);
            return Boolean.FALSE;
        }
        if (maxPercent > 0 && val > maxPercent) {
            if (setErrorCode) setErrorCode(ERR_NEED_LOWER_VALUE);
            return Boolean.FALSE;
        }
        if (minPercent > 0 && val < minPercent) {
            if (setErrorCode) setErrorCode(ERR_NEED_HIGHER_VALUE);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;

    } // end customValidate()
        
    /**
     * Custom canonicalized value calculation
     */
    protected String calculateCanonicalizedValue(Object newValue) {
        return ((String) newValue) + "%";
    }

} // end class Percent{}

// *** End of Percent.java ***
