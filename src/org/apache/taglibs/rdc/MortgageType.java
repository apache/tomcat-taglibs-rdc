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
 * DataModel for the Mortgage Term RDC
 *
 *
 * @author Sindhu Unnikrishnan
 * @author Rahul Akolkar
 */

public class MortgageType extends BaseModel {

	
	// value returned cannot be less than this value
	private String minTerm;
	//	value returned cannot be more than this value
	private String maxTerm;
	// The mortgage term input must conform to this pattern
	private String pattern;
	
	// Error Codes defined in config file
	// Invalid pattern specified
	public static final int ERR_INVALID_MORTGAGE_TERM = 1;
	// If the current value is higher than the maximum value allowed
	public static final int ERR_NEED_LOWER_VALUE = 2;
	// If the current value is lower than the minimum value allowed
	public static final int ERR_NEED_HIGHER_VALUE = 3;

	private static final int LESS = -1;
	private static final int EQUAL = 0;
	private static final int MORE = 1;

	public MortgageType() {
		super();
		this.maxTerm = null;
		this.minTerm = null;
		this.pattern = "[0-9]{1,2}Y(fixed|adjustable)";
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
			this.maxTerm = (String)canonicalize(maxTerm, true);
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
			this.minTerm = (String)canonicalize(minTerm, true);
		}
	}

	/**
		* This method returns the mortgage term format 
		*
		*@param input The mortgage term input string
		*@return The value of mortgage term 
		*/
	protected Object canonicalize(Object input, boolean isAttribute) {

		boolean patternValue = Pattern.matches(pattern, (String) input);
		if (!patternValue) {
			if (isAttribute) {
				throw new IllegalArgumentException("The required value of \"" +
				getId()+ "\"and the mortgage term of minimum or maximum " +
				"value do not match.");
			} else {
				return null;
			}
		}
		return input;
	}
	
	/**
	 * Validate the received input against the validation constraints
	 * @return boolean - true if valid, false if invalid
	 */
	protected Boolean validate(Object newValue, boolean setErrorCode) {
		
		String val = newValue.toString();	
		if (pattern != null) {
			boolean patternValue = Pattern.matches(pattern, val);
			if (!patternValue) {
				if (setErrorCode) setErrorCode(ERR_INVALID_MORTGAGE_TERM);
				return Boolean.FALSE;
			}
		}
		if (minTerm != null && cmpTerm(val, minTerm) == LESS) {
			if (setErrorCode) setErrorCode(ERR_NEED_HIGHER_VALUE);
			return Boolean.FALSE;
		} else if (maxTerm != null && cmpTerm(val, maxTerm) == MORE) {
			if (setErrorCode) setErrorCode(ERR_NEED_LOWER_VALUE);
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	/**
	 * This method compares the minimum and maximu Mortgage terms
	 * against the input and returns LESS,MORE or EQUAL accordingly
	 *  
	 * @param String s1-> the mortgage term value
	 * @param String s2 -> could be minimum and maximum terms
	 *
	 * @return could be LESS,MORE or EQUAL
	 */
	private int cmpTerm(String s1, String s2) {
		return cmp(
			Integer.parseInt(s1.substring(0, s1.indexOf('Y'))),
			Integer.parseInt(s2.substring(0, s2.indexOf('Y'))));
	}
	
	/**
	 * Compares the minimum and maximum mortgage terms
	 * against the mortgage input and returns LESS,MORE or
	 *  EQUAL accordingly
	 * 
	 * @param a-> the mortgage term value
	 * @param int b -> could be minimum and maximum terms
	 * 
	 * @return could be LESS,MORE or EQUAL
	 */
	private int cmp(int a, int b) {
		return a < b ? LESS : a > b ? MORE : EQUAL;
	}

}
