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

import org.apache.taglibs.rdc.core.BaseModel;

/**
 * Datamodel for the date RDC. The date RDC will be associated with 
 * the date input, the maximum and minimum dates within which the 
 * date input must lie, and a date format to which the date input must 
 * conform.
 * 
 * @author Abhishek Verma
 * @author Rahul Akolkar
 */

public class Date extends BaseModel {
    // The date RDC will be associated with the date input, the maximum and 
    // minimum dates within which the date input must lie, and a date format to
    // which the date input must conform.

    // Date returned must conform to this format
    private String format;
    // Date returned cannot be beyond this date
    private java.util.Date maxDate;
    // Date returned cannot be before this date
    private java.util.Date minDate;

    // formatter for the date strings and objects
    private SimpleDateFormat formatter;

    // Error codes, corresponding prompts defined in configuration file
    /**A constant for Error Code stating Invalid date*/
    public static final int ERR_INVALID_DATE = 1;

    /**A constant for Error Code stating the date entered is earlier 
     * than allowed */
    public static final int ERR_NEED_LATER_DATE = 2;

    /**A constant for Error Code stating the date entered is later 
     * than allowed */
    public static final int ERR_NEED_EARLIER_DATE = 3;

    /**
     * Sets default values for all data members
     */
    public Date() {
        super();
        this.minDate = null;
        this.maxDate = null;

        // MM - month, dd - day of month, yyyy - year
        // not using mm (lowercase), because it represents minutes in DateFormat
        this.format = "MMddyyyy";
        this.formatter = new SimpleDateFormat(format);
        this.formatter.setLenient(false);
    } // end Date constructor

    /**
     * Sets the date string format to use for vaidation
     *
     * @param strDateFormat The date format string
     */
    public void setFormat(String strDateFormat) {
        if (strDateFormat != null) {
            try {
                formatter.applyPattern(strDateFormat);
                this.format = strDateFormat;
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("format attribute of \"" +
                    getId() + "\" date tag is invalid.");
            }
        }
    } // end setFormat()

    /**
     * Sets the Maximum Date value
     *
     * @param strMaxDate The Maximum Date value (conforms to format)
     */
    public void setMaxDate(String strMaxDate) {
        if (strMaxDate != null) {
            this.maxDate = (java.util.Date)canonicalize(strMaxDate, true);
        }
    } // end setMaxDate()

    /**
     * Gets the Maximum Date value
     *
     * @return The Maximum Date value
     */
    public String getMaxDate() {
        return calculateCanonicalizedValue(maxDate);
    } // end getMaxDate()

    /**
     * Sets the Minimum Date value
     *
     * @param strMinDate The Minimum Date value (conforms to format)
     */
    public void setMinDate(String strMinDate) {
        if (strMinDate != null) {
            this.minDate = (java.util.Date)canonicalize(strMinDate, true);
        }
    } // end setMinDate()

    /**
     * Gets the Minimum Date value
     *
     * @return The Minimum Date value
     */
    public String getMinDate() {
        return calculateCanonicalizedValue(minDate);
    } // end getMinDate()
    
    /**
     * Get the date currently associated with this date component as a string
     * in the specified format.
     * 
     * @return value the value as a string
     */
    public Object getValue() {
        if (this.value == null) {
            return null;
        }
        return formatter.format((java.util.Date) this.value);
    }

    /**
     * Sets up the date string, converting phrases of today and tomorrow 
     * into valid dates followed by the format filter
     * 
     * @param strInput The date input string
     * @return The value of date (conforming to format)
     */
    protected Object canonicalize(Object input, boolean isAttribute) {
        if (input == null) {
            return null;
        }
        Calendar thisDay = new GregorianCalendar();
        String strInput = (String) input;
        if ("today".equalsIgnoreCase(strInput)) {
            return thisDay.getTime();
        } else if ("tomorrow".equalsIgnoreCase(strInput)) {
            thisDay.add(Calendar.DATE, 1);
            return thisDay.getTime();
        } else if ("yesterday".equalsIgnoreCase(strInput)) {
            thisDay.add(Calendar.DATE, -1);
            return thisDay.getTime();
        } else {
            if (isAttribute) {
                if (strInput.toLowerCase().indexOf('x') >= 0) {
                    return doOffsetDate(strInput);
                }
                return formatter.parse(strInput, new ParsePosition(0));
            } else {
                java.util.Date inputDate = doParseDate(strInput);
                if (inputDate == null) {
                    // will be here if the grammar allows invalid dates
                    // example: 02292007
                    setErrorCode(ERR_INVALID_DATE);
                }
                return inputDate;
            }
        }
    } // end canonicalize()

