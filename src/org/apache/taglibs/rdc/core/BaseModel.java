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

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.lang.reflect.Method;
import org.w3c.dom.Document;

/**
 * <p>This is the base class for all RDCs. Each atomic RDC 
 * must extend this class. GroupModel and ComponentModel
 * extend this class.</p>
 * 
 * @author Abhishek Verma
 * @author Rahul Akolkar
 */
public class BaseModel implements Serializable {
	
	// CONSTANTS
	public static final float 	DEFAULT_MIN_CONFIDENCE 	= 40.0F;
	public static final int 	DEFAULT_NUM_N_BEST 		= 1;
	
	// The unique identifier associated with this RDC
	protected String id;
	// The current state of this RDC
	protected int state;
	// This is used to identify the error that occured
	protected int errorCode;
	// specifies whether to do confirmation or not
	protected Boolean confirm;
	// Response of the user to the confirmation dialog
	protected Boolean confirmed;
	// Indicates whether the current value for this RDC is valid
	protected Boolean isValid;
	// The utterance of the user; what the user said
	protected String utterance;
	// The normalized value for the input of this RDC
	protected String canonicalizedValue;
	// The URI to submit the vxml form
	protected String submit;
	// Grammar (source string or inline string) being added
	protected String grammar;
	// Path to component grammar(s)
	protected ArrayList grammars;
	// The user preference for playing back the return value associated
	// with the RDC
	protected Boolean echo;
	// Indicates whether the current value for this RDC is ambiguous
	protected Boolean isAmbiguous;
	// Contains the list of ambiguous values keyed on grammar conforming values
	// For e.g., a map of ambiguous values for say, time 5'o clock would be
	// Key	Value
	// 0500a	5 A M
	// 0500p	5 P M
	protected Map ambiguousValues;
	// The default (parsed) configuration 
	protected Document configuration;
	// The class of the bean that subclasses this instance
	protected String className;
	// Specifies whether this RDC should emit a submit URI - to be removed soon
	protected Boolean skipSubmit;
	// Value currently associated with this RDC
	protected Object value;
	// The serialized n-best data from the vxml browser
	protected String candidates;
	// Minimum confidence below which all values are treated as invalid
	protected float minConfidence;
	// The maximum number of n-best results requested from the vxml browser
	protected int numNBest;
	// HashMap mapping this model's properties to the params in the request it
	// should look for
	protected HashMap paramsMap;

	public BaseModel() {
		this.id = null;
		this.errorCode = 0;
		this.confirm = Boolean.FALSE;
		this.confirmed = Boolean.FALSE;
		this.isValid = Boolean.FALSE;
		this.utterance = null;
		this.canonicalizedValue = null;
		this.submit = null;
		this.grammar = null;
		this.grammars = new ArrayList();
		this.echo = Boolean.FALSE;
		this.isAmbiguous = Boolean.FALSE;
		this.ambiguousValues = null;
		this.configuration = null;
		this.className = this.getClass().getName();
		this.skipSubmit = Boolean.FALSE;
		this.value = null;
		this.candidates = null;
		this.minConfidence = DEFAULT_MIN_CONFIDENCE;
		this.numNBest = DEFAULT_NUM_N_BEST;
		this.paramsMap = new HashMap();
	} // BaseModel constructor

	/**
	 * Get the id value for this RDC
	 *
	 * @return the id value
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the id value for this RDC
	 *
	 * @param id The new id value.
	 */
	public void setId(String id) {
		if (id.equals(Constants.STR_INIT_ONLY_FLAG)) {
			throw new NullPointerException(Constants.STR_INIT_ONLY_FLAG + 
				" is a reserved key word and cannot be an id.");
		}
		this.id = id;
		populateParamsMap();
		populateInitialGrammar();
	}

	/**
	 * Get the state value
	 *
	 * @return the state value
	 */
	public int getState() {
		return state;
	}

	/**
	 * Set the state value
	 *
	 * @param state The new state value
	 */
	public void setState(int state) {
		this.state = state;
	}

	/**
	 * Get the errorCode value 
	 * 
	 * @return the errorCode value
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * Set the errorCode value
	 * 
	 * @param errorCode The new errorCode value
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * Get the confirm value
	 *
	 * @return the confirm value
	 */
	public Boolean getConfirm() {
		return confirm;
	}

	/**
	 * Set the confirm value.
	 *
	 * @param confirm The new confirm value
	 */
	public void setConfirm(Boolean confirm) {
		if (confirm != null) {
			this.confirm = confirm;
		}
	}

