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
import java.util.LinkedHashMap;

import org.apache.taglibs.rdc.core.BaseModel;

/**
 * Datamodel for the time RDC. The time RDC will be associated with the time 
 * input, and the maximum and minimum times within which the time input 
 * must lie.
 *
 * @author Abhishek Verma
 */

public class Time extends BaseModel {
	// The time RDC will be associated with the time input,
	// and the maximum and minimum times within which the 
	// time input must lie.

	// The default/initial value of time
	private java.util.Date initial;
	// Time returned cannot be before this time
	private java.util.Date minTime;
	// Time returned cannot be beyond this time
	private java.util.Date maxTime;

	// Error codes, defined in configuration file
	/**A constant for Error Code stating no default value is specified */
	public static final int ERR_NO_DEFAULT = 1;

	/**A constant for Error Code stating Invalid time*/
	public static final int ERR_INVALID_TIME = 2;

	/**A constant for Error Code stating the time entered is later 
	 * than allowed */
	public static final int ERR_NEED_EARLIER_TIME = 3;

	/**A constant for Error Code stating the time entered is earlier 
	 * than allowed */
	public static final int ERR_NEED_LATER_TIME = 4;

	/**
	 * Sets default values for all data members
	 */
	public Time() {
		this.value = null;
		this.initial = null;
		this.minTime = null;
		this.maxTime = null;
	} // End Time constructor

	/**
	 * Sets the value of time.
	 * 
	 * @param value the value of time returned by grammar (hhmma)
	 * 5 P M will be 0500p
	 */
	public void setValue(String value) {
		if (value != null) {
			this.value = canonicalize(value);

			// Check the time for ambiguity
			setIsAmbiguous(isTimeAmbiguous());
			// If the time is ambiguous, then no point validating it
			// since it is not complete
			if (getIsAmbiguous() == Boolean.TRUE) {
				return;
			}

			setIsValid(validate());

			if (this.value != null) {
				setCanonicalizedValue(
					buildTimeString(
						(new SimpleDateFormat("hhmma")).parse(
							(String) this.value,
							new ParsePosition(0))));
			}
		}
	} //end setValue

	/**
	 * Gets the maximum allowed time
	 * 
	 * @return the maximum allowed time
	 */
	public String getMaxTime() {
		return buildTimeString(maxTime);
	}

	/**
	 * Sets maximum allowed time
	 * 
	 * @param maxTime the maximum allowed time
	 */
	public void setMaxTime(String maxTime) {
		if (maxTime != null) {
			this.maxTime = stringToTime(maxTime);
			if (this.maxTime == null) {
				throw new IllegalArgumentException(
					"maxTime attribute of \""
						+ getId()
						+ "\" time tag not in proper format.");
			}
		}
	}

	/**
	 * Gets the minimum allowed time
	 * 
	 * @return the minimum allowed time
	 */
	public String getMinTime() {
		return buildTimeString(minTime);
	}

	/**
	 * Sets minimum allowed time
	 * 
	 * @param minTime the minimum allowed time
	 */
	public void setMinTime(String minTime) {
		if (minTime != null) {
			this.minTime = stringToTime(minTime);
			if (this.minTime == null) {
				throw new IllegalArgumentException(
					"minTime attribute of \""
						+ getId()
						+ "\" time tag not in proper format.");
			}
		}
	}

	/**
	 * Gets the initial time value
	 *
	 * @return The default/initial time value
	 */
	public String getInitial() {
		return buildTimeString(initial);
	} // end getInitial()

	/**
	 * Sets the initial time value
	 * 
	 * @param initial The default/initial time value
	 */
	public void setInitial(String initial) {
		if (initial != null) {
			this.initial = stringToTime(initial);
			if (this.initial == null) {
				throw new IllegalArgumentException(
					"initial attribute of \""
						+ getId()
						+ "\" time tag not in proper format.");
			}
			// Validating initial
			if (minTime != null) {
				if (this.initial.before(minTime)) {
					this.initial = null;
					return;
				}
			}

			if (maxTime != null) {
				if (this.initial.after(maxTime)) {
					this.initial = null;
					return;
				}
			}
		}
	} // end setInitial()

