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
 * @author Abhishek Verma and Rahul
 */

public class Date extends BaseModel {
	// The date RDC will be associated with the date input, the maximum and 
	// minimum dates within which the date input must lie, and a date format to
	// which the date input must conform.

	// Date returned must conform to this format
	private String format;
	// The default/initial value of date
	private java.util.Date initial;
	// Date returned cannot be beyond this date
	private java.util.Date maxDate;
	// Date returned cannot be before this date
	private java.util.Date minDate;

	// formatter for the date strings and objects
	private SimpleDateFormat formatter;

	// Error codes, corresponding prompts defined in configuration file
	/**A constant for Error Code stating no default value is specified */
	public static final int ERR_NO_DEFAULT = 1;

	/**A constant for Error Code stating Invalid date*/
	public static final int ERR_INVALID_DATE = 2;

	/**A constant for Error Code stating the date entered is earlier 
	 * than allowed */
	public static final int ERR_NEED_LATER_DATE = 3;

	/**A constant for Error Code stating the date entered is later 
	 * than allowed */
	public static final int ERR_NEED_EARLIER_DATE = 4;

	/**
	 * Sets default values for all data members
	 */
	public Date() {
		super();

		this.value = null;
		this.initial = null;
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
				throw new IllegalArgumentException(
					"format attribute of \""
						+ getId()
						+ "\" date tag is invalid.");
			}
		}
	} // end setFormat()

	/**
	 * Sets the value of date, after applying the format filter on the input
	 *
	 * @param strDate is what the grammar returns (MMddyyyy)
	 */
	public void setValue(String strDate) {
		if (strDate != null) {
			this.value = canonicalize(strDate);

			// Check the date for validity
			setIsValid(validate());

			if (getIsValid() == Boolean.TRUE)
				setCanonicalizedValue(buildDateString());
		}
	} // end setDate()

	/**
	 * Sets the Maximum Date value
	 *
	 * @param strMaxDate The Maximum Date value (conforms to format)
	 */
	public void setMaxDate(String strMaxDate) {
		if (strMaxDate != null) {
			maxDate = stringToDate(strMaxDate);
			if (maxDate == null) {
				throw new NullPointerException(
					"maxDate attribute of \""
						+ getId()
						+ "\" date tag not in proper date format.");
			}
		}
	} // end setMaxDate()

	/**
	 * Gets the Maximum Date value
	 *
	 * @return The Maximum Date value
	 */
	public String getMaxDate() {
		return maxDate.toString();
	} // end getMaxDate()

	/**
	 * Sets the Minimum Date value
	 *
	 * @param strMinDate The Minimum Date value (conforms to format)
	 */
	public void setMinDate(String strMinDate) {
		if (strMinDate != null) {
			minDate = stringToDate(strMinDate);
			if (minDate == null) {
				throw new NullPointerException(
					"minDate attribute of \""
						+ getId()
						+ "\" date tag not in proper date format.");
			}
		}
	} // end setMinDate()

	/**
	 * Gets the Minimum Date value
	 *
	 * @return The Minimum Date value
	 */
	public String getMinDate() {
		return minDate.toString();
	} // end getMinDate()

	/**
	 * Gets the initial Date value
	 *
	 * @return The default/initial Date value
	 */
	public String getInitial() {
		// Rahul - Need to define a standard for converting objects 
		// within components to usable strings in utterances
		return initial.toString();
	} // end getInitial()

	/**
	 * Sets the initial Date value
	 * 
	 * @param initial The default/initial Date value (conforms to format)
	 */
	public void setInitial(String initial) {
		if (initial != null) {
			this.initial = stringToDate(initial);
			if (this.initial == null) {
				throw new NullPointerException(
					"initial attribute of \""
						+ getId()
						+ "\" date tag not in proper date format.");
			}
		}
	} // end setInitial()

	/**
	 * Sets up the date string, converting phrases of today and tomorrow 
	 * into valid dates followed by the format filter
	 * 
	 * @param strInput The date input string
	 * @return The value of date (conforming to format)
	 */
	private String canonicalize(String strInput) {
		if (strInput == null)
			return strInput;

		Calendar thisDay = new GregorianCalendar();

		if ("today".equalsIgnoreCase(strInput)) {
			return formatter.format(thisDay.getTime());
		} else if ("tomorrow".equalsIgnoreCase(strInput)) {
			thisDay.add(Calendar.DATE, 1);
			return formatter.format(thisDay.getTime());
		} else if ("yesterday".equalsIgnoreCase(strInput)) {
			thisDay.add(Calendar.DATE, -1);
			return formatter.format(thisDay.getTime());
		} else if ("initial".equalsIgnoreCase(strInput)) {
			if (initial == null
				|| (minDate != null && initial.before(minDate))
				|| (maxDate != null && initial.after(maxDate))) {
				return null;
			}
			return formatter.format(initial);
		} else
			return doParseDate(strInput);
	} // end canonicalize()

	/**
	 * Sets up the min and max date conerting phrases like today and
	 * tomorrow into valid dates followed by the format filter. Also, 
	 * takes care of offset dates
	 *
	 * @param strInput The min or max date (conforms to format, except when
	 * specified using a phrase like today or tomorrow)
	 * @return The value of date filtered on format (if the need be)
	 */
	private java.util.Date stringToDate(String strInput) {
		if (strInput == null)
			return null;

		Calendar thisDay = new GregorianCalendar();

		if (strInput.toLowerCase().indexOf('x') >= 0)
			return doOffsetDate(strInput);
		else if ("today".equalsIgnoreCase(strInput)) {
			return formatter.parse(
				formatter.format(thisDay.getTime()),
				new ParsePosition(0));
		} else if ("tomorrow".equalsIgnoreCase(strInput)) {
			thisDay.add(Calendar.DATE, 1);
			return formatter.parse(
				formatter.format(thisDay.getTime()),
				new ParsePosition(0));
		} else if ("yesterday".equalsIgnoreCase(strInput)) {
			thisDay.add(Calendar.DATE, -1);
			return formatter.parse(
				formatter.format(thisDay.getTime()),
				new ParsePosition(0));
		} else
			return formatter.parse(strInput, new ParsePosition(0));
	} // end setMinMaxDate()

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

		if (nMMPos >= 0)
			mm = strDate.substring(nMMPos, nMMPos + 2);

		if (nDDPos >= 0)
			dd = strDate.substring(nDDPos, nDDPos + 2);

		if (nYYYYPos >= 0)
			yyyy = strDate.substring(nYYYYPos, nYYYYPos + 4);
		else if (nYYPos >= 0)
			yyyy = strDate.substring(nYYPos, nYYPos + 2);

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

		return formatter.parse(
			formatter.format(thisDay.getTime()),
			new ParsePosition(0));
	} // end doOffsetDate()

	/**
	 * Parses a date like 0806????, 06042004, etc. to produce a format
	 * conforming date string
	 * 
	 * @param date The date string (MMddyyyy)
	 * @return A date string (conforming to format)
	 */
	private String doParseDate(String strDate) {
		int nMMPos = format.indexOf("MM");
		int nDDPos = format.indexOf("dd");

		String mm = strDate.substring(0, 2);
		String dd = strDate.substring(2, 4);
		String yyyy = strDate.substring(4, 8);

		Calendar today = new GregorianCalendar();

		if ("????".equals(yyyy))
			yyyy = String.valueOf(today.get(Calendar.YEAR));

		if (nMMPos >= 0) {
			if ("??".equals(mm))
				return strDate;
		} else
			mm = "01";

		if (nDDPos >= 0) {
			if ("??".equals(dd))
				return strDate;
		} else
			dd = "01";

		SimpleDateFormat df = new SimpleDateFormat("MMddyyyy");
		df.setLenient(false);
		java.util.Date tempDate =
			df.parse(mm + dd + yyyy, new ParsePosition(0));

		if (tempDate == null)
			return mm + dd + yyyy;
		else
			return formatter.format(tempDate);
	} // end doParseDate()

	/**
	 * Validates the received input against the validation constraints
	 *
	 * @return True if valid, False if invalid
	 */
	private Boolean validate() {
		java.util.Date newDate;

		if (value == null) {
			setErrorCode(ERR_NO_DEFAULT);
			return Boolean.FALSE;
		}

		newDate = formatter.parse((String) value, new ParsePosition(0));
		if (newDate == null) {
			// This error code is set whenever the date is invalid
			// for e.g., 02292007, 02??2004 provided that dayof month
			// is required in the format string 
			setErrorCode(ERR_INVALID_DATE);
			return Boolean.FALSE;
		}

		if (minDate != null && newDate.before(minDate)) {
			setErrorCode(ERR_NEED_LATER_DATE);
			return Boolean.FALSE;
		}

		if (maxDate != null && newDate.after(maxDate)) {
			setErrorCode(ERR_NEED_EARLIER_DATE);
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	} // end validate

	/**
	 * Builds a date string to be used for normalized output
	 * For e.g., 07082004 gets converted to July 8, 2004 
	 *  
	 * @return The date string for date
	 */
	private String buildDateString() {
		int nMMPos = format.indexOf("MM");
		int nDDPos = format.indexOf("dd");
		int nYYPos = format.indexOf("yy");

		String format = "";

		if (nMMPos >= 0)
			format = "MMMM";

		if (nDDPos >= 0) {
			format = format + (format.length() > 0 ? " d" : "d");
		}

		if (nYYPos >= 0)
			format = format + (format.length() > 0 ? ", " : "") + "yyyy";

		SimpleDateFormat df = new SimpleDateFormat(format);

		return df.format(formatter.parse((String) value, new ParsePosition(0)));
	}

} // end class Date{}

// *** End of Date.java ***
