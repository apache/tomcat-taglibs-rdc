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
 */
public class Currency extends BaseModel {
	// The currency RDC will be associated with the currency input,
	// the maximum and minimum values within which the input must 
	// lie and the currency code. The currency value and the maximum
	// and minimum values has an expected format of the form UUUmm.nn 
	// or mm.nn where UUU is the ISO 4217 currency code, mmm is integer
	// part and nn is the fractional part. If currency code is not 
	// specified the default currency code for the default locale is assumed.

	// the default/initial amount 
	private String initial;
	// Maximum allowed value for the amount
	private String maxValue;
	// Minimum allowed value for the amount
	private String minValue;
	// ISO 4217 currency code for the amount 
	private String currencyCode;

	//If user asks for default value and no default value is supplied

	/**A constant for Error Code stating no default value is specified */
	public static final int ERR_NO_DEFAULT = 1;

	// If the current value is higher than the maximum value allowed
	/**A constant for Error Code stating the currency entered 
	 * is higher than allowed */
	public static final int ERR_NEED_LOWER_VALUE = 2;

	// If the current value is lower than the minimum value allowed
	/**A constant for Error Code stating the currency entered 
	 * is lower than allowed */
	public static final int ERR_NEED_HIGHER_VALUE = 3;

	/** 
	 * Sets default values for all data model members
	 */
	public Currency() {
		super();

		// Values are of the form mm.nn
		this.value = null;
		this.initial = null;
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
	 * Gets the currency code for the currency used
	 *
	 * @return The ISO 4217 currency code currently used
	 */
	public String getCurrencyCode() {
		return this.currencyCode;
	} // end getCurrencyCode

	/**
	 * Sets amount value of currency
	 *
	 * @param value the amount value (UUUmmm.dd or mmm.dd format)
	 */
	public void setValue(String value) {
		if (value != null) {
			this.value = setupValue(canonicalize(value.toUpperCase()));

			setIsValid(validate());

			// setting the canonicalized value to be the user utterance.
			setCanonicalizedValue(getUtterance());
		}

	} // end setValue

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
			this.maxValue = setupMinMaxValue(maxValue.toUpperCase());
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
			this.minValue = setupMinMaxValue(minValue.toUpperCase());
		}
	} // end setMinValue()

	/**
		 * Gets the initial amount value
		 *
		 * @return The default/initial amount value
		 */
	public String getInitial() {
		return this.initial;
	} // end getInitial

	/**
	 * Sets the initial amount value
	 *
	 * @param initial The default/initial amount value
	 */
	public void setInitial(String initial) {
		try {
			if (initial != null) {
				String value = null;

				DecimalFormat df = new DecimalFormat("#.#");
				java.util.Currency currency =
					java.util.Currency.getInstance(getCurrencyCode());
				df.setMaximumFractionDigits(
					currency.getDefaultFractionDigits());
				df.setMinimumFractionDigits(
					currency.getDefaultFractionDigits());

				if (Pattern
					.matches(
						"[A-Za-z][A-Za-z][A-Za-z][0-9]*.?[0-9]*",
						initial.toUpperCase())) {
					String strCode = initial.toUpperCase().substring(0, 3);
					if ((strCode.compareToIgnoreCase(currencyCode)) != 0) {
						throw new IllegalArgumentException(
							"The required currency code of \""
								+ getId()
								+ "\"and the "
								+ " currency code of initial value do not match.");
					}
					value = initial.toUpperCase().substring(3);
				} else {
					value = initial.toUpperCase();
				}

				Double valueDouble = new Double(value);
				this.initial =
					df.format(valueDouble.doubleValue()).toUpperCase();

			}
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException(
				"initial value of \""
					+ getId()
					+ "\" is not in proper "
					+ "format. The format is UUUmm.nn or mm.nn");
		} catch (NullPointerException e) {
			throw new IllegalArgumentException(
				"currency code of \"" + getId() + "\" is null.");
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
				"initial value of \""
					+ getId()
					+ "\" is not in proper "
					+ "format. The format is UUUmm.nn or mm.nn");
		}

	} // end setInitial()

	/**
	 * Sets up value of currency. The format is UUUmm or mm.nn. If value is in UUUmm.nn format
	 * the currency code UUU must be consistent with the currency code supplied by user or
	 * the default currency code.
	 * 
	 * @param strInput The amount value in UUUmm.nn or mm.nn format 
	 * @return formatted amount value in mm.nn format, strips currency code if any
	 */
	private String setupValue(String strInput) {
		try {
			if (strInput == null) {
				return strInput;
			}
			String value = null;

			DecimalFormat df = new DecimalFormat("#.#");
			java.util.Currency currency =
				java.util.Currency.getInstance(getCurrencyCode());
			df.setMaximumFractionDigits(currency.getDefaultFractionDigits());
			df.setMinimumFractionDigits(currency.getDefaultFractionDigits());

			if (Pattern
				.matches(
					"[A-Za-z][A-Za-z][A-Za-z][0-9]*.?[0-9]*",
					strInput.toUpperCase())) {
				String strCode = strInput.toUpperCase().substring(0, 3);
				if ((strCode.compareToIgnoreCase(currencyCode)) != 0) {
					throw new IllegalArgumentException(
						"The required currency code of \""
							+ getId()
							+ "\"and the entered"
							+ " currency code do not match.");
				}
				value = strInput.toUpperCase().substring(3);
			} else {
				value = strInput.toUpperCase();
			}

			Double valueDouble = new Double(value);
			return df.format(valueDouble.doubleValue()).toUpperCase();

		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException(
				"amount value of \""
					+ getId()
					+ "\" is not in proper "
					+ "format. The format is UUUmm.nn or mm.nn");
		} catch (NullPointerException e) {
			throw new IllegalArgumentException(
				"currency code of \"" + getId() + "\" is null.");
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
				"amount value of \""
					+ getId()
					+ "\" is not in proper "
					+ "format. The format is UUUmm.nn or mm.nn");
		}

	} // end setupValue()

	/**
	 * Sets up maximum or minimum permissible value. The format is UUUmm.nn or mm.nn. If value 
	 * is in UUUmm.nn format the currency code UUU is ignored and the default currency code or
	 * the supplied currency code is assumed.
	 * 
	 * @param strInput The maximum or minimum amount, either in UUUmm.nn or mm.nn format
	 * @return formatted maximum or minimum value in mm.nn format
	 */
	private String setupMinMaxValue(String strInput) {
		try {
			if (strInput == null) {
				return strInput;
			}
			String value = null;

			DecimalFormat df = new DecimalFormat("#.#");
			java.util.Currency currency =
				java.util.Currency.getInstance(getCurrencyCode());
			df.setMaximumFractionDigits(currency.getDefaultFractionDigits());
			df.setMinimumFractionDigits(currency.getDefaultFractionDigits());

			if (Pattern
				.matches(
					"[A-Za-z][A-Za-z][A-Za-z][0-9]*.?[0-9]*",
					strInput.toUpperCase())) {
				value = strInput.toUpperCase().substring(3);
			} else {
				value = strInput.toUpperCase();
			}

			Double valueDouble = new Double(value);
			return df.format(valueDouble.doubleValue()).toUpperCase();
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException(
				"minimum or maximum amount value of \""
					+ getId()
					+ "\" is not in proper "
					+ "format. The format is UUUmm.nn or mm.nn");
		} catch (NullPointerException e) {
			throw new IllegalArgumentException(
				"currency code of \"" + getId() + "\" is null.");
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
				"minimum or maximum amount value of \""
					+ getId()
					+ "\" is not in proper "
					+ "format. The format is UUUmm.nn or mm.nn");
		}

	} // end setupMaxMinValue()

	/**
	 * Transforms initial to the corresponding value
	 * 
	 * @param input the amount value
	 * 
	 * @return the canonicalized amount value
	 */

	private String canonicalize(String input) {
		if (input == null) {
			return null;
		}

		if ("initial".equalsIgnoreCase(input)) {
			if (initial == null) {
				return null;
			}

			Double val;
			Double maxVal;
			Double minVal;

			val = new Double(initial);

			if (maxValue != null) {
				maxVal = new Double(maxValue);
				if (val.doubleValue() > maxVal.doubleValue()) {
					return null;
				}
			}

			if (minValue != null) {
				minVal = new Double(minValue);
				if (val.doubleValue() < minVal.doubleValue()) {
					return null;
				}
			}

			return this.initial;
		}

		return input;
	}

	/**
	 * Validates the amount value against the given constraints
	 *
	 * @return TRUE if valid, FALSE otherwise
	 */
	private Boolean validate() {

		Double val;
		Double maxVal;
		Double minVal;

		if (value == null) {
			setErrorCode(ERR_NO_DEFAULT);
			return Boolean.FALSE;
		}

		val = new Double((String) value);

		if (maxValue != null) {
			maxVal = new Double(maxValue);
			if (val.doubleValue() > maxVal.doubleValue()) {
				setErrorCode(ERR_NEED_LOWER_VALUE);
				return Boolean.FALSE;
			}
		}

		if (minValue != null) {
			minVal = new Double(minValue);
			if (val.doubleValue() < minVal.doubleValue()) {
				setErrorCode(ERR_NEED_HIGHER_VALUE);
				return Boolean.FALSE;
			}
		}

		return Boolean.TRUE;
	} // end validate

} // end class Currency{}

// *** End of Currency.java ***
