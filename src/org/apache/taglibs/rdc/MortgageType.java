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

import java.io.*;
import java.util.*;
import org.apache.taglibs.rdc.core.BaseModel;
import java.util.*;
import java.util.regex.*;

/**
 * DataModel for the Mortgage Term RDC
 *
 *
 * @author Sindhu Unnikrishnan, Rahul
 */

public class MortgageType extends BaseModel {

	
	// value returned cannot be less than this value
	private String minTerm;
	//	value returned cannot be more than this value
	private String maxTerm;
	// Initial value of Mortgage Term
	private String initial;
	
	// The mortgage term input must conform to this pattern
	private String pattern;
	
	// Invalid pattern specified
	public static final int ERR_INVALID_MORTGAGE_TERM = 1;
	// If the current value is higher than the maximum value allowed
	public static final int ERR_NEED_LOWER_VALUE = 2;
	// If the current value is lower than the minimum value allowed
	public static final int ERR_NEED_HIGHER_VALUE = 3;
	// The default or initial value is null
	public static final int ERR_DEFAULT = 4;
	
	//error Codes defined in config file
	private static final int LESS = -1;
	private static final int EQUAL = 0;
	private static final int MORE = 1;


	public MortgageType() {
		this.initial = null;
		this.value = null;
		this.maxTerm = null;
		this.minTerm = null;
		this.pattern = "[0-9]{1,2}Y(fixed|adjustable)";

	}

	/**
	 * Set what the user said (this value is canonicalised).
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
	 * Get the max mortgage term that a user has specified in the attirbute
	 * @return the user specified maxTerm
	 */
	public String getMaxTerm() {
		return maxTerm;
	}

	/**
	 * Set the MaxTerm value.
	 * @param maxTerm The max mortgage term value.
	 */
	public void setMaxTerm(String maxTerm) {
		if (maxTerm != null) {
			this.maxTerm = normalize(maxTerm);
		}
	}

	/**
	 * Get what min Mortgage term that a user has specified in the attirbute
	 * @return the user specified minTerm
	 */
	public String getMinTerm() {
		return minTerm;
	}

	/**
	 * Set the MinTerm value.
	 * @param minTerm The min Mortgage term value.
	 */
	public void setMinTerm(String minTerm) {
		if (minTerm != null) {
			this.minTerm = normalize(minTerm);
		}
	}

	/**
	  * Get the initial mortgage term value
	  *
	  * @return initial The default/initial mortgage term value
	  */
	public String getInitial() {
		return initial.toString();
	} // end getInitial()

	/**
	 * Set the initial mortgage term value
	 * 
	 * @param initial The default/initial Mortgage term value (conforms to format)
	 */
	public void setInitial(String initial) {
		if (initial != null) {
			this.initial = normalize(initial);
			boolean patternValue = Pattern.matches(pattern, initial);
			if (!patternValue) {
				setErrorCode(ERR_INVALID_MORTGAGE_TERM);
				throw new IllegalArgumentException(
					"The required value of \"" + getId() + "\"and the " + 
					" mortgage term of initial value do not match.");

			} else {
				this.initial = normalize(initial);
			}
			if (this.initial == null) {
				throw new NullPointerException(
					"initial attribute of \"" + getId()	+ 
					"\" mortgage term is null.");
			}
		}
	} // end setInitial()

	/**
	 * Sets up the mortgage term strin into valid mortgage terms 
	 * 
	 * @param strInput The mortgage term input string
	 * @return The value of mortgage term 
	 */
	private String canonicalize(String strInput) {
		if (strInput == null)
			return strInput;

		if (strInput.equalsIgnoreCase("initial")) {
			if (initial == null
				|| (minTerm != null && ((cmpTerm(initial, minTerm) == LESS)))
				|| (maxTerm != null && ((cmpTerm(initial, maxTerm) == MORE)))) {
				return null;
			}
			return initial;
		} else
			return strInput;
	} // end canonicalize()

	/**
		* This method returns the mortgage term format 
		*
		*@param input The mortgage term input string
		*@return The value of mortgage term 
		*/
	private String normalize(String input) {

		boolean patternValue = Pattern.matches(pattern, input);
		if (!patternValue) {
			setErrorCode(ERR_INVALID_MORTGAGE_TERM);
			throw new IllegalArgumentException(
				"The required value of \"" + getId()+ "\"and the "
				+ " mortgage term of minimum or maximum value do not match.");

		}
		return input;
	}

	/**
	 * Compares the minimum and maximum mortgage terms
	 * against the mortgage input and returns LESS,MORE or
	 *  EQUAL accordingly
	 * 
	 * @param input a-> the mortgage term value
	 * b -> could be minimum and maximum terms
	 * @return could be LESS,MORE or EQUAL
	 */
	public int cmp(int a, int b) {
		return a < b ? LESS : a > b ? MORE : EQUAL;
	}

	/**
	 * This method compares the minimum and maximu Mortgage terms
	 * against the input and returns LESS,MORE or EQUAL accordingly
	 *  
	 * @param input s1-> the mortgage term value
	 * s2 -> could be minimum and maximum terms
	 *
	 * @return could be LESS,MORE or EQUAL
	 */
	public int cmpTerm(String s1, String s2) {

		return cmp(
			Integer.parseInt(s1.substring(0, s1.indexOf('Y'))),
			Integer.parseInt(s2.substring(0, s2.indexOf('Y'))));
	}

	/**
	 * Validate the received input against the validation constraints
	 * @return boolean - true if valid, false if invalid
	 */
	private Boolean validate() {

	
		if (value == null) {
			setErrorCode(ERR_DEFAULT);
			return Boolean.FALSE;
		}
		
		String val = value.toString();
	
		if (pattern != null) {
			boolean patternValue = Pattern.matches(pattern, val);
			if (!patternValue) {
				setErrorCode(ERR_INVALID_MORTGAGE_TERM);
				return Boolean.FALSE;
			}
		}

		if (cmpTerm(val, minTerm) == LESS) {
			setErrorCode(ERR_NEED_HIGHER_VALUE);
			return Boolean.FALSE;
		} else if (cmpTerm(val, maxTerm) == MORE) {
			setErrorCode(ERR_NEED_LOWER_VALUE);
			return Boolean.FALSE;
		}
		return Boolean.TRUE;

	}

}
