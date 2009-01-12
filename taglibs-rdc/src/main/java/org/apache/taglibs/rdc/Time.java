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

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;

import org.apache.taglibs.rdc.core.BaseModel;

/**
 * Datamodel for the time RDC. The time RDC will be associated with the time 
 * input, and the maximum and minimum times within which the time input 
 * must lie.
 *
 * @author Abhishek Verma
 * @author Rahul Akolkar
 */

public class Time extends BaseModel {
    // The time RDC will be associated with the time input,
    // and the maximum and minimum times within which the 
    // time input must lie.

    // Serial Version UID
    private static final long serialVersionUID = 1L;
    // Time returned cannot be before this time
    private java.util.Date minTime;
    // Time returned cannot be beyond this time
    private java.util.Date maxTime;

    // Error codes, defined in configuration file
    /**A constant for Error Code stating Invalid time*/
    public static final int ERR_INVALID_TIME = 1;

    /**A constant for Error Code stating the time entered is later 
     * than allowed */
    public static final int ERR_NEED_EARLIER_TIME = 2;

    /**A constant for Error Code stating the time entered is earlier 
     * than allowed */
    public static final int ERR_NEED_LATER_TIME = 3;

    /**
     * Sets default values for all data members
     */
    public Time() {
        super();
        this.minTime = null;
        this.maxTime = null;
    } // End Time constructor

    /**
     * Gets the maximum allowed time
     * 
     * @return the maximum allowed time
     */
    public String getMaxTime() {
        return calculateCanonicalizedValue(maxTime);
    }

    /**
     * Sets maximum allowed time
     * 
     * @param maxTime the maximum allowed time
     */
    public void setMaxTime(String maxTime) {
        if (maxTime != null) {
            this.maxTime = (java.util.Date)canonicalize(maxTime, true);
        }
    }

    /**
     * Gets the minimum allowed time
     * 
     * @return the minimum allowed time
     */
    public String getMinTime() {
        return calculateCanonicalizedValue(minTime);
    }

    /**
     * Sets minimum allowed time
     * 
     * @param minTime the minimum allowed time
     */
    public void setMinTime(String minTime) {
        if (minTime != null) {
            this.minTime = (java.util.Date)canonicalize(minTime, true);
        }
    }
    
    /**
     * Sets the value of time.
     * 
     * @param value the value of time returned by grammar (hhmma)
     * 5 P M will be 0500p
     * @see BaseModel#setValue(Object)
     */
    public void setValue(Object value) {
        if (value != null) {

            // Check the time for ambiguity
            setIsAmbiguous(isTimeAmbiguous(value));
            // If the time is ambiguous, then no point validating it
            // since it is not complete
            if (getIsAmbiguous() == Boolean.TRUE) {
                return;
            }
            this.value = baseCanonicalize(value);
            
            setIsValid(baseValidate(this.value, true));

            if (getIsValid() == Boolean.TRUE) {
                setCanonicalizedValue(calculateCanonicalizedValue(this.value));
            }
        }
    } //end setValue
    
    /**
     * Converts the min and max time strings to java.util.Date objects
     * 
     * @param input The time string to be converted (hhmma)
     * @param isAttribute Whether the input is passed via a tag attribute
     * @return The java.util.Date object for the time string
     */
    protected Object canonicalize(Object input, boolean isAttribute) {
        if (input == null) {
            return null;
        }
        String inputStr = (String) input;
        SimpleDateFormat formatter = new SimpleDateFormat("hhmma");
        if (!isAttribute || (inputStr.toLowerCase().indexOf('x') == -1)) {
            java.util.Date canonVal = formatter.parse(inputStr,
                new ParsePosition(0));
            if (canonVal == null) {
                if (isAttribute) {
                    throw new IllegalArgumentException("Cannot canonicalize " +
                    "value " + inputStr + " for time tag with ID " + getId());
                } else {
                    // will only be here if grammar allows invalid times
                    setErrorCode(ERR_INVALID_TIME);
                }
            }
            return canonVal;
        } else if (isAttribute && inputStr.toLowerCase().indexOf('x') != -1) {
            // offset the time value
            String hh = inputStr.substring(0, 2);
            String mm = inputStr.substring(2, 4);

            Calendar thisDay = new GregorianCalendar();
            try {
                if (!hh.equals("xx")) {
                    thisDay.add(GregorianCalendar.HOUR, Integer.parseInt(hh));
                }
                if (!mm.equals("xx")) {
                    thisDay.add(GregorianCalendar.MINUTE,Integer.parseInt(mm));
                }
                // am/pm will be ignored in offseting time
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Cannot canonicalize " +
                "value " + inputStr + " for time tag with ID " + getId());
            }
            // factor out the date - to do: add check for date wrap
            return formatter.parse(formatter.format(thisDay.getTime()),
                new ParsePosition(0));
        } else {
            // won't be here
            return input;
        }
    }
    
    /**
     * Validates the received input against validation constraints
     * 
     * @return TRUE if valid, FALSE if invalid
     */
    protected Boolean validate(Object newValue, boolean setErrorCode) {
        
        java.util.Date time = (java.util.Date) newValue;
        if (minTime != null && time.before(minTime)) {
            if (setErrorCode) setErrorCode(ERR_NEED_LATER_TIME);
            return Boolean.FALSE;
        }
        if (maxTime != null && time.after(maxTime)) {
            if (setErrorCode) setErrorCode(ERR_NEED_EARLIER_TIME);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * Builds a time string to be used for output into prompts etc.
     * For e.g., 0505am gets converted to 5 5 AM 
     *  
     * @return the time string for time
     */
    protected String calculateCanonicalizedValue(Object time) {
        if (time == null) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat("MMMM dd yyyy h m a");
        return df.format((java.util.Date) time);
    }

    /** 
     * Checks if the input received is ambiguous or not
     * 
     * @return TRUE if ambiguous input, FALSE otherwise
     */
    private Boolean isTimeAmbiguous(Object newValue) {
        if (((String) newValue).charAt(4) == '?') {
            int hh = Integer.parseInt(((String) newValue).substring(0, 2));
            int mm = Integer.parseInt(((String) newValue).substring(2, 4));
            if (getAmbiguousValues() != null) {
                getAmbiguousValues().clear();
            } else {
                setAmbiguousValues(new LinkedHashMap());
            }
            getAmbiguousValues().put(((String)newValue).substring(0, 4) + "am",
                hh + (mm > 0 ? " " + mm : "") + " a.m.");
            getAmbiguousValues().put(((String)newValue).substring(0, 4) + "pm",
                hh + (mm > 0 ? " " + mm : "") + " p.m.");
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

}
