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
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.taglibs.rdc.sampleapps.mortgage.MortgageAppBean;
import org.apache.taglibs.rdc.MortgageData;

/**
 * Struts action class for mortgage data collection (mortgage sample app)
 *
 * @author Rahul
 */
public class MortgageAction extends Action {

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
		MortgageBean formBean = (MortgageBean) form;

		MortgageData mortgage = null;
		String monthlyInstallment = null;
		String propertyValueStr = ((MortgageAppBean)request.getSession().
			getAttribute("appBean")).getPropertyValue();
		double propertyValue = 0;
		int downpayPercent = 0;

		try {

			propertyValue = Double.parseDouble(propertyValueStr.trim());
			mortgage = formBean.getMortgage();
			String downpayPercentStr = mortgage.getPercent();
			downpayPercentStr = downpayPercentStr.replaceAll(" ", "");
			downpayPercent = Integer.parseInt(downpayPercentStr);
			monthlyInstallment = getMortgagePayment(mortgage,
				propertyValue);

		} catch (Exception e) {

			errors.add("MortgageAction", new ActionMessage(e.getClass().getName()));
			e.printStackTrace();
		}

		if (propertyValue == 0) {
			errors.add("MortgageAction", new ActionMessage("Zero propertyValue"));
		}
		if (downpayPercent == 0) {
			errors.add("MortgageAction", new ActionMessage("Zero downpayPercent"));
		}

		double downPayment = propertyValue * downpayPercent / 100;
		// Truncate cents for better TTS result ... won't work if downPayment > 10^7
		String downPaymentStr = "" + downPayment;
		if (downPaymentStr.indexOf('.') != -1) {
			downPaymentStr = downPaymentStr.substring(0,
				downPaymentStr.indexOf('.'));
		}

		// If a message is required, save the specified key(s)
		// into the request for use by the <struts:errors> tag.
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}

		// Write logic determining how the user should be forwarded.
		if ( formBean.getMortgage() != null ){
			((MortgageAppBean)request.getSession().getAttribute("appBean")).
				setDownPayment(downPaymentStr);
			request.setAttribute("monthlyInstallment", monthlyInstallment);
			forward = mapping.findForward("ok");
		} else {
			forward = mapping.findForward("redo");
		}

		// Finish with
		return (forward);

	}

	/**
	 * Get the monthly installment for the given mortgage
	 *
	 * @param mortgage the mortgage data
	 * @param amount the property value
	 * @return String the monthly installment returned as a string
	 */
	private String getMortgagePayment(MortgageData mortgage, double amount) {
		// Access back-end for rate lookup
		return "1279";
	}

}
