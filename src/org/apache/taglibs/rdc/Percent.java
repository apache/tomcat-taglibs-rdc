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
 */
public class Percent extends BaseModel {
	// The percent RDC will be associated with the percent input,
	// the maximum and minimum values within which the input must
	// lie .

	// the default/initial percentage
	private String initial;
	//this is the pattern for percent
	private String pattern;
	// Maximum allowed value for the percentage
	private int maxPercent;
	// Minimum allowed value for the percentage
	private int minPercent;

	// Error codes, defined in configuration file

	/**A constant for Error Code stating no default value is specified */
	public static final int ERR_NO_DEFAULT = 1;

	/**A constant for Error Code stating Invalid percent */
	public static final int ERR_INVALID_PERCENT = 2;

	/**A constant for Error Code stating the percent entered is
	 * larger than allowed */
	public static final int ERR_NEED_LOWER_VALUE = 3;

	/**A constant for Error Code stating the percent entered is
	 * smaller than allowed */
	public static final int ERR_NEED_HIGHER_VALUE = 4;

	/**
	 *
	 * Sets default values for all data model members
	 */
	public Percent() {
		super();
		this.value = null;
		this.initial = null;
		this.minPercent = 0;
		this.maxPercent = 0;
		this.pattern = "[0-9]{1,2}";
	}

	/**
	 * Overrides the utterance from BaseModel
	 *
	 * @return the utterance string
	 */

	public String getUtterance() {
		if (super.getUtterance() == null) {
			return null;
		} else {
			if (super.getUtterance().charAt(1) == ' ') {
				return super.getUtterance().replaceAll(" ", "") + "%";
			} else {
				return super.getUtterance();
			}
		}
	}

	/**
	 * Sets the percentage value.
	 *
	 * @param value the percentage value
	 */
	public void setValue(String value) {
		if (value != null) {
			this.value = canonicalize(value);
		}

		setIsValid(validate());

		if (getIsValid() == Boolean.TRUE) {
			setCanonicalizedValue((String) value + "%");

		}
	} // end setValue

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
			if (!(Pattern.matches(pattern, maxPercent))) {
				setErrorCode(ERR_INVALID_PERCENT);
				throw new IllegalArgumentException(
					"The required value of \""
						+ getId()
						+ "\"and the "
						+ " percent of  maximum value do not match.");

			} else {
				this.maxPercent = Integer.parseInt(maxPercent);
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
			if (!(Pattern.matches(pattern, minPercent))) {
				setErrorCode(ERR_INVALID_PERCENT);
				throw new IllegalArgumentException(
					"The required value of \""
						+ getId()
						+ "\"and the "
						+ " percent of  minimum value do not match.");
			} else {
				this.minPercent = Integer.parseInt(minPercent);
			}
		}
	} // end setMinPercent()

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
				throw new IllegalArgumentException(
					"pattern attribute of \""
						+ getId()
						+ "\" percent tag not in proper format.");
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
	 * Gets the initial percentage value
	 *
	 * @return The default/initial percentage value
	 */
	public String getInitial() {
		return initial;
	} // end getInitial()

	/**
	 * Sets the initial percentage value
	 *
	 * @param initial the default/initial percentage value
	 */
	public void setInitial(String initial) {
		if (initial != null) {
			this.initial = initial;

			//Validating initial
			if (pattern != null) {
				if (!(Pattern.matches(pattern, this.initial))) {
					this.initial = null;
					return;
				}
			}

			if (maxPercent > 0) {
				if (Integer.parseInt(this.initial) > maxPercent) {
					this.initial = null;
					return;
				}
			}

			if (minPercent > 0) {
				if (Integer.parseInt(this.initial) < minPercent) {
					this.initial = null;
					return;
				}
			}
		}
	} // end setInitial()

	/**
	 * Transforms initial to the corresponsing value
	 *
	 * @param input the percentage value
	 *
	 * @return the canonicalized percentage value
	 */
	private String canonicalize(String input) {
		if (input == null)
			return null;

		if ("initial".equalsIgnoreCase(input)) {
			if (initial == null) {
				return null;
			}

			return initial;
		}

		return input;
	}

	/**
	 * Validates the percent value against the given constraints
	 *
	 * @return TRUE if valid, FALSE otherwise
	 */
	private Boolean validate() {
		if (value == null) {
			setErrorCode(ERR_NO_DEFAULT);
			return Boolean.FALSE;
		}

		int val = Integer.parseInt(value.toString());

		if (pattern != null) {
			if (!(Pattern.matches(pattern, (String) value))) {
				setErrorCode(ERR_INVALID_PERCENT);
				return Boolean.FALSE;
			}
		}

		if (maxPercent > 0) {
			if (val > maxPercent) {
				setErrorCode(ERR_NEED_LOWER_VALUE);
				return Boolean.FALSE;
			}
		}

		if (minPercent > 0) {
			if (val < minPercent) {
				setErrorCode(ERR_NEED_HIGHER_VALUE);
				return Boolean.FALSE;
			}
		}

		if ((val % 5) == 0) {
			return Boolean.TRUE;
		} else {
			setErrorCode(ERR_INVALID_PERCENT);
			return Boolean.FALSE;
		}

	} // end validate

} // end class Percent{}

// *** End of Percent.java ***
