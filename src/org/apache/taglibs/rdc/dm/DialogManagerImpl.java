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
package org.apache.taglibs.rdc.dm;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Stack;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.io.IOException;
import java.lang.reflect.Method;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.taglibs.rdc.core.BaseModel;
import org.apache.taglibs.rdc.core.GroupModel;
import org.apache.taglibs.rdc.core.GroupTag;
import org.apache.taglibs.rdc.core.GroupConfirm;
import org.apache.taglibs.rdc.core.DialogManager;
import org.apache.taglibs.rdc.core.Constants;

/**
 * <p>Abstract base implementation of the RDC helper tag group.</p>
 * 
 * @author Rahul Akolkar
 */
public abstract class DialogManagerImpl implements DialogManager {

	// Error messages (to be i18n'zed)
	private static final String ERR_SET_PROPERTY = "Error while setting " +
		"\"{0}\" property on model with ID \"{1}\"; received error message: " +
		"\"{2}\"\n";
	
	// Logging
	private static Log log = LogFactory.getLog(DialogManagerImpl.class);
	
	// The RDC stack
	protected Stack stack;
	// The private data Model of Group
	protected LinkedHashMap stateMap;
	// The GroupTag object whose dialog we are managing
	protected GroupTag groupTag;
	// The state of the group implementation
	protected int groupState;

	/* Constructor */
	public DialogManagerImpl() {
	}	
	
	public void setGroupTag(GroupTag groupTag) {
		this.groupTag = groupTag;
	}

	/**
	 * Initialiaze this doTag() invocation
	 *
	 *
	 */	
	public boolean initialize(JspContext ctx, JspFragment bodyFragment) 
	throws JspException, IOException {
		
		//Get the stateMap from the rdcStack to store the GroupModel
		stack = (Stack) ctx.getAttribute(Constants.STR_RDC_STACK,
				PageContext.REQUEST_SCOPE);
		if (stack != null) {
			stateMap = (LinkedHashMap) stack.peek();
		} else {
			throw new JspException(Constants.ERR_NO_RDC_STACK);
		}
		if (stateMap == null) {
			throw new JspException(Constants.ERR_EMPTY_RDC_STACK);
		}

		// Get the GroupModel from the stateMap or create one if it does not 
		// exist and put it in stateMap 	
		GroupModel groupModel = (GroupModel) stateMap.get(groupTag.getId());
		if (groupModel == null) {
			groupModel = new GroupModel();
			groupModel.setId(groupTag.getId());
			groupModel.setSubmit(groupTag.getSubmit());
			if (groupModel.getLocalMap() == null) {
				groupModel.setLocalMap(new LinkedHashMap());
			}
			if (groupModel.getGroupConfirm() == null && 
				groupTag.getConfirm().booleanValue()) {
				groupModel.setGroupConfirm(new GroupConfirm((PageContext)
					ctx, groupModel, groupTag.getId()));
			}
			groupModel.getLocalMap().put(Constants.STR_INIT_ONLY_FLAG, Boolean.TRUE);
			stateMap.put(groupTag.getId(), groupModel);
		}

		/*
		 * Return now, if:
		 * 1) Caller is in initOnly mode, just return as we already registered
		 * or
		 * 2) In dormant state then turn for execution has not yet come. 
		 * Check for null value as this may be called directly from JSP page
		 */
		if (((Boolean) stateMap.get(Constants.STR_INIT_ONLY_FLAG) == Boolean.TRUE)
			|| (groupModel.getState() == Constants.FSM_DORMANT)) {
			return false;
		}

		Map modelMap = groupModel.getLocalMap();
		stack.push(modelMap);

		if (modelMap.get(Constants.STR_INIT_ONLY_FLAG) == Boolean.TRUE) {
			// Ask children to register if in registration phase 
			if (bodyFragment != null) {
				bodyFragment.invoke(null);
			}
			// Push the submit down to the children
			if (groupTag.getSubmit() != null) {
				setPropertyChildren(modelMap, "submit", groupTag.getSubmit());
			} else {
				throw new NullPointerException("Group " + groupTag.getId() +
					" has no specified submit URI");
			}
		}
		return true;
	}

	/**
	 * Collect the required information from the user
	 *
	 *
	 */		
	public abstract void collect(JspContext ctx, JspFragment bodyFragment) 
	throws JspException, IOException;
	
