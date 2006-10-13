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
package org.apache.taglibs.rdc.core;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.apache.taglibs.rdc.RDCUtils;
import org.w3c.dom.Document;
/**
 * <p>This is the base class for all RDCs. Each atomic RDC 
 * must extend this class. GroupModel and ComponentModel
 * extend this class.</p>
 * 
 * @author Abhishek Verma
 * @author Rahul Akolkar
 */
public abstract class BaseModel implements Serializable {
    
    // CONSTANTS
    /** The default value of the minimum confidence. Results below this
     *  confidence will result in the associated value being treated
     *  as invalid and the RDC will reprompt for user input. */
    public static final float     DEFAULT_MIN_CONFIDENCE     = 0.4F;
    /** The unique identifier associated with this RDC */
    public static final int     DEFAULT_NUM_N_BEST         = 1;
    /** The name that will be associated with the default grammar
     *  used to refer to the initial value associated with this RDC
     *  @see org.apache.taglibs.rdc.core.Grammar */
    public static final String     DEFAULT_INITIAL_GRAMMAR_NAME = 
        "RDC_DEFAULT_INITIAL_GRAMMAR";    
    /**
     * The property under which the initial grammar is stored in the
     * resource bundles in org.apache.taglibs.rdc.resources
     */
    public static final String PROPERTY_INITIAL_GRAMMAR = 
        "rdc.core.basemodel.initialgrammar";
        
    //Common validation errors
    /**A constant for errorCode when there is no error */
    public static final int ERR_NONE = 635462;
    /**A constant for errorCode stating no default value is specified */
    public static final int ERR_NO_DEFAULT = 635463;
    //Getters for common validation errors
    public final int getERR_NONE(){ return ERR_NONE; }
    public final int getERR_NO_DEFAULT(){ return ERR_NO_DEFAULT; }

    
    // PROPERTIES
    /** The unique identifier associated with this RDC */
    protected String id;
    /** The current state of this RDC */
    protected int state;
    /** This is used to identify the error that occured */
    protected int errorCode;
    /** specifies whether to do confirmation or not */
    protected Boolean confirm;
    /** Response of the user to the confirmation dialog */
    protected Boolean confirmed;
    /** Indicates whether the current value for this RDC is valid
     *  with respect to the supplied constraints */
    protected Boolean isValid;
    /** The utterance of the user; what the user said */
    protected String utterance;
    /** The normalized value for the input of this RDC */
    protected String canonicalizedValue;
    /** The semantic interpretation returned for the input of this RDC */
    protected Map interpretation;
    /** The URI to submit the vxml form */
    protected String submit;
    /** Path to component grammar(s) */
    protected List grammars;
    /** The user preference for playing back the return value associated
     *  with the RDC */
    protected Boolean echo;
    /** Indicates whether this RDC is invoked as a subdialog. This will
     *  affect what happens to the value collected by the RDC */
    protected Boolean subdialog;
    /** Indicates whether the current value for this RDC is ambiguous */
    protected Boolean isAmbiguous;
    /** Contains the list of ambiguous values keyed on grammar conforming values
     *  For e.g., a map of ambiguous values for say, time 5'o clock would be
     *  Key     Value
     *  0500a   5 A M
     *  0500p   5 P M */
    protected Map ambiguousValues;
    /** The default (parsed) configuration */
    protected Document configuration;
    /** The class of the bean that subclasses this instance */
    protected String className;
    /** Specifies whether this RDC should emit a submit URI -
     *  may be removed in later versions of this tag library */
    protected Boolean skipSubmit;
    /** Value currently associated with this RDC */
    protected Object value;
    /** The default/initial value associated with this RDC */
    protected Object initial;
    /** The serialized n-best data from the vxml browser */
    protected String candidates;
    /** Minimum confidence below which all values are treated as invalid */
    protected float minConfidence;
    /** The maximum number of n-best results requested from the vxml browser */
    protected int numNBest;
    /** Map this model's properties to the params in the request it
     *  should look for */
    protected Map paramsMap;
    /** The grammar available for the user to pick default/initial value
     *  associated with this RDC */
    protected Grammar initialGrammar;
    /** The status at exit, indicating whether this RDC collected input 
     *  or gracefully exited after a number of retries */
    protected int exitStatus;
    /** Maximum number of client side &lt;noinput&gt; events before this RDC
     *  gracefully exits with Constants.EXIT_MAXNOINPUT exitStatus
     *  @see Constants#EXIT_MAXNOINPUT */
    protected int maxNoInput;
    /** Maximum number of client side &lt;nomatch&gt; events before this RDC
     *  gracefully exits with Constants.EXIT_MAXNOMATCH exitStatus
     *  @see Constants#EXIT_MAXNOMATCH */
    protected int maxNoMatch;
    /** The Locale for this RDC */
    protected String locale;
    /** The Locale for this RDC, defaults to Constants.rdcLocale
     *  @see Constants#rdcLocale */
    protected transient Locale rdcLocale;
    /** The ResourceBundle for this RDC,defaults to Constants.rdcResourceBundle
     *  @see Constants#rdcResourceBundle */
    protected transient ResourceBundle rdcResourceBundle;
    
