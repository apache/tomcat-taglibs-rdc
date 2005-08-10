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
 *
 */
package org.apache.taglibs.rdc;

import javax.servlet.jsp.PageContext;
import org.apache.taglibs.rdc.core.ComponentModel;

/**
 * DataModel for the DateRange Composite RDC
 *
 * @author Elam Birnbaum
 */
public class DateRange extends ComponentModel 
{
	public DateRange()
	{
		super();
	}
	
	/** 
	  * Stores the id and file attributes from the config xml to the configMap
	  * 
	  * @see ComponentModel#configHandler()
	  */
	public void configHandler() 
	{
		this.configMap = RDCUtils.configHandler(this.config, (PageContext) this.context);
	}
}