	/**
	 * Sets up the time string, converting phrases like initial 
	 * into valid time (hhmmaa)
	 * 
	 * @param time The time input string
	 * @return The value of time
	 */
	private String canonicalize(String time) {
		if (time == null) {
			return time;
		}

		if ("initial".equalsIgnoreCase(time)) {
			if (initial == null) {
				return null;
			}

			return (new SimpleDateFormat("hhmma")).format(initial);
		}

		return time + 'm';
	}

	/**
	 * Converts the min and max time strings to java.util.Date objects
	 * 
	 * @param str the time string to be converted (hhmma)
	 * 
	 * @return The java.util.Date object for the time string
	 */
	private java.util.Date stringToTime(String str) {
		if (str == null) {
			return null;
		}

		SimpleDateFormat formatter = new SimpleDateFormat("hhmma");
		if (str.toLowerCase().indexOf('x') < 0) {
			return formatter.parse(str, new ParsePosition(0));
		} else {
			// offset the time value
			String hh = str.substring(0, 2);
			String mm = str.substring(2, 4);
			String ampm = str.substring(4, 6);

			Calendar thisDay = new GregorianCalendar();
			try {
				if (!hh.equals("xx")) {
					thisDay.add(GregorianCalendar.HOUR, Integer.parseInt(hh));
				}

				if (!mm.equals("xx")) {
					thisDay.add(GregorianCalendar.MINUTE, Integer.parseInt(mm));
				}

				if (!ampm.equals("xx")) {
					if (ampm.toLowerCase().equals("am")) {
						thisDay.set(GregorianCalendar.AM_PM, Calendar.AM);
					} else if (ampm.toLowerCase().equals("pm")) {
						thisDay.set(GregorianCalendar.AM_PM, Calendar.PM);
					}
				}
			} catch (NumberFormatException e) {
				// If the minimum or maximum time is not parsable, then 
				// return null object
				return null;
			}

			return formatter.parse(
				formatter.format(thisDay.getTime()),
				new ParsePosition(0));
		}
	}

	/**
	 * Validates the received input against validation constraints
	 * 
	 * @return TRUE if valid, FALSE if invalid
	 */
	private Boolean validate() {
		java.util.Date time;

		if (value == null) {
			setErrorCode(ERR_NO_DEFAULT);
			return Boolean.FALSE;
		}

		SimpleDateFormat formatter = new SimpleDateFormat("hhmma");
		formatter.setLenient(false);

		time = formatter.parse((String) value, new ParsePosition(0));
		if (time == null) {
			// This error code is set whenever the time is invalid
			setErrorCode(ERR_INVALID_TIME);
			return Boolean.FALSE;
		}

		if (minTime != null) {
			if (time.before(minTime)) {
				setErrorCode(ERR_NEED_LATER_TIME);
				return Boolean.FALSE;
			}
		}

		if (maxTime != null) {
			if (time.after(maxTime)) {
				setErrorCode(ERR_NEED_EARLIER_TIME);
				return Boolean.FALSE;
			}
		}

		return Boolean.TRUE;
	}

	/** 
	 * Checks if the input received is ambiguous or not
	 * 
	 * @return TRUE if ambiguous input, FALSE otherwise
	 */
	private Boolean isTimeAmbiguous() {
		if (value == null) {
			return Boolean.FALSE;
		}

		int hh = Integer.parseInt(((String) value).substring(0, 2));
		int mm = Integer.parseInt(((String) value).substring(2, 4));

		if (((String) value).charAt(4) == '?') {
			if (getAmbiguousValues() != null) {
				getAmbiguousValues().clear();
			} else {
				setAmbiguousValues(new LinkedHashMap());
			}

			getAmbiguousValues().put(
				((String) value).substring(0, 4) + "a",
				hh + (mm > 0 ? " " + mm : "") + " a.m.");
			getAmbiguousValues().put(
				((String) value).substring(0, 4) + "p",
				hh + (mm > 0 ? " " + mm : "") + " p.m.");
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	/**
	 * Builds a time string to be used for normalized output
	 * For e.g., 0505am gets converted to 5 5 AM 
	 *  
	 * @returns the time string for time
	 */
	private String buildTimeString(java.util.Date time) {
		if (time == null) {
			return null;
		}

		SimpleDateFormat df = new SimpleDateFormat("h m a");
		Calendar tempTime = new GregorianCalendar();
		tempTime.setTime(time);
		if (tempTime.get(Calendar.MINUTE) == 0) {
			df.applyPattern("h a");
		}
		return df.format(time);
	}
}