    public BaseModel() {
        this.id = null;
        this.errorCode = ERR_NONE;
        this.confirm = Boolean.FALSE;
        this.confirmed = Boolean.FALSE;
        this.isValid = Boolean.FALSE;
        this.subdialog = Boolean.FALSE;
        this.utterance = null;
        this.canonicalizedValue = null;
        this.submit = null;
        this.grammars = new ArrayList();
        this.echo = Boolean.FALSE;
        this.isAmbiguous = Boolean.FALSE;
        this.ambiguousValues = null;
        this.configuration = null;
        this.className = this.getClass().getName();
        this.skipSubmit = Boolean.FALSE;
        this.value = null;
        this.initial = null;
        this.candidates = null;
        this.minConfidence = DEFAULT_MIN_CONFIDENCE;
        this.numNBest = DEFAULT_NUM_N_BEST;
        this.paramsMap = new HashMap();
        this.initialGrammar = null;
        this.exitStatus = Constants.EXIT_UNREACHED;
        this.maxNoInput = 0;
        this.maxNoMatch = 0;
        this.locale = Constants.locale;
        this.rdcLocale = Constants.rdcLocale;
        this.rdcResourceBundle = Constants.rdcResourceBundle;
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
     * Get whether this RDC is invoked as a subdialog
     * 
     * @return subdialog The subdialog Boolean.
     */
    public Boolean getSubdialog() {
        return subdialog;
    }
    
    /**
     * Set whether this RDC is invoked as a subdialog
     * 
     * @param subdialog The subdialog Boolean.
     */
    public void setSubdialog(Boolean subdialog) {
        this.subdialog = subdialog;
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
     * Get the interpretation for this input
     *
     * @return interp the interpretation for this input
     */
    public Map getInterpretation() {
        return interpretation;
    }

    /**
     * Set the interpretation for this input
     *
     * @param interp the interpretation for this input
     */
    public void setInterpretation(Map interp) {
        interpretation = interp;
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
    public List getGrammars() {
        return grammars;
    }    

    /**
     * Add this <code>Grammar</code> object to the list of
     * grammars for this RDC.
     * 
     * @param grammar - the <code>Grammar</code> object
     */
    public void setGrammar(Grammar grammar) {
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
     * Set the value for this RDC instance. Update the isValid and
     * canonicalizedValue properties based on the new value.
     * 
     * @param value The value returned by the client
     */
    public void setValue(Object value) {
        if (value != null) {
            this.value = baseCanonicalize(value);
    
            setIsValid(baseValidate(this.value, true));
            
            if (getIsValid() == Boolean.TRUE) {
                setCanonicalizedValue(calculateCanonicalizedValue(this.value));
            }
        }
    }
    
    /**
     * Gets the initial value
     *
     * @return The default/initial value
     */
    public Object getInitial() {
        return initial;
    }

    /**
     * Sets the initial value for this RDC. 
     * Inheriting RDC beans that override this method must also take
     * responsibility for populating the initial grammar when appropriate.
     * 
     * @param initial The default/initial value
     */
    public void setInitial(Object initial) {
        if (initial != null) {
            this.initial = canonicalize(initial, true);
            if (this.initial != null) {
                // find appropriate place to validate
                if(baseValidate(this.initial, false) == Boolean.TRUE) {
                    populateInitialGrammar();
                }  else {
                    this.initial = null;
                    grammars.remove(initialGrammar);
                }
            }        
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
     * Set the skipSubmit value.
     * An RDC will be asked to refrain from submitting its results
     * if a container or component higher up in the heirarchy takes
     * responsibility for submitting the results.
     * Example: First interaction turn of a mixed initiative dialog.
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
     * Treatment depends on whether this component instance implements
     * the ValueInterpreter interface. 
     * 
     * @param string candidates the serialized n-best results from the vxml browser
     * @see ValueInterpreter interface
     */
    public void setCandidates(String candidates) {
        if (candidates.equals("MAX_NOINPUT")) {
            setExitStatus(Constants.EXIT_MAXNOINPUT);
            return;
        } else if (candidates.equals("MAX_NOMATCH")) {
            setExitStatus(Constants.EXIT_MAXNOMATCH);
            return;
        }
        this.candidates = candidates;
        NBestResults nbRes = new NBestResults();
        nbRes.setNBestResults(candidates);
        int i = 0, numResults = nbRes.getNumNBest();
        setErrorCode(ERR_NONE); // clear error code from previous input
        Object curValue = null;
        do {
            utterance = nbRes.getNthUtterance(i);
            interpretation = nbRes.getNthInterpretation(i);
            if (RDCUtils.implementsInterface(this.getClass(),
                    ValueInterpreter.class)) {
                ((ValueInterpreter) this).setValueFromInterpretation();
            } else {
                curValue = interpretation.get(Constants.STR_EMPTY);
                setValue(curValue);                
            }
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
    public Map getParamsMap() {
        return paramsMap;
    }

    /**
     * Get the exitStatus value
     *
     * @return the exitStatus value
     */
    public int getExitStatus() {
        return exitStatus;
    }

    /**
     * Set the exitStatus value
     *
     * @param exitStatus The new exitStatus value
     */
    public void setExitStatus(int exitStatus) {
        this.exitStatus = exitStatus;
    }
    
    /**
     * Get the maxNoInput value
     * 
     * @return Returns the maxNoInput.
     */
    public int getMaxNoInput() {
        return maxNoInput;
    }
    
    /**
     * Set the maxNoInput value
     * 
     * @param maxNoInput The maxNoInput to set.
     */
    public void setMaxNoInput(int maxNoInput) {
        this.maxNoInput = maxNoInput;
    }
    
    /**
     * Get the maxNoMatch value
     * 
     * @return Returns the maxNoMatch.
     */
    public int getMaxNoMatch() {
        return maxNoMatch;
    }
    
    /**
     * Set the maxNoMatch value
     * 
     * @param maxNoMatch The maxNoMatch to set.
     */
    public void setMaxNoMatch(int maxNoMatch) {
        this.maxNoMatch = maxNoMatch;
    }
    
    /**
     * Set the Locale (String) for this RDC
     * 
     * @param locale The locale (String) to set.
     */
    public void setLocale(String locale) {
        if (RDCUtils.isStringEmpty(locale)) {
            return;
        }
        this.locale = locale;
        Locale newLocale = null;
        /* 
         * Use - as delimiter for StringTokenizer, in line with IETF RFC 3066
         * http://www.ietf.org/rfc/rfc3066.txt
         */
        // No NPE catch since tokens won't be null
        if (locale.indexOf('-') == -1) {
            newLocale = new Locale(locale);
        } else {
            StringTokenizer localeToks = new StringTokenizer(locale, "-");
            String lang = localeToks.nextToken();
            String country = localeToks.nextToken();
            newLocale = localeToks.hasMoreTokens() ? new Locale(lang, 
                country, localeToks.nextToken()) : new Locale(lang, country);
        }
        if (newLocale == null) {
            return;
        }
        this.rdcLocale = newLocale;
        this.rdcResourceBundle = ResourceBundle.getBundle(Constants.
            STR_RDC_RESOURCE_BUNDLE, rdcLocale);
    }
    
    /**
     * Get the Locale (String) for this RDC
     * 
     * @return locale The locale (String)
     */
    public String getLocale() {
        return locale;
    }
    
    /**
     * Returns the Locale for this RDC, if it was set, or the default
     * Locale for this deployment
     * 
     * @return Locale Returns the Locale for this RDC
     */
    public Locale getRdcLocale() {
        // rdcLocale is transient, reclaim if necessary
        if (rdcLocale == null && !RDCUtils.isStringEmpty(locale)) {
            setLocale(locale);
        }
        if (rdcLocale == null) {
            rdcLocale = Constants.rdcLocale;
        }
        return rdcLocale;
    }
        
    /**
     * Return the resourceBundle.
     * 
     * @return Returns the resourceBundle.
     */
    public ResourceBundle getRdcResourceBundle() {
        // rdcResourceBundle is transient, reclaim if necessary
        if (rdcResourceBundle == null && !RDCUtils.isStringEmpty(locale)) {
            setLocale(locale);
        }
        if (rdcResourceBundle == null) {
            rdcResourceBundle = Constants.rdcResourceBundle;
        }
        return rdcResourceBundle;
    }
    
    /**
     * Transforms canonical data from client to its the corresponding value.
     * 
     * @param input the value
     * @return the canonicalized value
     */
    protected Object baseCanonicalize(Object input) {
        if (input instanceof String && 
                "initial".equalsIgnoreCase((String)input)) {
            // user has selected initial value
            return initial;
        }
        return canonicalize(input, false);
    }
    
    /**
     * Validates the input against the given constraints.
     * Inheriting RDC bean must override this method to do any custom
     * validation.
     * 
     * @return TRUE if valid, FALSE otherwise
     */
    protected Boolean baseValidate(Object newValue, boolean setErrorCode) {
        if (errorCode != ERR_NONE) {
            // canonicalization failed, it has also set error code
            return Boolean.FALSE;
        }
        if (newValue == null) {
            // shouldn't be here
            // this will be reached only if the user selects initial value
            // when no such value is available
            if (setErrorCode) setErrorCode(ERR_NO_DEFAULT);
            return Boolean.FALSE;
        }
        return validate(newValue, setErrorCode);
    }
    
    /**
     * Hook for custom canonicalization.
     * Inheriting RDC bean must override this method to do any custom
     * canonicalization.
     * 
     * @param input the value
     * @return the canonicalized value
     */    
    protected Object canonicalize(Object input, boolean isAttribute) {
        return input;    
    }
    
    /**
     * Hook for custom validation.
     * Inheriting RDC bean must override this method to do any custom
     * validation.
     * 
     * @return TRUE if valid, FALSE otherwise
     */
    protected Boolean validate(Object newValue, boolean setErrorCode) {
        return Boolean.TRUE;    
    }

    /**
     * Hook for custom canonicalized value calculation.
     * Inheriting RDC bean must override this method to do any custom
     * calculation of the canonical value.
     * 
     * @return String The canonical value
     */    
    protected String calculateCanonicalizedValue(Object value) {
        // By default, use utterance
        return this.utterance;
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
        MessageFormat initGramFormat = new MessageFormat(rdcResourceBundle.
            getString(PROPERTY_INITIAL_GRAMMAR), rdcLocale);
        Object[] args = { getId() };
        initialGrammar = new Grammar(initGramFormat.format(args),
            Boolean.FALSE, Boolean.TRUE, DEFAULT_INITIAL_GRAMMAR_NAME);
        grammars.add(initialGrammar);
    }
    
    /**
     * Return the serialized value collected by this RDC, used
     * primarily as the value returned when this RDC is invoked by
     * a subdialog (means it goes out as a request parameter).
     */
    public String getSerializedValue() {
        if (this.value == null) {
            // HTTP param value will be empty string
            return "";
        }
        return this.value.toString();
    }
    
    /**
     * Interface used by components to interpret the results sent
     * back from the vxml browser/client.
     * 
     * Since value has type Object and is component specific, 
     * the subclassing RDC should provide the mechanism to convert the
     * av pairs obtained from the serialized vxml interpretation
     * into the Object corresponding to its value.
     * 
     * The subclassing RDC does not require to implement this interface if
     * its value is a String and its receives no named av pairs from client.
     * 
     * @author Rahul Akolkar
     */
    public interface ValueInterpreter {
        
        /**
         * Use the current value of the interpretation property of this
         * instance to determine the current value of the &quot;value&quot;
         * property. This method should also update the isValid and
         * canonicalizedValue properties based on the new value of the
         * &quot;value&quot; property.
         */
        public void setValueFromInterpretation();
        
    }

}
