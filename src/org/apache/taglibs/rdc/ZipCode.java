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
 * Datamodel for the zipCode RDC. The zipCode RDC is associated with 
 * the zip code input, the length which the inputs should have, and 
 * a pattern to which the input must conform.  
 *
 * @author Raghavendra Udupa
 */

public class ZipCode extends BaseModel {
	// The zipCode RDC is associated with the zip code
	// input, the length  which the 
	// inputs should have, and a pattern to which the input
	// must conform.

	//  Length of the input; -1 indicates no constraint on length.
	private int length;
	// The zip code input must conform to this pattern
	private String pattern;
	// The default/initial value of zipCode
	private String initial;

	// Error codes, defined in configuration file
	/**A constant for Error Code stating no default value is specified */
	public static final int ERR_NO_DEFAULT = 1;

	/**A constant for Error Code stating Invalid Zip Code */
	public static final int ERR_INVALID_ZIP_CODE = 2;

	/**A constant for Error Code stating Invalid length of Zip Code */
	public static final int ERR_NEED_CORRECT_LENGTH_ZIP_CODE = 3;

	/**
	  * Sets default values for all data members
	  */
	public ZipCode() {
		this.value = null;
		this.length = -1;
		// Default pattern allows any combination of digits
		this.pattern = "[0-9]+";
		this.initial = null;
	}

	/**
	 * Sets the value of Zip Code input
	 * 
	 * @param value the value of Zip Code input
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
	 * Sets the allowed length of input
	 * 
	 * @param length the allowed length of input
	 */
	public void setLength(String length) {
		if (length != null) {
			try {
				this.length = Integer.parseInt(length);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(
					"length attribute of \""
						+ getId()
						+ "\" zipCode tag is not an integer.");
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
						+ "\" zipCode tag has invalid pattern syntax.");

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
	 * Gets the initial Zip Code value
	 *
	 * @return The default/initial Zip Code value
	 */
	public String getInitial() {
		return initial;
	} // end getInitial()

	/**
	 * Sets the initial Zip Code value
	 * 
	 * @param initial The default/initial Zip Code value
	 */
	public void setInitial(String initial) {
		if (initial != null) {
			if (pattern != null) {
				if (!(Pattern.matches(pattern, (String) initial))) {
					this.initial = null;
					return;
				}
			}

			if (length > 0) {
				if (initial.length() != length) {
					this.initial = null;
					return;
				}
			}
			this.initial = initial;
		}
	} // end setInitial()

	/**
	 * Transforms initial to the corresponsing value
	 * 
	 * @param input the Zip Code value
	 * 
	 * @return the canonicalized Zip Code value
	 */
	private String canonicalize(String input) {
		if (input == null)
			return null;

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
				setErrorCode(ERR_INVALID_ZIP_CODE);
				return Boolean.FALSE;
			}
		}

		if (length > 0) {
			if (((String) value).length() != length) {
				setErrorCode(ERR_NEED_CORRECT_LENGTH_ZIP_CODE);
				return Boolean.FALSE;
			}
		}

		return Boolean.TRUE;
	}
}
