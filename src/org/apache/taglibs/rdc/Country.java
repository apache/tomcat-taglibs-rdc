/* 
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
 * Datamodel for the country RDC. The country RDC is associated with the
 * country input
 *
 * @author Sindhu Unnikrishnan
 */

public class Country extends BaseModel {

	// The default/initial value of country
	private String initial;

	// Error codes, defined in configuration file

	/**A constant for Error Code stating no default value is specified */
	public static final int ERR_NO_DEFAULT = 1;

	/**
	 * Sets default values for all data members
	 */
	public Country() {
		this.value = null;
		this.initial = null;
	}

	/**
	 * Sets the value of  the country
	 *
	 * @param value the value of  country
	 */
	public void setValue(String value) {
		if (value != null) {
			this.value = canonicalize(value);

			setIsValid(validate());

			if (getUtterance() == null) {
				setUtterance((String) value);
				// Setting the canonicalized value to be the user
				// utterance. When we come up with some reasonable
				// normalization of input, then we can replace this
				setCanonicalizedValue((String) value);
			}
		}
	} //end setValue

	/**
	 * Gets the initial country value
	 *
	 * @return The default/initial country value
	 */
	public String getInitial() {
		return initial;
	} // end getInitial()

	/**
	 * Sets the initial country value
	 *
	 * @param initial the default/initial country value
	 */
	public void setInitial(String initial) {
		if (initial != null) {
			this.initial = initial;
		}
	} // end setInitial()

	/**
	 * Transforms initial to the corresponsing value
	 *
	 * @param input the country value
	 *
	 * @return the canonicalized country value
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
