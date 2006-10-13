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
/**
 * The data model of the mortgage composite.
 * 
 * @author Sindhu Unnikrishnan
 * @author Rahul Akolkar
 */
public class MortgageData implements Serializable
{

    private String percent;
    private String mortgageType;

    public MortgageData() {
        this.percent = null;
        this.mortgageType = null;
                
    }
    /**
     * Get the mortgage percentage value
     *
     * @return the mortgage type value
     */
    public String getPercent() {
        return percent;
    }

    /**
     * Set the mortgage percentage value 
     *
     * @param input The input value.
     */
    public void setPercent(String input) {
        this.percent = input;

        }
    
    
    /**
     * Get the mortgage type value
     *
     * @return the mortgage type value
     */
    public String getMortgageType() {
        return mortgageType;
    }
    
    /**
     * Set the mortgage type value 
     *
     * @param input The input value.
     */
    public void setMortgageType(String input) {
        this.mortgageType = input;
    }
    
    /**
     * A serialized version of MortgageData object
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("mortgagetype=").append(this.mortgageType).
            append(";percentdown=").append(this.percent);
        return buf.toString();
    }

    
}
