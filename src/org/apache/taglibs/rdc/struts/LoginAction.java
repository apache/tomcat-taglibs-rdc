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

package org.apache.taglibs.rdc.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.taglibs.rdc.struts.LoginBean;
import org.apache.taglibs.rdc.struts.MortgageAppBean;

/**
 * Struts action class for login (mortgage sample app)
 *
 * @author Rahul
 *
 */
public class LoginAction extends Action {

	/**
	 * Process the specified HTTP request, and create the corresponding
	 * HTTP response (or forward to another web component that will create it),
	 * with provision for handling exceptions thrown by the business logic.
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		ActionErrors errors = new ActionErrors();
		ActionForward forward = new ActionForward();
		LoginBean formBean = (LoginBean) form;

		boolean loginSuccess = false;
		String mlsPropertyValue = null;
		try {

			// Plug for back-end access to login validation
			// Add appropriate arguments
			loginSuccess = validateLogin(formBean.getMemberNumber());
			if ( loginSuccess ) {
				mlsPropertyValue = getMLSPropertyValue(formBean.getMlsNumber());
			}

		} catch (Exception e) {

			errors.add("LoginAction", new ActionMessage(e.getClass().getName()));

		}

		// If a message is required, save the specified key(s)
		// into the request for use by the <struts:errors> tag.
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}

		// Write logic determining how the user should be forwarded.
		if ( loginSuccess ) {
			((MortgageAppBean)request.getSession().getAttribute("appBean")).
				setMemberNumber(formBean.getMemberNumber());
			((MortgageAppBean)request.getSession().getAttribute("appBean")).
				setMlsNumber(formBean.getMlsNumber());
			((MortgageAppBean)request.getSession().getAttribute("appBean")).
				setPropertyValue(mlsPropertyValue);
			request.setAttribute("mlsNumber", formBean.getMlsNumber());
			request.setAttribute("mlsPropertyValue", mlsPropertyValue);
			forward = mapping.findForward("mortgage");
		} else {
			forward = mapping.findForward("re-login");
		}

		// Finish with
		return (forward);

	}

	/**
	 * Validate the memberNumber supplied
	 *
	 * @param memberNum the member number
	 * @return boolean indicating login success of failure
	 */
	private boolean validateLogin(String memberNum) {
		// Access back-end for login validation, return false if login fails
		return true;
	}

	/**
	 * Obtain the property value for the property with the given
	 * MLS number
	 *
	 * @param mlsNum the M.L.S. number for the property
	 * @return String the property value returned as a string
	 */
	private String getMLSPropertyValue(String mlsNum) {
		// Access back-end for
		return "250000";
	}

}
