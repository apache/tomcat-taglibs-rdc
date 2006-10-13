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

import org.apache.taglibs.rdc.core.BaseModel;
/**
 * Datamodel for the pause RDC. 
 * The pause RDC is used to put an RDC based application "on hold".
 * The utterance "resume" will "unpause" the application.
 *
 * @author Rahul Akolkar
 */
public class Pause extends BaseModel {

    // The URI where the application will resume after being "unpaused"
    private String resumeURI;

    /**
      * Sets default values for all data members
      */
    public Pause() {
        super();
        this.resumeURI = null;
    }
    
    /**
     * Get the URI where the application will resume after being "unpaused"
     * 
     * @return String the resumeURI
     */
    public String getResumeURI() {
        return resumeURI;
    }

    /**
     * Set the URI where the application will resume after being "unpaused"
     * 
     * @param String the resumeURI
     */
    public void setResumeURI(String string) {
        this.resumeURI = string;
    }
    
    /**
     * Return the resumeURI when on the voice browser's "filled" event
     * 
     * @param input The grammar match from the "resume" grammar
     * @return The value of pause RDC (which will be the resumeURI as defined
     *         in the public contract)
     */
    protected Object canonicalize(Object input, boolean isAttribute) {
        return resumeURI;        
    } // end canonicalize()

}
