package org.apache.taglibs.rdc.struts;

import java.util.Enumeration;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import org.apache.taglibs.rdc.MortgageData;

/**
 * Struts form bean for mortgage.jsp (from mortgage sample app)
 * 
 * @author Sindhu Unnikrishnan
 * @author Rahul
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
