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

/**
 * <p>A grammar object containing the inline grammar or URI reference
 * along with the grammar metadata.</p>
 * <p>Metadata:</p>
 * <ul>
 * <li>Boolean <code>isInline</code>: Indicates whether this grammar is 
 * inline (or a URI reference). Default is Boolean.FALSE (URI reference)</li>
 * <li>Boolean <code>isDTMF</code>: Indicates whether this is a DTMF 
 * (or voice) grammar. Default is Boolean.FALSE (voice grammar). This is
 * ignored for inline grammars.</li>
 * <li>String <code>name</code>: An optional name for this grammar.</li>
 * </ul>
 * 
 * @author Rahul Akolkar
 */
public class Grammar implements Serializable {

    /** Indicates whether this grammar is inline (or a URI reference)
     *  Default is Boolean.FALSE (URI reference) */
    private Boolean isInline;
    /** Indicates whether this is a DTMF (or voice) grammar
     * Default is Boolean.FALSE (voice grammar) */
    private Boolean isDTMF;
    /** The inline grammar string or URI */
    private String grammar;
    /** The optional name associated with this grammar */
    private String name;

    /**
     * <p>Constructor, no arguments, metadata set to defaults.</p>
     * 
     */
    public Grammar() {
        this.grammar = null;
        this.isInline = Boolean.FALSE;
        this.isDTMF = Boolean.FALSE;
        this.name = null;
    } // Grammar constructor

    /**
     * <p>Constructor, allows user to set metadata associated with
     * grammar.</p>
     * 
     * @param grammar the URI reference
     * @param isDTMF whether this is a DTMF grammar
     * @param isInline whether this is an inline grammar
     * @param name The name for this grammar
     * 
     */
    public Grammar(String grammar, Boolean isDTMF, Boolean isInline, 
            String name) {
        if (grammar == null || grammar.length() == 0) {
            throw new IllegalArgumentException("Grammar cannot be " +
                "instantiated with a null or empty String");
        }
        this.grammar = grammar;
        this.isDTMF = isDTMF;
        this.isInline = isInline;
        this.name = name;
    } // Grammar constructor

    /**
     * <p>Returns a Boolean indicating whether the grammar is DTMF 
     * (or voice).</p>
     * 
     * @return isDTMF whether this is a DTMF grammar
     */
    public Boolean getIsDTMF() {
        return isDTMF;
    }

    /**
     * <p>Set whether this is a DTMF grammar.</p>
     * 
     * @param isDTMF whether this is a DTMF grammar
     */
    public void setIsDTMF(Boolean isDTMF) {
        this.isDTMF = isDTMF;
    }

    /**
     * <p>Returns a Boolean indicating whether the grammar is inline 
     * (or an URI reference).</p>
     * 
     * @return isInline whether this is an inline grammar
     */
    public Boolean getIsInline() {
        return isInline;
    }

    /**
     * <p>Set whether this is an inline grammar.</p>
     * 
     * @param isInline whether this is an inline grammar
     */
    public void setIsInline(Boolean isInline) {
        this.isInline = isInline;
    }

    /**
     * <p>Return the inline grammar or the URI reference (interpretation depends
     * on Boolean isInline).</p>
     * 
     * @return grammar the inline grammar or the URI reference
     */
    public String getGrammar() {
        return grammar;
    }

    /**
     * <p>Set the inline grammar or URI reference (interpretation depends
     * on Boolean isInline).</p>
     * 
     * @param grammar the inline grammar or the URI reference
     */
    public void setGrammar(String grammar) {
        this.grammar = grammar;
    }

    /**
     * <p>Return the name associated with this grammar.
     * The value may be null.</p>
     * 
     * @return name The name for this grammar
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Set the name associated with this grammar.</p>
     * 
     * @param name The name for this grammar
     */
    public void setName(String name) {
        this.name = name;
    }

}
