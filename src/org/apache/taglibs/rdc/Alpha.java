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
 * Datamodel for the alpha RDC. The alpha RDC will be associated 
 * with the alphabetical input (spelling), the maximum and minimum 
 * length within which the input's length must lie, and a pattern to 
 * which the input must conform. 
 *
 * @author Abhishek Verma
 */

public class Alpha extends BaseModel {
	// The alpha RDC will be associated with the alphabetical
	// input (spelling), the maximum and minimum length within 
	// which the input's length must lie, and a pattern to which 
	// the input must conform.

	// The default/initial value of alpha
	private String initial;
	// Maximum allowed length of the input; -1 indicates 
	// no constraint on maximum length
	private int minLength;
	// Minimum allowed length of the input; -1 indicates 
	// no constraint on minimum length 
	private int maxLength;
	// The alpha input must conform to this pattern
	private String pattern;

	// Error codes, defined in configuration file

	/**A constant for Error Code stating no default value is specified */
	public static final int ERR_NO_DEFAULT = 1;

	/**A constant for Error Code stating Invalid apha */
	public static final int ERR_INVALID_ALPHA = 2;

	/**A constant for Error Code stating the alpha entered is 
	 * larger than allowed */
	public static final int ERR_NEED_SHORTER_ALPHA = 3;

	/**A constant for Error Code stating the alpha entered is 
	 * smaller than allowed */
	public static final int ERR_NEED_LONGER_ALPHA = 4;

	/**
	 * Sets default values for all data members
	 */
	public Alpha() {
		this.value = null;
		this.initial = null;
		this.minLength = -1;
		this.maxLength = -1;
		// Default pattern allows any combination of alphabets
		this.pattern = "[a-zA-Z ']*";
	}

	/**
	 * Sets the value of alpha input
	 * 
	 * @param value the value of alpha input
	 */
	public void setValue(String value) {
		if (value != null) {
			this.value = canonicalize(value);

			setIsValid(validate());

			// Setting the canonicalized value to be the user
			// utterance. When we come up with some reasonable
			// normalization of input, then we can replace this
			setCanonicalizedValue(getUtterance());
		}
	} //end setValue

	/**
	 * Gets the maximum allowed length of input
	 * 
	 * @return the maximum allowed length of input
	 */
	public String getMaxLength() {
		return String.valueOf(maxLength);
	}

	/**
	 * Sets the maximum allowed length of input
	 * 
	 * @param maxLength the maximum allowed length of input
	 */
	public void setMaxLength(String maxLength) {
		if (maxLength != null) {
			try {
				this.maxLength = Integer.parseInt(maxLength);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(
					"maxLength attribute of \""
						+ getId()
						+ "\" alpha tag is not a number.");
			}
		}
	}

	/**
	 * Gets the minimum allowed length of input
	 * 
	 * @return the minimum allowed length of input
	 */
	public String getMinLength() {
		return String.valueOf(minLength);
	}

	/**
	 * Sets the minimum allowed length of input
	 * 
	 * @param minLength the minimum allowed length of input
	 */
	public void setMinLength(String minLength) {
		if (minLength != null) {
			try {
				this.minLength = Integer.parseInt(minLength);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(
					"minLength attribute of \""
						+ getId()
						+ "\" alpha tag is not a number.");
			}
		}
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
				throw new IllegalArgumentException(
					"pattern attribute of \""
						+ getId()
						+ "\" alpha tag not in proper format.");
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
	 * Gets the initial alpha value
	 *
	 * @return The default/initial alpha value
	 */
	public String getInitial() {
		return initial;
	} // end getInitial()

	/**
	 * Sets the initial alpha value
	 * 
	 * @param initial The default/initial alpha value
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

			if (maxLength > 0) {
				if (this.initial.length() > maxLength) {
					this.initial = null;
					return;
				}
			}

			if (minLength > 0) {
				if (this.initial.length() < minLength) {
					this.initial = null;
					return;
				}
			}
		}
	} // end setInitial()

	/**
	 * Transforms initial to the corresponsing value
	 * 
	 * @param input the alpha value
	 * 
	 * @return the canonicalized alpha value
	 */
	private String canonicalize(String input) {
		if (input == null) {
			return null;
		}

		if ("initial".equalsIgnoreCase(input)) {
			if (initial == null) {
				return null;
			}

			return (initial);
		}

		return input;
	}

	/**
	 * Validates the input against the given constraints
	 * 
	 * @return TRUE if valid, FALSE otherwise
	 */
	private Boolean validate() {
		if (value == null) {
			setErrorCode(ERR_NO_DEFAULT);
			return Boolean.FALSE;
		}

		if (pattern != null) {
			if (!(Pattern.matches(pattern, (String) value))) {
				setErrorCode(ERR_INVALID_ALPHA);
				return Boolean.FALSE;
			}
		}

		if (maxLength > 0) {
			if (((String) value).length() > maxLength) {
				setErrorCode(ERR_NEED_SHORTER_ALPHA);
				return Boolean.FALSE;
			}
		}

		if (minLength > 0) {
			if (((String) value).length() < minLength) {
				setErrorCode(ERR_NEED_LONGER_ALPHA);
				return Boolean.FALSE;
			}
		}

		return Boolean.TRUE;
	}
}