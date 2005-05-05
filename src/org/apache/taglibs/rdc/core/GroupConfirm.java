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
package org.apache.taglibs.rdc.core;

import java.util.Map;
import java.util.Iterator;
import java.io.IOException;
import javax.servlet.jsp.PageContext;


/**
 * <p>Helper class for carrying out group confirmation.</p>
 * 
 * @author Sindhu Unnikrishnan
 * @author Rahul Akolkar
 */
public class GroupConfirm {

	// CLASS PROPERTIES
	GroupModel localModel = null;
	private String submit = null;
	private Map localMap = null;
	private PageContext ctx = null;
	private String idConfirm = null;
	private String idIdentify = null;
	private int confirmState;

	/* Constructor */
	public GroupConfirm(PageContext ctx, GroupModel localModel, String id) {
		this.localModel = localModel;
		this.submit = localModel.getSubmit();
		this.localMap = localModel.getLocalMap();
		this.ctx = ctx;
		this.idConfirm = id + "Confirm";
		this.idIdentify = id + "Identify";
		this.confirmState = Constants.CONF_STATE_INPUT;
	}

	/**
	 * Group confirmation state machine
	 * 
	 * @return int groupState
	 */
	public int doGroupConfirmation() {
		if (confirmState == Constants.CONF_STATE_INPUT) {
			populateFieldConfirm();
			return confirmState;
		}

		if (confirmState == Constants.CONF_STATE_CONFIRMATION){
		    compareConfirmationValues();
			return confirmState;
		}

		if (confirmState == Constants.CONF_STATE_IDENTIFICATION){
			compareIdentificationValues();
			return confirmState;
		}

		return Constants.CONF_STATE_UNEXPECTED;
	}

	private void populateFieldConfirm() {

		StringBuffer result = new StringBuffer();
		String current = null;
		BaseModel model = null;

		result.append("<field name = \"" + idConfirm + "\" type = \""
				+ Constants.STR_BOOLEAN + "\">");
		result.append(Constants.VXML_PROMPT_BEGIN + 
			Constants.STR_CONF_PROMPT_START + 
			Constants.STR_CONF_PROMPT_ENUMERATE	+ Constants.VXML_PROMPT_END);
		result.append(Constants.VXML_PROMPT_BEGIN + 
			Constants.STR_CONF_PROMPT_END + Constants.VXML_PROMPT_END);

		Iterator iter = localMap.keySet().iterator();
		while (iter.hasNext()) {
			current = (String) iter.next();
			if (!current.equals(Constants.STR_INIT_ONLY_FLAG)) {
				model = (BaseModel) localMap.get(current);
				result.append(Constants.VXML_OPTION_BEGIN);
				result.append(model.getCanonicalizedValue());
				result.append(Constants.VXML_OPTION_END);
			}
		}
		result.append("<filled> <submit next =\"" + submit + "\" namelist=\""
				+ idConfirm	+ "\" /> </filled></field>");
		try {
			ctx.getOut().write(result.toString());
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		confirmState = Constants.CONF_STATE_CONFIRMATION;
	}

	private void compareConfirmationValues() {

		String testConfirm = ctx.getRequest().getParameter(idConfirm);
		if (testConfirm == null) {
			confirmState = Constants.CONF_STATE_UNEXPECTED;
			return;
		}

		if (confirmState == Constants.CONF_STATE_CONFIRMATION) {
			if (testConfirm.equals(Constants.STR_TRUE)) {
				localModel.setConfirmed(Boolean.TRUE);
				confirmState = Constants.CONF_STATE_DONE;
			} else if (testConfirm.equals(Constants.STR_FALSE)) {
				populateFieldIdentification();
			}
		}

	}

	private void populateFieldIdentification() {
		StringBuffer result = new StringBuffer();
		String current = null;
		BaseModel model = null;

		result.append("<field name = \"" + idIdentify + "\">" + 
			Constants.VXML_PROMPT_BEGIN + Constants.STR_CONF_PROMPT_SELECT + 
			Constants.VXML_PROMPT_END);

		Iterator iter = localMap.keySet().iterator();
		while (iter.hasNext()) {
			current = (String) iter.next();
			if (!current.equals(Constants.STR_INIT_ONLY_FLAG)) {
				model = (BaseModel) localMap.get(current);
				if (model.getConfirmed() == Boolean.FALSE) {
					result.append(Constants.VXML_OPTION_BEGIN).append(current).
						append(Constants.VXML_OPTION_END);
				}
			}
		}

		result.append("<filled> <submit next =\"" + submit + "\" namelist=\""
				+ idIdentify + "\" /></filled></field>");
		try {
			ctx.getOut().write(result.toString());
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		confirmState = Constants.CONF_STATE_IDENTIFICATION;

	}

	private void compareIdentificationValues() {

		String testIdentify = ctx.getRequest().getParameter(idIdentify);
		if (testIdentify == null) {
			confirmState = Constants.CONF_STATE_UNEXPECTED;
			return;
		}
		BaseModel model = null;
		StringBuffer result = new StringBuffer();
		Iterator iter = localMap.keySet().iterator();
		String current = null;
		while (iter.hasNext()) {
			current = (String) iter.next();
			if (current.equals(Constants.STR_INIT_ONLY_FLAG)) {
				continue;
			}
			model = (BaseModel) localMap.get(current);
			if (confirmState == Constants.CONF_STATE_IDENTIFICATION) {
				if (testIdentify.equals(current)) {
					model.setConfirmed(Boolean.FALSE);
					model.setConfirm(Boolean.TRUE);
					model.setState(Constants.FSM_INPUT);
					model.setSkipSubmit(Boolean.FALSE);
					result.append(Constants.VXML_BLOCK_BEGIN + "OK, lets try " +
						model.getId() + " again.");
					result.append("<submit next =\"" + submit + "\" />");
					result.append(Constants.VXML_BLOCK_END);
				}
			}
		}
		try {
			ctx.getOut().write(result.toString());
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		localModel.setState(Constants.GRP_STATE_RUNNING);
		confirmState = Constants.CONF_STATE_INPUT;
	}
}