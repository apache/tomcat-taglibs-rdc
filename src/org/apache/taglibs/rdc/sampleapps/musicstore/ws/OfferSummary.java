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
package org.apache.taglibs.rdc.sampleapps.musicstore.ws;

/**
 * Offer Summary Object to store price offers and available
 * total of each pricing offers
 * 
 * @author Thomas Ling 
 * @author Rahul Akolkar
 */

public class OfferSummary implements java.io.Serializable {

    int newPrice;
    int usedPrice;
    int collectPrice;
    int totalNew;
    int totalUsed;
    int totalCollect;
    
    /**
     * Constructor
     */
    public OfferSummary(int newPrice, int usedPrice, int collectPrice,
                        int totalNew, int totalUsed, int totalCollect) {
        this.newPrice = newPrice;
        this.usedPrice = usedPrice;
        this.collectPrice = collectPrice;
        this.totalNew = totalNew;
        this.totalUsed = totalUsed;
        this.totalCollect = totalCollect;
    }

    /**
     * @return Returns the collectPrice.
     */
    public int getCollectPrice() {
        return collectPrice;
    }
    /**
     * @return Returns the newPrice.
     */
    public int getNewPrice() {
        return newPrice;
    }
    /**
     * @return Returns the totalCollect.
     */
    public int getTotalCollect() {
        return totalCollect;
    }
    /**
     * @return Returns the totalNew.
     */
    public int getTotalNew() {
        return totalNew;
    }
    /**
     * @return Returns the totalUsed.
     */
    public int getTotalUsed() {
        return totalUsed;
    }
    /**
     * @return Returns the usedPrice.
     */
    public int getUsedPrice() {
        return usedPrice;
    }
}
