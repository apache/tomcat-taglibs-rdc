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
 * Datamodel for the isbn (International Standards Book number) RDC. 
 * The isbn RDC is associated with the International Standards Book 
 * Number input and a pattern to which the input must confirm. The 
 * ISBN is a 10 digit number and should conform to this length.
 *
 * @author Tanveer Faruquie
 * @author Rahul Akolkar
 */

public class ISBN extends BaseModel {
	// The isbn RDC is associated with the International Standards Book Number
	// input and a pattern to which the input must confirm. The 
	// ISBN is a 10 digit
	// number and should conform to this length.

	// The ISBN input must conform to this pattern
	private String pattern;

	// Error codes, defined in configuration file
	/**A constant for Error Code stating Invalid ISBN Code */
	public static final int ERR_INVALID_ISBN_CODE = 1;

	/**A constant for Error Code stating the incorrect length of ISBN Code */
	public static final int ERR_NEED_CORRECT_LENGTH_ISBN_CODE = 2;

	// the length of ISBN is 10
	public static final int ISBN_LENGTH = 10;

	/**
	  * Sets default values for all data members
	  */
	public ISBN() {
		super();
		// Default pattern allows any combination of digits or X
		this.pattern = "[0-9X]+";
	}


	/**
	 * Sets the pattern string to which the ISBN must conform
	 * 
	 * @param pattern the pattern string to which the ISBN must conform
	 */
	public void setPattern(String pattern) {
		if (pattern != null) {
			try {
				Pattern.compile(pattern);
				this.pattern = pattern;
			} catch (PatternSyntaxException e) {
				throw new IllegalArgumentException("pattern attribute of \"" +
					getId()	+ "\" ssn tag not in proper format.");
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
	 * Validates the input against the given constraints
	 * 
	 * @return TRUE if valid, FALSE otherwise
	 */
	protected Boolean validate(Object newValue, boolean setErrorCode) {

		if (((String) newValue).length() != ISBN_LENGTH) {
			if (setErrorCode) setErrorCode(ERR_NEED_CORRECT_LENGTH_ISBN_CODE);
			return Boolean.FALSE;
		}
		if (pattern != null && !(Pattern.matches(pattern, (String)newValue))) {
			if (setErrorCode) setErrorCode(ERR_INVALID_ISBN_CODE);
			return Boolean.FALSE;
		}
		if (!checksum(((String) newValue).toUpperCase())) {
			if (setErrorCode) setErrorCode(ERR_INVALID_ISBN_CODE);
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	/**
	 * Verifies the checksum of the given ISBN value 
	 *  
	 * @return true if checksum is valid, false otherwise
	 */

	private boolean checksum(String isbn) {
		int sum = 0;
		int val;
		String numberValues = "0123456789X";

		for (int i = 0; i < isbn.length(); i++) {
			val = numberValues.indexOf(isbn.charAt(i));
			if (val >= 0) {
				if (val == 10 && (i != isbn.length() - 1)) {
					// X in wrong position, should be only in the last place
					return false;
				}
				sum += (10 - i) * val;
			} else {
				// invalid character
				return false;
			}
		}

		if ((sum % 11) == 0) {
			return true;
		} else {
			return false;
		}
	}
}