    /**
     * Validates the received input against the validation constraints
     *
     * @return True if valid, False if invalid
     */
    protected Boolean validate(Object newValue, boolean setErrorCode) {
        
        java.util.Date newDate = (java.util.Date) newValue; 
        if (minDate != null && newDate.before(minDate)) {
            if (setErrorCode) setErrorCode(ERR_NEED_LATER_DATE);
            return Boolean.FALSE;
        }
        if (maxDate != null && newDate.after(maxDate)) {
            if (setErrorCode) setErrorCode(ERR_NEED_EARLIER_DATE);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    } // end customValidate()

    /**
     * Builds a date string to be used for normalized output
     * For e.g., 07082004 gets converted to July 8, 2004 
     *  
     * @return The date string for date
     */
    protected String calculateCanonicalizedValue(Object newValue) {
        String format = "";
        if (newValue == null) {
            return format;
        }
        int nMMPos = format.indexOf("MM");
        int nDDPos = format.indexOf("dd");
        int nYYPos = format.indexOf("yy");
        if (nMMPos >= 0) {
            format = "MMMM";
        }
        if (nDDPos >= 0) {
            format = format + (format.length() > 0 ? " d" : "d");
        }
        if (nYYPos >= 0) {
            format = format + (format.length() > 0 ? ", " : "") + "yyyy";
        }
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format((java.util.Date) newValue);
    }

    /**
     * Sets dates that are partially specified like 06xxxxxx (6 months from today). 
     * 
     * @param date The partial date
     * @return A date string from a partially specified date string (conforming
     * to format)
     */
    private java.util.Date doOffsetDate(String strDate) {
        int nMMPos = format.indexOf("MM");
        int nDDPos = format.indexOf("dd");
        int nYYPos = format.indexOf("yy");
        int nYYYYPos = format.indexOf("yyyy");

        String mm = "xx";
        String dd = "xx";
        String yyyy = "xxxx";

        if (nMMPos >= 0) {
            mm = strDate.substring(nMMPos, nMMPos + 2);
        }
        if (nDDPos >= 0) {
            dd = strDate.substring(nDDPos, nDDPos + 2);
        }
        if (nYYYYPos >= 0) {
            yyyy = strDate.substring(nYYYYPos, nYYYYPos + 4);
        } else if (nYYPos >= 0) {
            yyyy = strDate.substring(nYYPos, nYYPos + 2);
        }

        Calendar thisDay = new GregorianCalendar();
        if (!mm.equals("xx")) {
            thisDay.add(Calendar.MONTH, Integer.parseInt(mm));
        }
        if (!dd.equals("xx")) {
            thisDay.add(Calendar.DATE, Integer.parseInt(dd));
        }
        if (!(yyyy.equals("xxxx") || yyyy.equals("xx"))) {
            thisDay.add(Calendar.YEAR, Integer.parseInt(yyyy));
        }
        return thisDay.getTime();
    } // end doOffsetDate()

    /**
     * Parses a date like 0806????, 06042004, etc. to produce a date object
     * 
     * @param date The date string (MMddyyyy)
     * @return Date The Date object
     */
    private java.util.Date doParseDate(String strDate) {

        String mm = strDate.substring(0, 2);
        String dd = strDate.substring(2, 4);
        String yyyy = strDate.substring(4, 8);

        Calendar today = new GregorianCalendar();
        if ("????".equals(yyyy)) {
            yyyy = String.valueOf(today.get(Calendar.YEAR));
        }
        if ("??".equals(mm)) {
            mm = String.valueOf(today.get(Calendar.MONTH));
        }
        if ("??".equals(dd)) {
            dd = String.valueOf(today.get(Calendar.DATE));
        }
        SimpleDateFormat df = new SimpleDateFormat("MMddyyyy");
        df.setLenient(false);
        return df.parse(mm + dd + yyyy, new ParsePosition(0));
    } // end doParseDate()

} // end class Date{}

// *** End of Date.java ***
