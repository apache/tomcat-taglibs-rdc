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

/**
 * 
 * 
 * @author Sindhu Unnikrishnan
 */
 
public class MortgageData 
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

	
}
