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
 *
 *
 * @author Sindhu Unnikrishnan, Rahul
 */

public class Duration extends BaseModel {
	//The duration RDC will be associated
	//with the duration input, the maximum and minimum
	//duration within which the input's duration must lie, and a pattern to
	//which the input must conform.

	// Initial value of Duration
	private String initial;
	//	value returned cannot be more than this value
	private String maxDuration;
	// value returned cannot be less than this value
	private String minDuration;
	// The duration input must conform to this pattern
	private String pattern;

	//Error codes defined in the configuration file

	/**A constant for Error Code stating no default value is specified */
	public static final int ERR_DEFAULT = 1;

	/**A constant for Error Code stating Invalid duration */
	public static final int ERR_INVALID_DURATION = 2;

	/**A constant for Error Code stating the duration entered is
	 * larger than allowed */
	public static final int ERR_NEED_LOWER_VALUE = 3;

	/**A constant for Error Code stating the duration entered is
	 * smaller than allowed */
	public static final int ERR_NEED_HIGHER_VALUE = 4;

	//this constant that checks whether one value is less than the other
	private static final int LESS = -1;

	//this constant constant checks whether two values are equal
	private static final int EQUAL = 0;

	//this constant checks whether one value is more than the other
	private static final int MORE = 1;

	//this constant indicates the end of the year string
	private int INDEX_YEAR_END = -1;

	//this constant indicates the end of the month string
	private int INDEX_MONTH_END = -1;

	//this constant indicates the end of the day string
	private int INDEX_DAY_END = -1;

	//this constant indicates the begining of the year string
	private int INDEX_YEAR_BEGIN = -1;

	//this constant indicates the begining of the month string
	private int INDEX_MONTH_BEGIN = -1;

	//this constant indicates the begining of the day string
	private int INDEX_DAY_BEGIN = -1;

	/**
	 * Sets default values for all data members
	 */
	public Duration() {
		this.initial = null;
		this.value = null;
		maxDuration = null;
		minDuration = null;
		this.pattern = "P([0-9]{2}Y)?([0-9]{2}M)?([0-9]{2}D)?";

	}

	/**
	 * Sets what the user said (this value is canonicalised).
	 * @param Input is what the grammar returns
	 */
	public void setValue(String Input) {

		this.value = canonicalize(Input);
		setIsValid(validate());
		if (getIsValid() == Boolean.TRUE) {
			setCanonicalizedValue((String) value);
			if (getUtterance() == null) {
				setUtterance((String) value);
			}
		}

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
			this.maxDuration = normalize(maxDuration);
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
			this.minDuration = normalize(minDuration);
		}
	}

	/**
	 * Gets the initial duration value
	 *
	 * @return The default/initial duration value
	 */
	public String getInitial() {
		return initial.toString();
	} // end getInitial()

	/**
	 * Sets the initial duration value
	 *
	 * @param initial The default/initial duration value
	 */
	public void setInitial(String initial) {
		if (initial != null) {
			this.initial = normalize(initial);
			if (!(Pattern.matches(pattern, initial))) {
				setErrorCode(ERR_INVALID_DURATION);
				throw new IllegalArgumentException(
					"The required value of \""
						+ getId()
						+ "\"and the "
						+ " duration of initial value do not match.");

			} else {
				this.initial = normalize(initial);
			}
			if (this.initial == null) {
				throw new NullPointerException(
					"initial attribute of \""
						+ getId()
						+ "\" duration is null.");
			}
		}
	} // end setInitial()

	/**
	 * Transforms initial to the corresponsing value
	 *
	 * @param input the duration value
	 *
	 * @return the canonicalized duration  value
	 */
	private String canonicalize(String strInput) {
		if (strInput == null)
			return strInput;

		if ("initial".equalsIgnoreCase(strInput)) {
			if (initial == null
				|| (minDuration != null
					&& ((cmpDuration(initial, minDuration) == LESS)))
				|| (maxDuration != null
					&& ((cmpDuration(initial, maxDuration) == MORE)))) {
				return null;
			}
			return initial;
		} else
			return strInput;
	} // end canonicalize()

	/**
		* This method returns the full duration format if either partial
		* duration is spoken or retuns full duration if input is
		* full duration
		*
		* @param input The duration value(either partial or full)
		* @return The full duration format
		*/
	private String normalize(String input) {
		INDEX_YEAR_END = input.indexOf('Y');
		INDEX_MONTH_END = input.indexOf('M');
		INDEX_DAY_END = input.indexOf('D');

		if (!(Pattern.matches(pattern, input))) {
			setErrorCode(ERR_INVALID_DURATION);
			throw new IllegalArgumentException(
				"The required value of \""
					+ getId()
					+ "\"and the "
					+ " duration of minimum or maximum value do not match.");

		} else {
			if (INDEX_YEAR_END == -1) {
				input = "P00Y" + (input.length() > 1 ? input.substring(1) : "");
			}
			if (INDEX_DAY_END == -1) {
				input += "00D";
			}
			if (INDEX_MONTH_END == -1) {
				input =
					input.substring(0, input.indexOf('Y'))
						+ "Y00M"
						+ input.substring(input.indexOf('Y')+1);
			}
		}
		return input;
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
		INDEX_YEAR_END = s1.indexOf('Y');
		INDEX_MONTH_END = s1.indexOf('M');
		INDEX_DAY_END = s1.indexOf('D');
		INDEX_YEAR_BEGIN = s1.indexOf('P') + 1;
		INDEX_MONTH_BEGIN = INDEX_MONTH_END - 2;
		INDEX_DAY_BEGIN = INDEX_DAY_END - 2;

		switch (cmp(Integer
			.parseInt(s1.substring(INDEX_YEAR_BEGIN, INDEX_YEAR_END)),
			(Integer
				.parseInt(s2.substring(INDEX_YEAR_BEGIN, INDEX_YEAR_END))))) {
			case LESS :
				return LESS;
			case MORE :
				return MORE;
			case EQUAL :
				switch (cmp(Integer
					.parseInt(s1.substring(INDEX_MONTH_BEGIN, INDEX_MONTH_END)),
					(Integer
						.parseInt(
							s2.substring(
								INDEX_MONTH_BEGIN,
								INDEX_MONTH_END))))) {
					case LESS :
						return LESS;
					case MORE :
						return MORE;
					case EQUAL :
						switch (cmp(Integer
							.parseInt(
								s1.substring(INDEX_DAY_BEGIN, INDEX_DAY_END)),
							(Integer
								.parseInt(
									s2.substring(
										INDEX_DAY_BEGIN,
										INDEX_DAY_END))))) {
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
	 * Validates the received input against the validation constraints
	 * @return true if valid, false if invalid
	 */
	private Boolean validate() {

		if (value == null) {
			setErrorCode(ERR_DEFAULT);
			return Boolean.FALSE;
		}
		if (pattern != null) {
			if (!(Pattern.matches(pattern, (String) value))) {
				setErrorCode(ERR_INVALID_DURATION);
				return Boolean.FALSE;
			}
		}

		if (cmpDuration((String) value, minDuration) == LESS) {
			setErrorCode(ERR_NEED_HIGHER_VALUE);
			return Boolean.FALSE;
		} else if (cmpDuration((String) value, maxDuration) == MORE) {
			setErrorCode(ERR_NEED_LOWER_VALUE);
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}

}