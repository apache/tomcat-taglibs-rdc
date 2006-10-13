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
import org.apache.taglibs.rdc.MortgageData;
import org.apache.taglibs.rdc.core.StrutsSubmitTag;

/**
 * Struts form bean for mortgage.jsp (from mortgage sample app)
 *
 * @author Sindhu Unnikrishnan
 * @author Rahul Akolkar
 *
 */
public class MortgageBean extends ActionForm {

    // The MortgageData supplied by the user
    private MortgageData mortgage = null;

    /**
     * Get mortgage
     * @return <code>MortgageData</code> the mortgage information
     */
    public MortgageData getMortgage() {
        return mortgage;
    }

    /**
     * Set mortgage
     * @param <code>MortgageData</code> the mortgage information
     */
    public void setMortgage(MortgageData mortgage) {
        this.mortgage  = mortgage;
    }

    /**
     * Reset all bean properties to their default state.
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {

        mortgage = null;

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
