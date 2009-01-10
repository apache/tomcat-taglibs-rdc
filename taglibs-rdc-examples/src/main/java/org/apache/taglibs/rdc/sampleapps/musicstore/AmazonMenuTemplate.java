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
package org.apache.taglibs.rdc.sampleapps.musicstore;

import java.io.Serializable;

import org.apache.taglibs.rdc.core.BaseModel;
import org.apache.taglibs.rdc.core.Constants;
import org.apache.taglibs.rdc.core.Grammar;
import org.apache.taglibs.rdc.core.RDCTemplate;

/**
 *
 * The RDC template instance for the main menu of the music store
 * sample application. This template instance has:
 * a) A user defined data model
 * b) Multiple turns in "input" state
 * c) "Mixed initiative" style interaction for first turn
 * d) Custom "dialog management" based on validation criteria for 
 *    subsequent turns
 * 
 * @author Rahul Akolkar
 */
public class AmazonMenuTemplate extends RDCTemplate 
    implements BaseModel.ValueInterpreter {

    /**A constant for Error Code stating no category from the menu 
     * has been specified */
    public static final int ERR_NO_CATEGORY = 1;
    public final int getERR_NO_CATEGORY() { return ERR_NO_CATEGORY; }

    // Component Grammars
    private Grammar mainGrammar;
    private Grammar categoryGrammar;
    public void setMainGrammar(Grammar g) {    mainGrammar = g; }
    public void setCategoryGrammar(Grammar g) { categoryGrammar = g; }

    /**
     * Sets default values for all data members
     */
    public AmazonMenuTemplate() {
        super();
        this.value = new AmazonMenuResult();
        this.mainGrammar = null;
        this.categoryGrammar = null;
        this.populateGrammars = true;
    }
    
    /**
     * Sets the new value
     * 
     * @see org.apache.taglibs.rdc.core.BaseModel.ValueInterpreter#setValueFromInterpretation()
     */
    public void setValueFromInterpretation() {
        String genre = (String) interpretation.get(AmazonMenuResult.SI_GENRE);
        String category = (String) interpretation.get(AmazonMenuResult.
            SI_CATEGORY);
        if (genre == null && category == null) {
            value = baseCanonicalize((String)interpretation.get(Constants.
                STR_EMPTY));
        } else {
            if (genre != null && genre.length() > 0) {
                ((AmazonMenuResult) value).setGenre(genre);
            }
            if (category != null && category.length() > 0) {
                ((AmazonMenuResult) value).setCategory(category);
            }
        }
        setIsValid(baseValidate(value, true));        

        setCanonicalizedValue(((AmazonMenuResult) value).getReadableValue());
    }
    
    /**
     * Initialize the component grammars for first round trip
     * 
     * @see RDCTemplate#initComponentGrammars()
     */
    public void initComponentGrammars() {
        // start with main grammar + initial if applicable
        this.grammars.remove(categoryGrammar);
    }
    
    /**
     * Set the response of user to the confirmation dialog
     * 
     * @param confirmed
     */
    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
        if (!confirmed.booleanValue()) {
            if (grammars.contains(categoryGrammar)) {
                grammars.remove(categoryGrammar);
                grammars.add(mainGrammar);
            }
            canonicalizedValue = null;
            value = new AmazonMenuResult();
        }
    }

    /**
     * Custom validation
     * 
     * @see BaseModel#validate(Object,boolean)
     */
    protected Boolean validate(Object newValue, boolean setErrorCode) {

        AmazonMenuResult castValue = (AmazonMenuResult) newValue;
        castValue.setReadableValue((canonicalizedValue == null) ? utterance : 
            (canonicalizedValue + " " + utterance));
        if (castValue.getGenre() != null && castValue.getCategory() == null) {
            if (setErrorCode) setErrorCode(ERR_NO_CATEGORY);
            grammars.remove(mainGrammar);
            grammars.add(categoryGrammar);
            return Boolean.FALSE;
        }
        if (castValue.getCategory() != null && castValue.getGenre() == null) {
            castValue.setGenre(AmazonMenuResult.DEFAULT_GENRE);
        }
        return Boolean.TRUE;
    }
    
    /**
     * Class that encapsulates the user's choice from the main menu.
     * 
     * @author Rahul Akolkar
     */
    public static class AmazonMenuResult implements Serializable {
        public static String SI_GENRE = "genre";
        public static String SI_CATEGORY = "category";
        public static String DEFAULT_GENRE = "301668";
        private String genre;
        private String category;
        private String readableValue;

        public AmazonMenuResult() {
            genre = null;
            category = null;
            readableValue = null;
        }
        public String getCategory() {
            return category;
        }
        public String getGenre() {
            return genre;
        }
        public String getReadableValue() {
            return readableValue;
        }
        public void setCategory(String string) {
            category = string;
        }
        public void setGenre(String string) {
            genre = string;
        }
        public void setReadableValue(String string) {
            readableValue = string;
        }
        public String toString() {
            return readableValue;
        }
    }

}

