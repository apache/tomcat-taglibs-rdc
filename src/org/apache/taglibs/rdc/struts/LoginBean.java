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

import java.util.Enumeration;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.taglib.tiles.GetAttributeTag;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;

/**
 * Struts form bean for login.jsp (from mortgage sample app)
 *
 * @author Sindhu Unnikrishnan
 * @author Rahul
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
		HashMap viewsMap = (HashMap) request.getSession().getAttribute("viewsMap");
		HashMap formData = (HashMap) viewsMap.get(request.getAttribute("key"));

		try {
			BeanUtils.populate(this, formData);
		} catch (IllegalAccessException iae) {
			iae.printStackTrace();
			errors.add("RDC", new org.apache.struts.action.ActionError(
					"IllegalAccessException while populating form bean"));
		} catch (InvocationTargetException ite) {
			ite.printStackTrace();
			errors.add("RDC", new org.apache.struts.action.ActionError(
					"InvocationTargetException while populating form bean"));
		}

		return errors;

	}

}
