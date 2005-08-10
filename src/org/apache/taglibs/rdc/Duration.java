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

import java.util.regex.Pattern;

import org.apache.taglibs.rdc.core.BaseModel;

/**
 * Datamodel for the duration RDC. The duration RDC will be associated
 * with the duration input, the maximum and minimum
 * duration within which the input's duration must lie, and a pattern to
 * which the input must conform.
 *
 * @author Sindhu Unnikrishnan
 * @author Rahul Akolkar
 */

public class Duration extends BaseModel {
    //The duration RDC will be associated
    //with the duration input, the maximum and minimum
    //duration within which the input's duration must lie, and a pattern to
    //which the input must conform.

    //    value returned cannot be more than this value
    private String maxDuration;
    // value returned cannot be less than this value
    private String minDuration;
    // The duration input must conform to this pattern
    private String pattern;

    //Error codes defined in the configuration file
    /**A constant for Error Code stating Invalid duration */
    public static final int ERR_INVALID_DURATION = 1;

    /**A constant for Error Code stating the duration entered is
     * larger than allowed */
    public static final int ERR_NEED_LOWER_VALUE = 2;

    /**A constant for Error Code stating the duration entered is
     * smaller than allowed */
    public static final int ERR_NEED_HIGHER_VALUE = 3;

    //this constant that checks whether one value is less than the other
    private static final int LESS = -1;
    //this constant constant checks whether two values are equal
    private static final int EQUAL = 0;
    //this constant checks whether one value is more than the other
    private static final int MORE = 1;

    /**
     * Sets default values for all data members
     */
    public Duration() {
        super();
        maxDuration = null;
        minDuration = null;
        this.pattern = "P([0-9]{2}Y)?([0-9]{2}M)?([0-9]{2}D)?";
    }

    /**
     * Gets the maximum Duration that a user has specified in the attirbute
     * @return the user specified maximum duration
     */
    public String getMaxDuration() {
        return maxDuration;
    }

    /**
     * Sets the maximum Duration value.
     * @param maxDuration The maximum duration value.
     */
    public void setMaxDuration(String maxDuration) {
        if (maxDuration != null) {
            this.maxDuration = (String)canonicalize(maxDuration, true);
        }
    }

    /**
     * Gets what minimum duration that a user has specified in the attirbute
     * @return the user specified minimum duration
     */
    public String getMinDuration() {
        return minDuration;
    }

    /**
     * Sets the minimum duration value.
     * @param minDuration The minimum duration value.
     */
    public void setMinDuration(String minDuration) {
        if (minDuration != null) {
            this.minDuration = (String)canonicalize(minDuration, true);
        }
    }
    
    /**
     * Validates the received input against the validation constraints
     * @return true if valid, false if invalid
     */
    protected Boolean validate(Object newValue, boolean setErrorCode) {

        if (pattern != null && !(Pattern.matches(pattern, (String)newValue))) {
            if (setErrorCode) setErrorCode(ERR_INVALID_DURATION);
            return Boolean.FALSE;
        }
        if (cmpDuration((String) newValue, minDuration) == LESS) {
            if (setErrorCode) setErrorCode(ERR_NEED_HIGHER_VALUE);
            return Boolean.FALSE;
        } else if (cmpDuration((String) newValue, maxDuration) == MORE) {
            if (setErrorCode) setErrorCode(ERR_NEED_LOWER_VALUE);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * This method returns the full duration format if either partial
     * duration is spoken or retuns full duration if input is
     * full duration
     *
     * @param input The duration value(either partial or full)
     * @return The full duration format
     */
    protected Object canonicalize(Object input, boolean isAttribute) {
        if (input == null) {
            return null;
        }
        String inputStr = (String) input;
        int INDEX_YEAR_END = inputStr.indexOf('Y');
        int INDEX_MONTH_END = inputStr.indexOf('M');
        int INDEX_DAY_END = inputStr.indexOf('D');

        if (!(Pattern.matches(pattern, inputStr))) {
            if (isAttribute) {
                throw new IllegalArgumentException("The required value \"" +
                inputStr + "\" does not match the expected pattern for " +
                "duration RDC with ID " + getId());
            } else {
                setErrorCode(ERR_INVALID_DURATION);
                return null;
            } 
        } else {
            if (INDEX_YEAR_END == -1) {
                inputStr = "P00Y" + (inputStr.length() > 1 ?
                    inputStr.substring(1) : "");
            }
            if (INDEX_DAY_END == -1) {
                inputStr += "00D";
            }
            if (INDEX_MONTH_END == -1) {
                inputStr = inputStr.substring(0,inputStr.indexOf('Y')) + "Y00M"
                        + inputStr.substring(inputStr.indexOf('Y')+1);
            }
        }
        return inputStr;
    }

    /**
     * This method compares the minimum and maximu durations
     * against the input and returns LESS,MORE or EQUAL accordingly
     *
     * @param s1 the duration value
     * @param s2 could be minimum and maximum durations
     *
     * @return could be LESS,MORE or EQUAL
     */
    private int cmpDuration(String s1, String s2) {
        int INDEX_YEAR_END = s1.indexOf('Y');
        int INDEX_MONTH_END = s1.indexOf('M');
        int INDEX_DAY_END = s1.indexOf('D');
        int INDEX_YEAR_BEGIN = s1.indexOf('P') + 1;
        int INDEX_MONTH_BEGIN = INDEX_MONTH_END - 2;
        int INDEX_DAY_BEGIN = INDEX_DAY_END - 2;

        switch (cmp(Integer.parseInt(s1.substring(INDEX_YEAR_BEGIN,
        INDEX_YEAR_END)), Integer.parseInt(s2.substring(INDEX_YEAR_BEGIN,
        INDEX_YEAR_END)))) {
            case LESS :
                return LESS;
            case MORE :
                return MORE;
            case EQUAL :
                switch (cmp(Integer.parseInt(s1.substring(INDEX_MONTH_BEGIN,
                INDEX_MONTH_END)), Integer.parseInt(s2.
                substring(INDEX_MONTH_BEGIN, INDEX_MONTH_END)))) {
                    case LESS :
                        return LESS;
                    case MORE :
                        return MORE;
                    case EQUAL :
                        switch (cmp(Integer.parseInt(s1.
                        substring(INDEX_DAY_BEGIN, INDEX_DAY_END)),
                        Integer.parseInt(s2.substring(INDEX_DAY_BEGIN,
                        INDEX_DAY_END)))) {
                            case LESS :
                                return LESS;
                            case MORE :
                                return MORE;
                        }
                }
        }
        return EQUAL;
    }
    
    /**
     * Compares the minimum and maximum durations
     * against the duration input and returns LESS,MORE or
     *  EQUAL accordingly
     *
     * @param a the duration value
     * @param b could be minimum and maximum durations
     * @return could be LESS,MORE or EQUAL
     */
    private int cmp(int a, int b) {
        return a < b ? LESS : a > b ? MORE : EQUAL;
    }

}