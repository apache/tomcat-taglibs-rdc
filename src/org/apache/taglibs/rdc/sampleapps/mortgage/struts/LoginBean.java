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
 * Struts form bean for login.jsp (from mortgage sample app)
 *
 * @author Sindhu Unnikrishnan
 * @author Rahul Akolkar
 *
 */
public class LoginBean extends ActionForm {

    // The member number of the user
    private String memberNumber = null;
    // The multiple listing service number of the property
    private String mlsNumber = null;

    /**
     * Gets the member number
     *
     * @return String the member number
     */
    public String getMemberNumber() {
        return memberNumber;
    }

    /**
     * Sets the member number
     *
     * @param String the new member number
     */
    public void setMemberNumber(String n) {
        this.memberNumber = n;
    }

    /**
     * Gets the multiple listing service number
     *
     * @return String the MLS number
     */
    public String getMlsNumber() {
        return mlsNumber;
    }

    /**
     * Sets the multiple listing service number
     *
     * @param String the new MLS number
     */
    public void setMlsNumber(String n) {
        this.mlsNumber = n;
    }

    /**
     * Reset all bean properties to their default state.
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {

        memberNumber = null;
        mlsNumber = null;

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
