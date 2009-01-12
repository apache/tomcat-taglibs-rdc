/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.taglibs.rdc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.taglibs.rdc.core.BaseModel;

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

    // Serial Version UID
    private static final long serialVersionUID = 1L;
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
     * @param optionList The list of options
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
     * @param string The class name as a string
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

        // Serial Version UID
        private static final long serialVersionUID = 1L;

        // "accept" attribute values
        public static final String ACCEPT_EXACT = "exact";
        public static final String ACCEPT_APPROX = "approximate";

        // Default attribute values
        private static final String STR_EMPTY = "";

        // Instance variables
        private List values;
        private List utterances;
        private List dtmfs;
        private List accepts;
        
        /**
         * Constructor
         *
         */    
        public Options() {
            values = new ArrayList();
            utterances = new ArrayList();
            dtmfs = new ArrayList();
            accepts = new ArrayList();
        }

        /**
         * Add this option to the list. Option contains an utterance
         * and an optional value, dtmf and accept.
         *
         * @param option_utterance The utterance for this option
         */
        public void add(String option_utterance) {
            add(STR_EMPTY, option_utterance, STR_EMPTY, false); 
        }

        /**
         * Add this option to the list. Option contains an utterance
         * and an optional value, dtmf and accept.
         *
         * @param option_utterance The utterance for this option
         * @param option_approximate Whether an exact or approximate match is
         *                           sought
         */
        public void add(String option_utterance, boolean option_approximate) {
            add(STR_EMPTY, option_utterance, STR_EMPTY, option_approximate);
        }

        /**
         * Add this option to the list. Option contains an utterance
         * and an optional value, dtmf and accept.
         *
         * @param option_value The value for this option
         * @param option_utterance The utterance for this option
         */
        public void add(String option_value, String option_utterance) {
            add(option_value, option_utterance, STR_EMPTY, false);
        }

        /**
         * Add this option to the list. Option contains an utterance
         * and an optional value, dtmf and accept.
         *
         * @param option_value The value for this option
         * @param option_utterance The utterance for this option
         * @param option_approximate Whether an exact or approximate match is
         *                           sought
         */
        public void add(String option_value, String option_utterance, 
                boolean option_approximate) {
            add(option_value, option_utterance, STR_EMPTY, option_approximate);
        }

        /**
         * Add this option to the list. Option contains an utterance
         * and an optional value, dtmf and accept.
         *
         * @param option_value The value for this option
         * @param option_utterance The utterance for this option
         * @param option_dtmf The DTMF value for this option
         */
        public void add(String option_value, String option_utterance,
                String option_dtmf) {
            add(option_value, option_utterance, option_dtmf, false);
        }

        /**
         * Add this option to the list. Option contains an utterance
         * and an optional value, dtmf and accept.
         *
         * @param option_value The value for this option
         * @param option_utterance The utterance for this option
         * @param option_dtmf The DTMF value for this option
         * @param option_approximate Whether an exact or approximate match is
         *                           sought
         */
        public void add(String option_value, String option_utterance, 
                String option_dtmf, boolean option_approximate) {
            values.add(option_value);
            utterances.add(option_utterance);
            dtmfs.add(option_dtmf);
            if (option_approximate == true) {
                accepts.add(ACCEPT_APPROX);
            } else {
                accepts.add(ACCEPT_EXACT);
            }
        }

        /**
         * Generate the markup of the vxml &lt;option&gt; elements as a String
         *
         * @return String the VXML markup
         */        
        public String getVXMLOptionsMarkup() {
            StringBuffer buf = new StringBuffer();
            for (int i=0; i < utterances.size(); i++) {
                String val = (String) values.get(i);
                String utt = (String) utterances.get(i);
                String dtm = (String) dtmfs.get(i);
                String acc = (String) accepts.get(i);
                if (!RDCUtils.isStringEmpty(utt)) {
                    buf.append("<option");
                    if (!RDCUtils.isStringEmpty(val)){
                        buf.append(" value=\"").append(val.trim()).append("\"");
                    }
                    if (!RDCUtils.isStringEmpty(dtm)) {
                        buf.append(" dtmf=\"").append(dtm.trim()).append("\"");
                    }
                    if (ACCEPT_APPROX.equals(acc)) {
                        buf.append(" accept=\"").append(acc).append("\"");
                    }
                    buf.append(">").append(utt.trim()).append("</option>");
                }
            }
            return buf.toString();
        }

    } // end class Options{}

} // end class SelectOne{}

// *** End of SelectOne.java ***