	/**
	 * Confirm the collected information from the user
	 *
	 *
	 */		
	public void confirm() {
		
		GroupModel groupModel = (GroupModel) stateMap.get(groupTag.getId());
		Map modelMap = groupModel.getLocalMap();
		
		if (groupModel.getState() == Constants.GRP_STATE_RUNNING) {
			if (groupState == Constants.GRP_ALL_CHILDREN_DONE) {
				if (groupTag.getConfirm().booleanValue()) {
					if (groupModel.getGroupConfirm().doGroupConfirmation() == 
						Constants.CONF_STATE_DONE) {
							groupModel.setState(Constants.GRP_STATE_DONE);
					}
				} else {
					groupModel.setState(Constants.GRP_STATE_DONE);
				}
			}
		} 
	}

	/**
	 * Finish up this doTag() invocation
	 *
	 *
	 */	
	public void finish(JspContext ctx) throws JspException, IOException {
		
		GroupModel groupModel = (GroupModel) stateMap.get(groupTag.getId());
		Map modelMap = groupModel.getLocalMap();
	
		// Populate the return value HashMap and provide the JSP scripting var
		if (groupModel.getState() == Constants.GRP_STATE_DONE) {
			Map retValMap = createReturnValueMap(modelMap);
			groupModel.setValue(retValMap);
			ctx.setAttribute(groupTag.getId(), retValMap);
		}
	
		stack.pop();
	}

	/** 
	 * Creates the Map for the retVal of the group tag where the keys
	 * are the id's of the children and the value is the BaseModel.value
	 * object of each child
	 *
	 * @param children Map that holds the id's and child data models
	 */
	private Map createReturnValueMap(Map children) {

		if (children == null) {
			return null;
		}

		Map retValMap = new HashMap();
		Iterator iter = children.keySet().iterator();
		String current = null;

		// Loop for all the children in the list, get their values
		// fill the valuemap with {Id, Value} pair for all children RDCs
		while (iter.hasNext()) {
			current = (String) iter.next();
			if (current.equals(Constants.STR_INIT_ONLY_FLAG)) {
				continue;
			}
			retValMap.put(current, 
				((BaseModel) children.get(current)).getValue());
		} // end while() loop over all the children	

		return retValMap;
	} // end createReturnValueMap()
	
	
	/** 
	 * Set the state of the children
	 *
	 * @param children Map 
	 * @param state the new state for the children
	 * 
	 */
	protected void setStateChildren(Map children, int state) {
		if (children == null) {
			return;
		}

		Iterator iter = children.keySet().iterator();
		String current;
		BaseModel model = null;

		//Loop while there are elements in the list
		while (iter.hasNext()) {
			current = (String) iter.next();
			if (current.equals(Constants.STR_INIT_ONLY_FLAG)) {
				continue;
			}

			model = (BaseModel) children.get(current);
			if (model != null) {
				model.setState(state);
			}
		}
	}
	
	/**
	 * Sets property for all children to value
	 * 
	 *
	 */
	protected void setPropertyChildren(Map children, String propertyName, 
		Object value) {
			
		if (children == null || propertyName == null ||
			propertyName.length() == 0) {
			return;
		}

		Iterator iter = children.keySet().iterator();
		String current;
		BaseModel model = null;
		String setterName = "set" + propertyName.substring(0,1).toUpperCase() +
			((propertyName.length() > 1) ? propertyName.substring(1) : 
			Constants.STR_EMPTY);
		Class[] argClasses = { value.getClass() };
		Object[] args = { value };

		//Loop while there are elements in the list
		while (iter.hasNext()) {
			current = (String) iter.next();
			if (current.equals(Constants.STR_INIT_ONLY_FLAG)) {
				continue;
			}

			model = (BaseModel) children.get(current);
			if (model != null) {
				Class modelClass = model.getClass();
				try {	
					Method setter = modelClass.getMethod(setterName, 
						argClasses);
					setter.invoke((Object)model, args);
				} catch (Exception e) {
					MessageFormat msgFormat = 
						new MessageFormat(ERR_SET_PROPERTY);
		        	log.error(msgFormat.format(new Object[] {propertyName,
		        		model.getId(), e.getMessage()}));
				}
			}
		}
	}
}
