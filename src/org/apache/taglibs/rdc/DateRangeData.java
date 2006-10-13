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
 * The public data model of the date range composite.
 * 
 * @author Elam Birnbaum
 */
public class DateRangeData implements Serializable 
{
    // Start and end dates
    private String startDate;
    private String endDate;

    public DateRangeData() 
    {
        this.startDate = null;
        this.endDate = null;
    }
    
    /**
     * Gets the start date
     * 
     * @return Returns the start date.
     */
    public String getStartDate() 
    {
        return startDate;
    }
    
    /**
     * Gets the end date
     * 
     * @return Returns the end date.
     */
    public String getEndDate() 
    {
        return endDate;
    }

    /**
     * Sets the start date
     * 
     * @param startDate The start date to set.
     */
    public void setStartDate(String startDate) 
    {
        this.startDate = startDate;
    }

    /**
     * Sets the ending date
     * 
     * @param endDate The ending to set.
     */
    public void setEndDate(String endDate) 
    {
        this.endDate = endDate;
    }
    
    /**
     * A serialized version of the DateRangeData object
     */
    public String toString() 
    {
        StringBuffer buf = new StringBuffer();
        buf.append("startDate=").append(startDate).
            append(";endDate=").append(endDate);
        return buf.toString();
    }
}
