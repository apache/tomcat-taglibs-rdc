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
/*$Id$*/
package org.apache.taglibs.rdc.core;

import java.util.Stack;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.taglibs.rdc.dm.DialogManagerImpl;

/**
 * <p>This is the implementation of the RDC helper tag group.
 * Group is a container with pluggable DM strategies.</p>
 * 
 * @author Rahul Akolkar
 */
public class GroupTag extends SimpleTagSupport {
	
	// CLASS PROPERTIES
	// The unique identifier associated with the group
	private String id;
	// Holds the submit url
	private String submit;
	// Holds the config url
	private String config;
	// Indicates whether group level confirmation is requested
	// Overrides atom level confirmation, if specified
	private Boolean confirm;
	// The class name that implements the dialog management strategy
	private String strategy;
	

	/* Constructor */
	public GroupTag() {
		super();
		id = null;
		submit = null;
		config = null;
		strategy = null;
		confirm = Boolean.FALSE;
	}

	/**
	 * Gets the ID of the group
	 *
	 * @return ID The group's strategy
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id of the group
	 *
	 * @param id The group's id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the submit URL where group should submit
	 *
	 * @return submit The group's submit URL
	 */
	public String getSubmit() {
		return submit;
	}

	/**
	 * Sets the submit URL for the group
	 *
	 * @param submit The group's submit URL
	 */
	public void setSubmit(String submit) {
		this.submit = submit;
	}

	/**
	 * Gets the configuration URI of the group
	 *
	 * @return config The group's configuration URI
	 */
	public String getConfig() {
		return config;
	}

	/**
	 * Sets the configuration URI of the group
	 *
	 * @param config The group's configuration URI
	 */
	public void setConfig(String config) {
		this.config = config;
	}
	
	/**
	 * Get the group level confirmation
	 * 
	 * @return confirm
	 */
	public Boolean getConfirm() {
		return confirm;
	}

	/**
	 * Set the group level confirmation
	 * 
	 * @param Boolean confirm
	 */
	public void setConfirm(Boolean confirm) {
		this.confirm = confirm;
	}
	
	/**
	 * Get the class name that implements the DM strategy
	 * 
	 * @return strategy the class name
	 */
	public String getStrategy() {
		return strategy;
	}

	/**
	 * Set the class name that implements the DM strategy
	 * 
	 * @param strategy the class name
	 */
	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	/**
	 * Has the state machine for the group
	 *
	 * Uses a pluggable dialog management strategy
	 */
	public void doTag() throws JspException, IOException {
		
		DialogManagerImpl dmImpl = null;
		try {
			dmImpl = (DialogManagerImpl) Class.forName(strategy).newInstance();
			dmImpl.setGroupTag(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (!dmImpl.initialize(getJspContext(), getJspBody())) {
			return;	
		}
		
		dmImpl.collect(getJspContext(), getJspBody());
		
		dmImpl.confirm();
		
		dmImpl.finish(getJspContext());
		
	}

}
