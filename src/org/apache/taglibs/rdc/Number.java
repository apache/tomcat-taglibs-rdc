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

import org.apache.taglibs.rdc.core.BaseModel;

/**
 * Datamodel for the number RDC. The number RDC will be associated with 
 * the number input and the maximum and minimum value within which the 
 * input's value must lie.
 *
 * @author Sandeep Jindal
 * @author Rahul Akolkar
 */

public class Number extends BaseModel {

	// The default/initial value of number
	// initial is of type String and not Double because 
	// Double.toString returns scientific notation for 
	// numbers less that 10 pow -3 and greater than 10 pow 7.
	// The value returned by the grammar is fixed-number representation
	// and initial must conform to that.

	// Maximum allowed value of the input; null value indicates 
	// no constraint on maximum value
	private Double minValue;

	// Minimum allowed value of the input; null value indicates 
	// no constraint on minimum value 
	private Double maxValue;

	// Error codes, defined in configuration file
	/**A constant for Error Code stating the number entered is 
	 * longer than allowed */
	public static final int ERR_NEED_SHORTER_NUMBER = 1;

	/**A constant for Error Code stating the number entered is 
	 * shorter than allowed */
	public static final int ERR_NEED_LONGER_NUMBER = 2;

	/**
	 * Sets default values for all data members
	 */
	public Number() {
		super();
		this.minValue = null;
		this.maxValue = null;
	}

	/**
	 * Gets the maximum allowed value of number
	 * 
	 * @return The maximum allowed value of number
	 */
	public String getMaxValue() {
		return maxValue.toString();
	}

	/**
	 * Sets the maximum allowed value of input
	 * 
	 * @param maxValue the maximum allowed value of input
	 */
	public void setMaxValue(String maxValue) {
		if (maxValue != null) {
			this.maxValue = (Double)canonicalize(maxValue, true);
		}
	}

	/**
	 * Gets the minimum allowed value of number
	 * @return The minimum allowed value of number
	 */

	public String getMinValue() {
		return minValue.toString();
	}

	/**
	 * Sets the minimum allowed value 
	 * 
	 * @param minValue the minimum allowed value of input
	 */
	public void setMinValue(String minValue) {
		if (minValue != null) {
			this.minValue = (Double)canonicalize(minValue, true);
		}
	}

	/**
	 * Custom canonicalization
	 */
	protected Object canonicalize(Object newValue, boolean isAttribute) {
		Double dabal = null;
		try {
			dabal = new Double((String) newValue);
		} catch (NumberFormatException e) {
			if (isAttribute) {
				throw new IllegalArgumentException("Cannot canonicalize " +
				"value " + newValue + " for number tag with ID " + getId());
			}
		}
		return dabal;
	}

	/**
	 * Validates the input against the given constraints
	 * 
	 * @return TRUE if valid, FALSE otherwise
	 */
	protected Boolean validate(Object newValue, boolean setErrorCode) {

		if (maxValue != null && maxValue.compareTo(newValue) < 0) {
			if (setErrorCode) setErrorCode(ERR_NEED_SHORTER_NUMBER);
			return Boolean.FALSE;
		}
		if (minValue != null && minValue.compareTo(newValue) > 0) {
			if (setErrorCode) setErrorCode(ERR_NEED_LONGER_NUMBER);
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

}
