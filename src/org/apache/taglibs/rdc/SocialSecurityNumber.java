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

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.taglibs.rdc.core.BaseModel;

/**
 * Datamodel for the ssn (social security number) RDC. 
 * The ssn RDC is associated with the Social Security Number input and a 
 * pattern to which the input must conform. The SSN is a 9 digit number 
 * and should conform to this length.
 *
 * @author Tanveer Faruquie
 * @author Rahul Akolkar
 */

public class SocialSecurityNumber extends BaseModel {
    // The ssn RDC is associated with the Social Security Number
    // input and a pattern to which the input must conform. The ssn is a 9 digit
    // number and should conform to this length.

    // The social security number input must conform to this pattern
    private String pattern;

    // Error codes, defined in configuration file
    /**A constant for Error Code stating Invalid SSN */
    public static final int ERR_INVALID_SSN_CODE = 1;

    /**A constant for Error Code stating the incorrect length of SSN */
    public static final int ERR_NEED_CORRECT_LENGTH_SSN_CODE = 2;

    // the length of the social security number is 9
    public static final int SSN_LENGTH = 9;

    /**
      * Sets default values for all data members
      */
    public SocialSecurityNumber() {
        super();
        // Default pattern allows any combination of digits
        this.pattern = "[0-9]+";
    }

    /**
     * Sets the pattern string to which the input must conform
     * 
     * @param pattern the pattern string to which the input must conform
     */
    public void setPattern(String pattern) {
        if (pattern != null) {
            try {
                Pattern.compile(pattern);
                this.pattern = pattern;
            } catch (PatternSyntaxException e) {
                throw new IllegalArgumentException("pattern attribute of \"" +
                    getId()    + "\" ssn tag not in proper format.");
            }
        }
    }

    /**
     * Gets the pattern string
     * 
     * @return the pattern string
     */
    public String getPattern() {
        return this.pattern;
    }

    /**
     * Validates the input against the given constraints
     * 
     * @return TRUE if valid, FALSE otherwise
     */
    protected Boolean validate(Object newValue, boolean setErrorCode) {

        if (pattern != null && !(Pattern.matches(pattern, (String)newValue))) {
            if (setErrorCode) setErrorCode(ERR_INVALID_SSN_CODE);
            return Boolean.FALSE;
        }
        if (((String) newValue).length() != SSN_LENGTH) {
            if (setErrorCode) setErrorCode(ERR_NEED_CORRECT_LENGTH_SSN_CODE);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
