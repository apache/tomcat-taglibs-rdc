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

import java.lang.reflect.Field;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.taglibs.rdc.RDCUtils;
/**
 * A basic template using the default atomic FSM, meant to slash component
 * development effort.
 *
 * @author Rahul Akolkar
 */
public class RDCTemplate extends BaseModel {

	/** The instance specific data that is passed in */
	protected Object data;
	/** The fully qualified class name of the model bean for this
	 *  instance of the RDC template */
	protected String bean;
	/** The fragment that will override any or all of the
	 *  default fsm as needed for this instance of the template */
	protected String fsmFragment;
	/** The instance specific constraints on the value to be collected. */
	protected Map constraints;
	/** The instance specific dynamic tag attributes. */
	protected Map dynamicAttributes;
	/** The property that governs whether we will attempt to populate this
	 *  template instance with the grammars that will be passed in */
	protected boolean populateGrammars;

	/**
	 * Sets default values for all data members
	 */
	public RDCTemplate() {
		super();
		this.data = null;
		this.bean = RDCTemplate.class.getName();
		this.fsmFragment = null;
		this.constraints = null;
		this.dynamicAttributes = null;
		this.populateGrammars = false;
	}
		
	/**
	 * Get the data value that was passed in
	 * 
	 * @return data
	 */
	public Object getData() {
		return this.data;
	}

	/**
	 * Sets the data value
	 * 
	 * @param data
	 */
	public void setData(Object data) {
		if (data != null) {
			this.data = data;
		}
	}
	
	/**
	 * Get the data value that was passed in
	 * 
	 * @return string bean
	 */
	public String getBean() {
		return bean;
	}

	/**
	 * Sets the bean value
	 * 
	 * @param string bean
	 */
	public void setBean(String bean) {
		if (bean != null) {
			this.bean = bean;
		}
	}
	
	/**
	 * Get the tag fragment URI overriding the default FSM
	 * defined in &lt;fsm-run&gt;
	 * 
	 * @return fsmTagFragment
	 */
	public String getFsmFragment() {
		return this.fsmFragment;
	}

	/**
	 * Set the tag fragment URI overriding the default FSM
	 * defined in &lt;fsm-run&gt;
	 * 
	 * @param fsmTagFragment
	 */
	public void setFsmFragment(String fsmFragment) {
		if (fsmFragment != null) {
			this.fsmFragment = fsmFragment;
		}
	}

	/**
	 * Get the constraints on the value to be collected. Only
	 * those constraints passed in via the constraints attribute
	 * of this instance of the template tag will be returned.
	 * 
	 * @return constraints
	 */
	public Map getConstraints() {
		return this.constraints;
	}

	/**
	 * Set the constraints on the value to be collected
	 * 
	 * @param constraints
	 */
	public void setConstraints(Map constraints) {
		if (constraints != null) {
			this.constraints = constraints;
			if (this.constraints.size() > 0) {
				populate(this.constraints);
			}
		} 
	}

	/**
	 * Set the dynamic attributes data received from this tag instance
	 * 
	 * @param attrsMap The dynamic attributes name value map
	 */
	public void setDynamicAttributes(Map dynamicAttributes) {
		if (dynamicAttributes != null) {
			this.dynamicAttributes = dynamicAttributes;
			if (this.dynamicAttributes.size() > 0) {
				populate(this.dynamicAttributes);
			}
		}
	}

	/**
	 * Sets the grammar(s). The param can be an array of some combination
	 * of Grammar objects and string URIs or a Grammar object or a string URI.
	 * 
	 * @param grammar
	 */
	public void setGrammar(Object grammar) {
		Map grammarMap = new HashMap();
		if (grammar != null) {
			if (grammar instanceof Object[]) {
				Object[] grammarsArray = (Object []) grammar;
				for (int i=0; i < grammarsArray.length; i++) {
					Object grammarObj = grammarsArray[i];
					if (grammarObj instanceof Grammar) {
						addGrammar((Grammar)grammarObj, grammarMap);
					} else if (grammarObj instanceof String) {
						// Assume voice and external (not inline)
						addGrammar(new Grammar((String)grammarObj,
							Boolean.FALSE, Boolean.FALSE, null), null);
					}
				}
			} else if (grammar instanceof Grammar) {
				addGrammar((Grammar)grammar, grammarMap);
			} else if (grammar instanceof String) {
				// Assume voice and external (not inline)
				addGrammar(new Grammar((String)grammar, Boolean.FALSE,
					Boolean.FALSE, null), null);
			}			
		}
		if (populateGrammars && grammarMap.size() > 0) {
			populate(grammarMap);
		}
		initComponentGrammars();
	}

	/**
	 * Hook for instances to prepare grammars for first round trip
	 */
	public void initComponentGrammars() {
		// hook for subclasses
	}
	
	/**
	 *  Add grammar object
	 */
	private void addGrammar(Grammar grammar, Map grammarMap) {
		this.grammars.add(grammar);
		if (populateGrammars && grammarMap != null) {
			String name = grammar.getName();
			// We cannot populate anonymous grammars
			if (!RDCUtils.isStringEmpty(name)) {
				grammarMap.put(name, grammar);
			}
		}		
	}

	/**
	 *  Populate this bean with supplied property value pairs
	 */	
	private void populate(Map props2values) {
		Map safeProps = new HashMap(props2values);
		Set badProps = props2values.keySet();
		badProps.retainAll(illegalProperties);
		if (badProps.size() > 0) {
			// tsk tsk ... how could you do that
			Object[] badPropsArr = badProps.toArray(); 
			String badPropsStr = "";
			for (int i = 0; i < badPropsArr.length; i++) {
				String badProp = (String)badPropsArr[i];
				safeProps.remove(badProp);
				badPropsStr += "\"" + badProp + "\", ";
			}
			(new Exception("WARNING: Did not populate following property(ies)"+
			" for RDC template instance with id "+getId()+ " : " +badPropsStr +  
			" as properties defined in BaseModel should not be populated via" +
			" the grammars/constraints/dynamic attributes. Rename these.")).
			printStackTrace();
		}
		try {
			BeanUtils.populate(this, safeProps);
		} catch (Exception e) {
			e.printStackTrace(); //ignore
		}
	}
	
	/** Ensure that the grammars and constraints population mechanism is
	 *  not misused */
	private static final Set illegalProperties = new HashSet();
	/*
	 *  Strictly illegal properties are those defined in BaseModel. Beyond
	 *  that, we are willing to trust the user ;-)
	 */	
	static {
		Field[] fields = BaseModel.class.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			String fieldName = fields[i].getName();
			illegalProperties.add(fieldName);
		}
	}

}