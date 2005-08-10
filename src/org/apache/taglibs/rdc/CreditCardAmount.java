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
import org.apache.taglibs.rdc.core.Constants;
import org.apache.taglibs.rdc.core.Grammar;

/**
 * 
 * Datamodel for the credit card payment RDC. 
 * 
 * The amount value and the maximum and minimum values have an expected 
 * format of the form UUUmm.nn or mm.nn where UUU is the ISO 4217 currency 
 * code, mmm is integer part and nn is the fractional part. If currency 
 * code is not specified the default currency code for the default locale 
 * is assumed.
 *
 * @author Rahul Akolkar
 */
public class CreditCardAmount extends BaseModel {
    // The amount value, balance, the maximum and minimum amounts due
    // have an expected format of the form UUUmm.nn or mm.nn
    // where UUU is the ISO 4217 currency code, mmm is integer
    // part and nn is the fractional part. If currency code is not 
    // specified the default currency code for the default locale is used.

    // Maximum allowed amount for this payment
    private String maxAmount;
    // Minimum allowed amount for this payment
    private String minAmount;
    // The account balance
    private String balance;
    // ISO 4217 currency code for the amount 
    private String currencyCode;
    // Maximum number of denials before a graceful exit
    private int maxDenials;
    
    // Number of denials
    private int denialCount;
    
    // Component Grammars
    private Grammar minimumDueGrammar;
    private Grammar fullAmountGrammar;
    public void setMinimumDueGrammar(Grammar g) { minimumDueGrammar = g; }
    public void setFullAmountGrammar(Grammar g) { fullAmountGrammar = g; }

    // If the current value is higher than the maximum value allowed
    /**A constant for Error Code stating the currency entered 
     * is higher than allowed */
    public static final int ERR_NEED_LOWER_AMOUNT = 1;

    // If the current value is lower than the minimum value allowed
    /**A constant for Error Code stating the currency entered 
     * is lower than allowed */
    public static final int ERR_NEED_HIGHER_AMOUNT = 2;

    /** 
     * Sets default values for all data model members
     */
    public CreditCardAmount() {
        super();
        this.minAmount = null;
        this.maxAmount = null;
        this.maxDenials = 2;
        this.denialCount = 0;
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
     * Record user response to confirmation
     *
     * @param confirmed The user confirmation
     */
    public void setConfirmed(Boolean confirmed) {
        super.setConfirmed(confirmed);
        if (!confirmed.booleanValue()) {
            denialCount++;
            if (maxDenials > 0 && denialCount == maxDenials) {
                setState(Constants.FSM_DONE);
            }
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
     * Gets the maximum allowed amount for this payment
     *
     * @return the maximum allowed amount
     */
    public String getMaxAmount() {
        return this.maxAmount;
    } // end getMaxAmount

    /**
     * Sets the maximum allowed amount for this payment
     *
     * @param maxAmount The maximum allowed amount
     */
    public void setMaxAmount(String maxAmount) {
        if (maxAmount != null) {
            this.maxAmount = (String)canonicalize(maxAmount.toUpperCase(), true);
        }
    } // end setMaxAmount

    /**
     * Gets the minimum allowed amount for this payment
     *
     * @return the minimum allowed amount
     */
    public String getMinAmount() {
        return this.minAmount;
    } // end getMinAmount

    /**
     * Sets the minimum allowed amount for this payment
     *
     * @param minAmount The minimum allowed amount
     */
    public void setMinAmount(String minAmount) {
        if (minAmount != null) {
            this.minAmount = (String)canonicalize(minAmount.toUpperCase(), true);
            this.grammars.add(this.minimumDueGrammar);
        }
    } // end setMinAmount
    
    /**
     * Return the account balance
     * 
     * @return Returns the balance
     */
    public String getBalance() {
        return balance;
    }
    
    /**
     * Set the account balance
     * 
     * @param balance The balance to set.
     */
    public void setBalance(String balance) {
        if (balance != null) {
            this.balance = (String)canonicalize(balance.toUpperCase(), true);
            this.grammars.add(this.fullAmountGrammar);
        }
    }

    /**
     * Get the maximum denials allowed before graceful exit
     * 
     * @return Returns the maxDenials.
     */
    public int getMaxDenials() {
        return maxDenials;
    }
    /**
     * Set the maximum denials allowed before graceful exit
     * 
     * @param maxDenials The maxDenials to set.
     */
    public void setMaxDenials(int maxDenials) {
        this.maxDenials = maxDenials;
    }
    
    /**
     * Sets up maximum or minimum permissible value. The format is 
     * UUUmm.nn or mm.nn. If value is in UUUmm.nn format the currency
     * code UUU is ignored and the default currency code or
     * the supplied currency code is assumed.
     * 
     * @param strInput The maximum or minimum amount, 
     *                 either in UUUmm.nn or mm.nn format
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
                        "currency code of \"" + getId()    + "\" and the" +
                        " entered currency code do not match.");
                }
                newValue = inputStr.toUpperCase().substring(3);
            } else {
                newValue = inputStr;
            }
            if (!isAttribute) {
                if ("minimum".equalsIgnoreCase(inputStr)) {
                    inputStr = minAmount;
                } else if ("full".equalsIgnoreCase(inputStr)) {
                    inputStr = balance;
                }
            }
            Double valueDouble = new Double(newValue);
            return df.format(valueDouble.doubleValue()).toUpperCase();
        } catch (IndexOutOfBoundsException e) {
            if (isAttribute) {
                throw new IllegalArgumentException("An amount value \"" +
                inputStr + "\" associated with creditcardAmount RDC " +getId()+
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
                inputStr + "\" associated with creditcardAmount RDC " +getId()+
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
        val = new Double((String) newValue);
        if (maxAmount != null) {
            maxVal = new Double(maxAmount);
            if (val.doubleValue() > maxVal.doubleValue()) {
                if (setErrorCode) setErrorCode(ERR_NEED_LOWER_AMOUNT);
                return Boolean.FALSE;
            }
        }
        if (minAmount != null) {
            minVal = new Double(minAmount);
            if (val.doubleValue() < minVal.doubleValue()) {
                if (setErrorCode) setErrorCode(ERR_NEED_HIGHER_AMOUNT);
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    } // end validate()

}
// *** End of CreditCardPayment.java ***
