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
 */

package org.apache.taglibs.rdc;

import org.apache.taglibs.rdc.core.BaseModel;
import org.w3c.dom.Document;

/**
 *
 * Datamodel for list RDC. The SelectOne RDC will be associated with 
 * the list input. From a list of options the user selects one option .
 *
 * @author Tanveer Faruquie
 */
public class SelectOne extends BaseModel {
	// The SelectOne RDC will be associated with the list input. From
	// a list of options the user selects one option

	// The default/initial list value
	private String initial;
	// The list of available options for selection
	private Document options;

	/**A constant for Error Code stating no default value is specified */
	public static final int ERR_NO_DEFAULT = 1;

	/**
	 * Sets default values for all data members
	 * 
	 */
	public SelectOne() {
		super();
		this.value = null;
		this.options = null;
		this.initial = null;
	} // end SelectOne Constructor

	/**
	 * Sets the new list value
	 *
	 * @param value the list value
	 */
	public void setValue(String value) {
		this.value = canonicalize(value);

		setIsValid(validate());

		// Set the canonicalized value to the user utterance
		setCanonicalizedValue(getUtterance());

	} // end setValue()

	/**
	 * Gets the options list. This list has all the options from which
	 * the user selects a value.
	 * 
	 * @return options list.
	 */
	public Document getOptions() {
		return this.options;
	}

	/**
	 * Sets the options list. This list has all the options from which
	 * the user selects a value
	 * 
	 * @param options The options list.
	 */
	public void setOptions(Document options) {
		this.options = options;
	}

	/**
	 * Gets the default list value
	 *
	 * @return the default/initial list value
	 */
	public String getInitial() {
		return this.initial;
	} // end getInitial

	/**
	 * Sets the default list value
	 *
	 * @param initial the default/initial list value.
	 */
	public void setInitial(String initial) {
		if (initial != null) {
			this.initial = initial;
		}
	} // end setInitial

	/**
	 * Transforms initial to the corresponding value
	 * 
	 * @param input the list value
	 * 
	 * @return the canonicalized list value
	 */

	private String canonicalize(String input) {
		if (input == null) {
			return null;
		}

		if ("initial".equalsIgnoreCase(input)) {
			if (initial == null) {
				return null;
			}

			return this.initial;
		}

		return input;
	} // end canonicalize

	/**
	 * Validates the selected value.
	 *
	 * @return TRUE if valid, false otherwise
	 */
	private Boolean validate() {
		if (value == null) {
			setErrorCode(ERR_NO_DEFAULT);
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	} // end validate

} // end class SelectOne{}

// *** End of SelectOne.java ***
