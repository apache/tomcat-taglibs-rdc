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
package org.apache.taglibs.rdc.sampleapps.mortgage.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.taglibs.rdc.sampleapps.mortgage.MortgageAppBean;

/**
 * Struts action class for user's response to
 * mortgage quote (mortgage sample app)
 *
 * @author Rahul
 *
 */
public class ProceedAction extends Action {

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

		ActionMessages errors = new ActionMessages();
		ActionForward forward = new ActionForward();
		ProceedBean formBean = (ProceedBean) form;

		String downPayment = ((MortgageAppBean)request.getSession().
			getAttribute("appBean")).getDownPayment();

		if (downPayment == null || downPayment.equals("")) {
			errors.add("ProceedAction", new ActionMessage("No down payment value supplied"));
		}

		// If a message is required, save the specified key(s)
		// into the request for use by the <struts:errors> tag.
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}

		// Write logic determining how the user should be forwarded.
		if ( formBean.getMortgageOK().booleanValue() ){
			//This will be returned by the Audium app ... we are skipping one step here
			((MortgageAppBean)request.getSession().getAttribute("appBean")).
				setTransactionNum(" Z as in Zebra six seven one C as in cat ");
			forward = mapping.findForward("ok");
		} else {
			forward = mapping.findForward("bye");
		}

		// Finish with
		return (forward);

	}

}
