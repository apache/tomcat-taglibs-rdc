package org.apache.taglibs.rdc.struts;

import java.util.Enumeration;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;

/**
 * Struts form bean for mortgage-rate.jsp (from mortgage sample app)
 * 
 * @author Rahul
 *
 */
public class ProceedBean extends ActionForm {

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
	 * @param String the new response to the mortgage quote
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
	 * @param String the new property value
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
	 * @param String the new submit URI
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
	 * @param String the new transfer amount
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
		HashMap viewsMap = (HashMap)request.getSession().getAttribute("viewsMap");
		HashMap formData = (HashMap)viewsMap.get(request.getAttribute("key"));

		try {
			BeanUtils.populate(this, formData);
		} catch (IllegalAccessException iae) {
			iae.printStackTrace();
			errors.add("RDC", new org.apache.struts.action.
				ActionError("IllegalAccessException while populating form bean"));
		} catch (InvocationTargetException ite) {
			ite.printStackTrace();
			errors.add("RDC", new org.apache.struts.action.
				ActionError("InvocationTargetException while populating form bean"));
		}

		return errors;

	}

}
