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
 */

package org.apache.taglibs.rdc;

import java.io.Serializable;
import java.util.ArrayList;
import org.apache.taglibs.rdc.core.BaseModel;
import org.w3c.dom.Document;

/**
 *
 * Datamodel for list RDC. The SelectOne RDC will be associated with 
 * the list input. From a list of options the user selects one option .
 *
 * @author Tanveer Faruquie
 * @author Rahul Akolkar
 */
public class SelectOne extends BaseModel {
	// The SelectOne RDC will be associated with the list input. From
	// a list of options the user selects one option

	// The optionList attribute
	private Object optionList;
	// The list of available options for selection
	private Object options;
	// org.w3c.dom.Document for static list and Options for a dynamic one
	private String optionsClass;

	/**
	 * Sets default values for all data members
	 * 
	 */
	public SelectOne() {
		super();
		this.optionList = null;
		this.options = null;
		// assume static list to start off
		this.optionsClass = org.w3c.dom.Document.class.getName();
	} // end SelectOne Constructor

	/**
	 * Gets the options list. This list has all the options from which
	 * the user selects a value.
	 * 
	 * @return options list.
	 */
	public Object getOptions() {
		return this.options;
	} // end getOptions()

	/**
	 * Sets the options list. This list has all the options from which
	 * the user selects a value
	 * 
	 * @param options The options list.
	 */
	public void setOptions(Object options) {
		this.options = options;
	} // end setOptions()

	/**
	 * Gets the Options object. This contains all the options from which
	 * the user selects a value.
	 * 
	 * @return Options
	 */
	public Object getOptionList() {
		return optionList;
	} // end getOptionList()

	/**
	 * Sets the Options object. This contains all the options from which
	 * the user selects a value.
	 * 
	 * @param Options
	 */
	public void setOptionList(Object optionList) {
		this.optionList = optionList;
		if (optionList instanceof Options) {
			options = ((Options) optionList).getVXMLOptionsMarkup();
			optionsClass = Options.class.getName();
			if (((String) options).length() == 0) {
				throw new IllegalArgumentException("SelectOne " + id +
					" cannot be used with a empty optionList.");
			}
		}
	} // end setOptionList()
	
	/**
	 * Get the class name of the options for this instance
	 * 
	 * @return String class name as a String
	 */
	public String getOptionsClass() {
		return optionsClass;
	}

	/**
	 * Set the class name of the options for this instance
	 * 
	 * @param String class name as a String
	 */
	public void setOptionsClass(String string) {
		optionsClass = string;
	}
	
	/**
	 * Encapsulates a set of options.
	 * Each option should include an utterance, and an optional value.
	 *
	 */	
	public static class Options implements Serializable {
		
		private ArrayList values;
		private ArrayList utterances;
		
		/**
		 * Constructor
		 *
		 */	
		public Options() {
			values = new ArrayList();
			utterances = new ArrayList();
		}

		/**
		 * Add this option to the list. Option contains an utterance
		 * and an optional value.
		 *
		 */		
		public void add(String option_value, String option_utterance) {
			values.add(option_value);
			utterances.add(option_utterance);
		}

		/**
		 * Generate the markup of the vxml &lt;option&gt; elements as a String
		 *
		 * @return String the VXML markup
		 */		
		public String getVXMLOptionsMarkup() {
			String options = "";
			for (int i=0; i < utterances.size(); i++) {
				String val = (String) values.get(i);
				String utt = (String) utterances.get(i);
				if (utt != null && utt.trim().length() > 0) {
					if (val == null || val.trim().length() == 0) {
						options += "<option>" + utt + "</option>";
					} else {
						options += "<option value=\"" + val.trim() + "\">" + 
							utt + "</option>";
					}
				}
			}
			return options;
		}

	} // end class Options{}

} // end class SelectOne{}

// *** End of SelectOne.java ***
