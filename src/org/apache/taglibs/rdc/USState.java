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
 * Datamodel for the usState RDC. The usState RDC is associated with the 
 * US state input.
 *
 * @author Raghavendra Udupa
 */

public class USState extends BaseModel {
	// The usState RDC is associated with the  US state
	// input.

	// The default/initial value of usState
	private String initial;

	// Error codes, defined in configuration file

	/**A constant for Error Code stating no default value is specified */
	public static final int ERR_NO_DEFAULT = 1;

	/**
	  * Sets default values for all data members
	  */
	public USState() {
		this.value = null;
		this.initial = null;
	}

	/**
	 * Sets the value of US State input
	 * 
	 * @param value the new value of US state input
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
	 * Gets the initial US State value
	 *
	 * @return The default/initial US State value
	 */
	public String getInitial() {
		return initial;
	} // end getInitial()

	/**
	 * Sets the initial US State value
	 * 
	 * @param initial the default/initial US State value
	 */
	public void setInitial(String initial) {
		if (initial != null) {
			this.initial = initial;
		}
	} // end setInitial()

	/**
	 * Transforms initial to the corresponsing value
	 * 
	 * @param input the usState value
	 * 
	 * @return the canonicalized usState value
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

		return Boolean.TRUE;
	}
}