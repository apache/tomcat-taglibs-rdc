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
 * Datamodel for the Credit Card Type.
 *
 * @author Pankaj Kankar
 */

public class CreditCardType extends BaseModel {

	// The default/initial value of CreditCardType
	private String initial;

	// Error codes, defined in configuration file

	/**A constant for Error Code stating Invalid Credit Card Type */
	public static final int  ERR_NO_DEFAULT = 1;

	/**
		* Sets default values for all data members
		*/
	public CreditCardType() {
		super();
		this.value = null;
		this.initial = null;
	}

	/**
	 * Sets the value of the CreditCardType
	 * 
	 * @param cardType the value of CreditCardType
	 */
	public void setValue(String cardType) {
		if (cardType != null) {
			this.value = canonicalize(cardType);

			setIsValid(validate());

			//if some other reasonable normalization is thought of
			//this can be replaced
			if (getIsValid() == Boolean.TRUE) {
				setCanonicalizedValue((String) value);
			}
		}
	} //end setValue

	/**
	 * Gets the initial CreditCardType value
	 *
	 * @return The default/initial CreditCardType value
	 */
	public String getInitial() {
		return initial;
	} // end getInitial()

	/**
	 * Sets the initial CreditCardType value
	 * 
	 * @param initial The default/initial CreditCardType value
	 */
	public void setInitial(String initial) {
		if (initial != null) {
			this.initial = initial;
		}
	} // end setInitial()

	/**
	 * Transforms initial to the corresponsing value
	 * 
	 * @param input The CreditCardType value
	 * 
	 * @return the canonicalized CreditCardType value
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
			setErrorCode( ERR_NO_DEFAULT);
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}
}
