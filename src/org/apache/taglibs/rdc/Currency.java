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

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.taglibs.rdc.core.BaseModel;

/**
 * 
 * Datamodel for the currency RDC. The currency RDC will be associated 
 * with the currency input, the maximum and minimum values within which 
 * the input must lie and the currency code. 
 * 
 * The currency value and the maximum  and minimum values has an expected 
 * format of the form UUUmm.nn or mm.nn where UUU is the ISO 4217 currency 
 * code, mmm is integer part and nn is the fractional part. If currency 
 * code is not specified the defaultcurrency code for the default locale 
 * is assumed.
 *
 * @author Tanveer Faruquie
 * @author Rahul Akolkar
 */
public class Currency extends BaseModel {
	// The currency RDC will be associated with the currency input,
	// the maximum and minimum values within which the input must 
	// lie and the currency code. The currency value and the maximum
	// and minimum values has an expected format of the form UUUmm.nn 
	// or mm.nn where UUU is the ISO 4217 currency code, mmm is integer
	// part and nn is the fractional part. If currency code is not 
	// specified the default currency code for the default locale is assumed.

	// Maximum allowed value for the amount
	private String maxValue;
	// Minimum allowed value for the amount
	private String minValue;
	// ISO 4217 currency code for the amount 
	private String currencyCode;

	// If the current value is higher than the maximum value allowed
	/**A constant for Error Code stating the currency entered 
	 * is higher than allowed */
	public static final int ERR_NEED_LOWER_VALUE = 1;

	// If the current value is lower than the minimum value allowed
	/**A constant for Error Code stating the currency entered 
	 * is lower than allowed */
	public static final int ERR_NEED_HIGHER_VALUE = 2;

	/** 
	 * Sets default values for all data model members
	 */
	public Currency() {
		super();

		// Values are of the form mm.nn
		this.minValue = null;
		this.maxValue = null;

		// Get default locale and the corresponding currecy code for that locale
		// If there is no currency code for the default locale or if the default
		// currency code is not supported than the default currency code is USD 
		// (US dollars)
		try {
			Locale defaultLocale = Locale.getDefault();
			java.util.Currency defaultCurrency =
				java.util.Currency.getInstance(defaultLocale);
			if (defaultCurrency == null) {
				this.currencyCode = "USD";
			} else {
				this.currencyCode = defaultCurrency.getCurrencyCode();
			}
		} catch (NullPointerException e) {
			this.currencyCode = "USD";
		} catch (IllegalArgumentException e) {
			this.currencyCode = "USD";
		}
	}

	/**
	 * Gets the currency code for the currency used
	 *
	 * @return The ISO 4217 currency code currently used
	 */
	public String getCurrencyCode() {
		return this.currencyCode;
	} // end getCurrencyCode
	
	/**
	 * Sets the specified currency code 
	 *
	 * @param code The currency code as defined in ISO 4217 standards
	 */
	public void setCurrencyCode(String code) {
		if (code != null) {
			this.currencyCode = code.toUpperCase();
		}
	} // end setCurrencyCode

	/**
	 * Gets the maximum allowed value for the amount
	 *
	 * @return the maximum allowed value for the amount
	 */
	public String getMaxValue() {
		return this.maxValue;
	} // end getMaxValue

	/**
	 * Sets the maximum allowed value for the amount
	 *
	 * @param maxValue The maximum allowed value 
	 */
	public void setMaxValue(String maxValue) {
		if (maxValue != null) {
			this.maxValue = (String)canonicalize(maxValue.toUpperCase(), true);
		}
	} // end setMaxValue

	/**
	 * Gets the minimum allowed value for the amount
	 *
	 * @return the minimum allowed value for the amount
	 */
	public String getMinValue() {
		return this.minValue;
	} // end getMinValue

	/**
	 * Sets the minimum allowed value for the amount
	 *
	 * @param minValue The minimum allowed value
	 */
	public void setMinValue(String minValue) {
		if (minValue != null) {
			this.minValue = (String)canonicalize(minValue.toUpperCase(), true);
		}
	} // end setMinValue()
	
	/**
	 * Sets up maximum or minimum permissible value. The format is UUUmm.nn or mm.nn. If value 
	 * is in UUUmm.nn format the currency code UUU is ignored and the default currency code or
	 * the supplied currency code is assumed.
	 * 
	 * @param strInput The maximum or minimum amount, either in UUUmm.nn or mm.nn format
	 * @return formatted maximum or minimum value in mm.nn format
	 */
	protected Object canonicalize(Object input, boolean isAttribute) {
		if (input == null) {
			return null;
		}
		String inputStr = (String) input;
		try {
			if (inputStr == null) {
				return inputStr;
			}
			String newValue = null;

			DecimalFormat df = new DecimalFormat("#.#");
			java.util.Currency currency =
				java.util.Currency.getInstance(getCurrencyCode());
			df.setMaximumFractionDigits(currency.getDefaultFractionDigits());
			df.setMinimumFractionDigits(currency.getDefaultFractionDigits());

			if (Pattern.matches("[A-Za-z][A-Za-z][A-Za-z][0-9]*.?[0-9]*",
					inputStr.toUpperCase())) {
				String strCode = inputStr.toUpperCase().substring(0, 3);
				if ((!isAttribute && 
						(strCode.compareToIgnoreCase(currencyCode)) != 0)) {
					throw new IllegalArgumentException("The required " +
						"currency code of \"" + getId()	+ "\" and the" +
						" entered currency code do not match.");
				}
				newValue = inputStr.toUpperCase().substring(3);
			} else {
				newValue = inputStr.toUpperCase();
			}

			Double valueDouble = new Double(newValue);
			return df.format(valueDouble.doubleValue()).toUpperCase();
		} catch (IndexOutOfBoundsException e) {
			if (isAttribute) {
				throw new IllegalArgumentException("An amount value \"" +
				inputStr + "\" associated with currency RDC " + getId() +
				"\" is not in proper format. The format is UUUmm.nn or mm.nn");
			} else {
				return null;
			}
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("Currency code of \"" + 
				getId() + "\" is null.");
		} catch (NumberFormatException e) {
			if (isAttribute) {
				throw new IllegalArgumentException("An amount value \"" +
				inputStr + "\" associated with currency RDC " + getId() +
				"\" is not in proper format. The format is UUUmm.nn or mm.nn");
			} else {
				return null;
			}
		}

	} // end normalize()
	
	/**
	 * Validates the amount value against the given constraints
	 *
	 * @return TRUE if valid, FALSE otherwise
	 */
	protected Boolean validate(Object newValue, boolean setErrorCode) {

		Double val, maxVal, minVal;
		val = new Double((String) value);
		if (maxValue != null) {
			maxVal = new Double(maxValue);
			if (val.doubleValue() > maxVal.doubleValue()) {
				if (setErrorCode) setErrorCode(ERR_NEED_LOWER_VALUE);
				return Boolean.FALSE;
			}
		}
		if (minValue != null) {
			minVal = new Double(minValue);
			if (val.doubleValue() < minVal.doubleValue()) {
				if (setErrorCode) setErrorCode(ERR_NEED_HIGHER_VALUE);
				return Boolean.FALSE;
			}
		}
		return Boolean.TRUE;
	} // end customValidate()

} // end class Currency{}

// *** End of Currency.java ***