	/**
	 * Get the response of user to the confirmation dialog
	 *
	 * @return the response to confirmatiom dialog
	 * True - the user said 'yes'
	 * False - the user said 'no'
	 * null - the confirmation dialog hasnt taken place yet 
	 */
	public Boolean getConfirmed() {
		return confirmed;
	}

	/**
	 * Set the response of user to the confirmation dialog
	 *
	 * @param confirmed is new response to confirmation dialog
	 */
	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}

	/**
	 * Get the whether input is valid or not
	 *
	 * @return the whether the input is valid or not true is valid.
	 * True - the input is valid
	 * False - the input is invalid
	 * null - the validation hasnt taken place 
	 */
	public Boolean getIsValid() {
		return isValid;
	}

	/**
	 * Set whether value is valid or not
	 *
	 * @param isValid is new value to indicate whether the input is valid or not
	 */
	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

	/**
	 * Get whether the input value is ambiguous or not
	 * 
	 * @return whether the input value is ambiguous or not
	 * True - the input value is ambiguous
	 * False, null - the input value is not ambiguous
	 */
	public Boolean getIsAmbiguous() {
		return isAmbiguous;
	}

	/**
	 * Set whether the input value is ambiguous or not
	 * 
	 * @param isAmbiguous is the new value to indicate whether the input is ambiguous or not
	 */
	public void setIsAmbiguous(Boolean isAmbiguous) {
		this.isAmbiguous = isAmbiguous;
	}

	/**
	 * Get what the user said; the utterance of the user
	 *
	 * @return the utterance of the user
	 */
	public String getUtterance() {
		return utterance;
	}

	/**
	 * Set what the user said
	 *
	 * @param utterance is the new utterance of the user
	 */
	public void setUtterance(String utterance) {
		this.utterance = utterance;
	}

	/**
	 * Get the normalized value for the input
	 *
	 * @return the normalized value of the input
	 */
	public String getCanonicalizedValue() {
		return canonicalizedValue;
	}

	/**
	 * Set the normalized value for input
	 *
	 * @param canonicalizedValue is the normalized value of the input
	 */
	public void setCanonicalizedValue(String canonicalizedValue) {
		this.canonicalizedValue = canonicalizedValue;
	}

	/**
	 * Get the submit URI for the RDC
	 *
	 * @return the submit URI
	 */
	public String getSubmit() {
		return submit;
	}

	/**
	 * Set the submit URI for the RDC
	 *
	 * @param submit - the submit URI
	 */
	public void setSubmit(String submit) {
		this.submit = submit;
	}
	
	/**
	 * Get the grammar path(s) for the RDC
	 * grammars is a read only property
	 * 
	 * @param grammars - the grammar path(s)
	 */
	public ArrayList getGrammars() {
		return grammars;
	}	

	/**
	 * Set the grammar path for the RDC
	 * grammar is a write only property
	 *
	 * @param grammar - the grammar path
	 */
	public void setGrammar(String grammar) {
		this.grammars.add(grammar);
	}

	/**
	 * Get whether the user has requested echoing the return value
	 * 
	 * @return whether user has requested echo
	 * True - echo has been requested
	 * False, null - echo has not been requested
	*/
	public Boolean getEcho() {
		return echo;
	}

	/**
	 * Set whether the user has requested echo
	 * 
	 * @param isEcho - the user's choice to echo the return value
	 */
	public void setEcho(Boolean echo) {
		if (echo != null) {
			this.echo = echo;
		}
	}

	/**
	 * Get the list of ambiguous values
	 * 
	 * @return the map containing the list of ambiguous values
	 */
	public Map getAmbiguousValues() {
		return this.ambiguousValues;
	}

	/**
	 * Set the map of ambiguous values
	 * 
	 * @param ambiguousValues is a map containing the ambiguous values
	 */
	public void setAmbiguousValues(Map ambigValues) {
		this.ambiguousValues = ambigValues;
	}

	/**
	 * Get the Configuration value.
	 * @return the Configuration value.
	 */
	public Document getConfiguration() {
		return configuration;
	}

	/**
	 * Set the Configuration value.
	 * @param newConfiguration The new Configuration value.
	 */
	public void setConfiguration(Document newConfiguration) {
		this.configuration = newConfiguration;
	}
	
	/**
	 * The bean subclassing this instance.
	 * Read only property.
	 * 
	 * @return className
	 */
	public String getClassName() {
		return className;
	}
	
	/**
	 * Get the value currently associated with the RDC
	 * 
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Dynamically compute the RDC bean which this object belongs to
	 * and call the corresponding setter method.
	 * 
	 * Inheriting RDC bean must override this method.
	 * 
	 * (Unless all values as valid)
	 * 
	 * @param object
	 * - Rahul
	 */
	public void setValue(Object object) {
		Object[] argsArr = { object	};
		Class[] argsClassArray = { object.getClass() };
		Method valueSetter = null;
		try {
			valueSetter = this.getClass().getMethod("setValue", argsClassArray);
			valueSetter.invoke(this, argsArr);
		} catch (NoSuchMethodException nsme) {
			this.value = object;
			this.isValid = Boolean.TRUE;
		} catch (Exception e) {
			this.value = object;
			this.isValid = Boolean.TRUE;
			e.printStackTrace();
		}
	}

	/**
	 * Get the skipSubmit value
	 * 
	 * @return skipSubmit
	 */
	public Boolean getSkipSubmit() {
		return skipSubmit;
	}

	/**
	 * Is this RDC in a mixed initiative dialog?
	 * 
	 * @param newSkipSubmit
	 */
	public void setSkipSubmit(Boolean newSkipSubmit) {
		this.skipSubmit = newSkipSubmit;
	}

	/**
	 * Get the minConfidence
	 * 
	 * @return minConfidence the current value
	 */
	public float getMinConfidence() {
		return minConfidence;
	}

	/**
	 * Set the minConfidence
	 * 
	 * @param minConfidence the new value
	 */
	public void setMinConfidence(float minConfidence) {
		if (minConfidence > 0.0F) {
			this.minConfidence = minConfidence;
		}
	}

	/**
	 * Get the n-best data string
	 * 
	 * @return the serialized n-best data
	 */
	public String getCandidates() {
		return candidates;
	}

	/**
	 * Set the candidates (serialized n-best data string). 
	 * 
	 * Since value has type Object, the subclassing RDC should convert the
	 * name value pairs obtained from the serialized vxml interpretation
	 * into the object corresponding to its value.
	 * 
	 * The subclassing RDC must have a method of the following
	 * signature:
	 * 
	 * Object getValueFromMap(HashMap attrValueMap)
	 * 
	 * (Unless value is a String)
	 * 
	 * @param string candidates the serialized n-best results from the vxml browser
	 * - Rahul
	 */
	public void setCandidates(String candidates) {
		this.candidates = candidates;
		NBestResults nbRes = new NBestResults();
		nbRes.setNBestResults(candidates);
		int i = 0, numResults = nbRes.getNumNBest();
		Object curValue = null;
		do {
			HashMap interp = nbRes.getNthInterpretation(i);
			Object[] argsArr = { interp	};
			Class[] argsClassArray = { interp.getClass() };
			Method nBestSetter = null;
			try {
				nBestSetter = this.getClass().getMethod("getValueFromMap", argsClassArray);
				curValue = nBestSetter.invoke(this, argsArr);
			} catch (NoSuchMethodException nsme) {
				curValue = interp.get(Constants.STR_EMPTY);
			} catch (Exception e) {
				curValue = interp.get(Constants.STR_EMPTY);
				e.printStackTrace();
			}			
			setValue(curValue);
			utterance = nbRes.getNthUtterance(i);
		} while (!isValid.booleanValue() && ++i < numResults &&  
				 nbRes.getNthConfidence(i) > minConfidence);

	}

	/**
	 * Get numNBest
	 * 
	 * @return the maximum number of n-best values that the browser 
	 * will be asked to return
	 */
	public int getNumNBest() {
		return numNBest;
	}

	/**
	 * Set numNBest
	 * 
	 * @param numNBest the new maximum number of n-best values requested
	 */
	public void setNumNBest(int numNBest) {
		if (numNBest > 0) {
			this.numNBest = numNBest;
		}
	}

	/**
	 * Get paramsMap - read-only property, no setter
	 * 
	 * @return paramsMap
	 */
	public HashMap getParamsMap() {
		return paramsMap;
	}

	/**
	 * Create the request parameter and bean property mapping
	 *
	 */
	private void populateParamsMap() {
		paramsMap.put(getId() + "ResultNBest", "candidates");
		paramsMap.put(getId() + "Confirm", "confirmed");
	}

	/**
	 * Feed in the initial grammar
	 *
	 */
	private void populateInitialGrammar() {
		grammars.add("<grammar xml:lang=\"en-US\" version=\"1.0\"" +
			" root=\"" + getId() + "Initial\">\n\t<rule id=\"" + getId() + 
			"Initial\" >\n\t\t<one-of>\n\t\t\t" +
			"<item> initial <tag><![CDATA[$ = \"initial\";]]></tag></item>\n\t\t\t" +
			"<item> default <tag><![CDATA[$ = \"initial\";]]></tag></item>\n\t\t" +
			"</one-of>\n\t</rule>\n</grammar>");
	}
}
