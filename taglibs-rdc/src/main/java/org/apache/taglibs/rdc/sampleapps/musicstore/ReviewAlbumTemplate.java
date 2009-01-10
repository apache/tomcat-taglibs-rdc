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

import org.apache.taglibs.rdc.core.RDCTemplate;

/**
 *
 * The RDC template instance used for operations over an individual
 * music album [ describing album details, initiate addition of album to
 * shopping cart ]
 * 
 * @author Rahul Akolkar
 */
public class ReviewAlbumTemplate extends RDCTemplate {

    /**A constant for Error Code stating album details should 
     * be played out */
    public static final int ERR_NEED_DETAILS = 1;
    public final int getERR_NEED_DETAILS() { return ERR_NEED_DETAILS; }

    /**
     * Sets default values for all data members
     */
    public ReviewAlbumTemplate() {
        super();
    }

    /**
     * Custom validation
     * 
     * @see org.apache.taglibs.rdc.core.BaseModel#validate(Object,boolean)
     */
    protected Boolean validate(Object newValue, boolean setErrorCode) {
        if (newValue.equals("details")) {
            if (setErrorCode) setErrorCode(ERR_NEED_DETAILS);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

}

