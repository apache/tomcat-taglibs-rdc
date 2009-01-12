/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.taglibs.rdc.sampleapps.mortgage.struts;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.taglibs.rdc.core.StrutsSubmitTag;

/**
 * Struts form bean for mortgage-rate.jsp (from mortgage sample app)
 *
 * @author Rahul
 *
 */
public class ProceedBean extends ActionForm {

    // Serial Version UID
    private static final long serialVersionUID = 1L;
    // User's indication where the mortgage quote is acceptable
    private Boolean mortgageOK = null;
    // The property value
    private String mlsPropertyValue = null;
    // The down payment amount to be transferred to the escrow account
    private String transferAmount = null;
    // The URI where the banking transaction app will submit its results back
    private String submitURI = null;

    /**
     * Gets the user's response to the mortgage quote
     *
     * @return Boolean the user's response to the mortgage quote
     */
    public Boolean getMortgageOK() {
        return mortgageOK;
    }

    /**
     * Sets the user's response to the mortgage quote
     *
     * @param b The new response to the mortgage quote
     */
    public void setMortgageOK(Boolean b) {
        this.mortgageOK = b;
    }

    /**
     * Gets the property value (for the MLS number supplied earlier)
     *
     * @return String the property value
     */
    public String getMlsPropertyValue() {
        return mlsPropertyValue;
    }

    /**
     * Sets the property value
     *
     * @param string The new property value
     */
    public void setMlsPropertyValue(String string) {
        this.mlsPropertyValue = string;
    }

    /**
     * Gets the submit URI
     *
     * @return String the submit URI
     */
    public String getSubmitURI() {
        return submitURI;
    }

    /**
     * Sets the submit URI
     *
     * @param string The new submit URI
     */
    public void setSubmitURI(String string) {
        this.submitURI = string;
    }

    /**
     * Gets the transfer amount
     *
     * @return String the amount to be transferred
     */
    public String getTransferAmount() {
        return transferAmount;
    }

    /**
     * Sets the transfer amount
     *
     * @param string The new transfer amount
     */
    public void setTransferAmount(String string) {
        this.transferAmount = string;
    }

    /**
     * Reset all bean properties to their default state.
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {

        mortgageOK = null;
        mlsPropertyValue = null;
        submitURI = null;
        transferAmount = null;

    }

    /**
     * Validate the properties that have been set for this HTTP request,
     * and return an <code>ActionErrors</code> object that encapsulates
     * any validation errors that have been found.
     */
    public ActionErrors validate(
        ActionMapping mapping,
        HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();
        StrutsSubmitTag.populate(this, request, errors);

        return errors;

    }

}
