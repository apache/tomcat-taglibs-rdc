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
 * Datamodel for the ssn (social security number) RDC. 
 * The ssn RDC is associated with the Social Security Number input and a 
 * pattern to which the input must conform. The SSN is a 9 digit number 
 * and should conform to this length.
 *
 * @author Tanveer Faruquie
 */

public class SocialSecurityNumber extends BaseModel {
	// The ssn RDC is associated with the Social Security Number
	// input and a pattern to which the input must conform. The ssn is a 9 digit
	// number and should conform to this length.

	// The social security number input must conform to this pattern
	private String pattern;
	// The default/initial value of social security number
	private String initial;

	// Error codes, defined in configuration file

	/**A constant for Error Code stating no default value is specified */
	public static final int ERR_NO_DEFAULT = 1;

	/**A constant for Error Code stating Invalid SSN */
	public static final int ERR_INVALID_SSN_CODE = 2;

	/**A constant for Error Code stating the incorrect length of SSN */
	public static final int ERR_NEED_CORRECT_LENGTH_SSN_CODE = 3;

	// the length of the social security number is 9
	public static final int SSN_LENGTH = 9;

	/**
	  * Sets default values for all data members
	  */
	public SocialSecurityNumber() {
		super();
		this.value = null;
		// Default pattern allows any combination of digits
		this.pattern = "[0-9]+";
		this.initial = null;
	}

	/**
	 * Sets the value of social security number input
	 * 
	 * @param value the value of social security number
	 */
	public void setValue(String value) {
		if (value != null) {
			this.value = canonicalize(value);

			setIsValid(validate());

			// Setting the canonicalized value to be the user
			// utterance. 
			setCanonicalizedValue(getUtterance());
		}
	} //end setValue

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
						+ "\" ssn tag not in proper format.");
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
	 * Gets the initial social security number value
	 *
	 * @return The default/initial social security number value
	 */
	public String getInitial() {
		return this.initial;
	} // end getInitial()

	/**
	 * Sets the initial social security number value
	 * 
	 * @param initial The default/initial social security number value
	 */
	public void setInitial(String initial) {
		if (initial != null) {
			if (pattern != null) {
				if (!(Pattern.matches(pattern, (String) initial))) {
					this.initial = null;
					return;
				}
			}

			if (initial.length() != SSN_LENGTH) {
				this.initial = null;
				return;
			}

			this.initial = initial;
		}
	} // end setInitial()

	/**
	 * Transforms initial to the corresponsing value
	 * 
	 * @param input the social security number value
	 * 
	 * @return the canonicalized social security number value
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
				setErrorCode(ERR_INVALID_SSN_CODE);
				return Boolean.FALSE;
			}
		}

		if (((String) value).length() != SSN_LENGTH) {
			setErrorCode(ERR_NEED_CORRECT_LENGTH_SSN_CODE);
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}
}
