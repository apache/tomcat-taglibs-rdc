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

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.xml.transform.TransformerException;

import org.apache.commons.digester.Digester;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import org.apache.taglibs.rdc.RDCUtils;
import org.apache.taglibs.rdc.core.BaseModel;
import org.apache.taglibs.rdc.core.ComponentModel;
import org.apache.taglibs.rdc.core.GroupTag;
import org.apache.taglibs.rdc.core.GroupModel;
import org.apache.taglibs.rdc.core.Constants;
/**
 * <p>Concrete implementation of the RDC helper tag group using an
 * rule based directed dialog. This dialog management strategy is:
 * <ul>
 * <li><b>Rule-based:</b> Children are executed according to the
 * navigation rules</li>
 * <li><b>Directed:</b> Only one child is active at any given time</li>
 * </ul>
 * <br>Navigation rules specified in an XML document whose URI is the custom
 * attribute of the &lt;group&gt; tag.</p>
 * 
 * @author Rahul Akolkar
 */
public class RuleBasedDirectedDialog extends DialogManagerImpl {
	
	private static final String XPATH_NAVIGATION =
		"*/navigation";
	private static final String XPATH_CONDITION = 
		"/dm-config/ruleset-def/conditions-list/condition-def";
		
	private Navigation navigation = null;
	
	private LinkedHashMap lruCache = new LinkedHashMap();
	private ArrayList tempVars = new ArrayList();
		
	/* Constructor */
	public RuleBasedDirectedDialog() {
		super();
	}
	
	public boolean initialize(JspContext ctx, JspFragment bodyFragment) 
	throws JspException, IOException {
		
		boolean retVal = super.initialize(ctx, bodyFragment);
	
		Digester digester = new Digester();
		digester.setErrorHandler(new NavigationRulesErrorHandler());

		digester.addObjectCreate(XPATH_NAVIGATION, Navigation.class);
		digester.addObjectCreate(XPATH_NAVIGATION + "/initial", 
			NavigationRule.class);
		digester.addSetProperties(XPATH_NAVIGATION + "/initial", 
			new String[] {"target", "defaultTarget"},
			new String[] {"target", "defaultTarget"});
		digester.addObjectCreate(XPATH_NAVIGATION + "/initial/condition",
			ConditionImpl.class);
		digester.addSetProperties(XPATH_NAVIGATION + "/initial/condition", 
			new String[] {"lvalue", "operation", "rvalue", "target"},
			new String[] {"lvalue", "operation", "rvalue", "target"});
		digester.addSetNext(XPATH_NAVIGATION + "/initial/condition", 
			"addCondition" );
		digester.addSetNext(XPATH_NAVIGATION + "/initial","addNavigationRule");

		digester.addObjectCreate(XPATH_NAVIGATION + "/rule", 
			NavigationRule.class);
		digester.addSetProperties(XPATH_NAVIGATION + "/rule", 
			new String[] {"from", "target", "defaultTarget"},
			new String[] {"from", "target", "defaultTarget"});
		digester.addObjectCreate(XPATH_NAVIGATION + "/rule/condition", 
			ConditionImpl.class);
		digester.addSetProperties(XPATH_NAVIGATION + "/rule/condition", 
			new String[] {"lvalue", "operation", "rvalue", "target"},
			new String[] {"lvalue", "operation", "rvalue", "target"});
		digester.addSetNext(XPATH_NAVIGATION + "/rule/condition",
			"addCondition");
		digester.addSetNext(XPATH_NAVIGATION + "/rule", "addNavigationRule" );

		DOMParser dp = new DOMParser();
		final String rules = groupTag.getConfig();
		InputSource inputSrc = null;
		if (!RDCUtils.isStringEmpty(rules) && rules.startsWith("META-INF/")) {
			// unpack rules from distro, which we know validate
			digester.setValidating(false);
			final String jar = ((PageContext)ctx).getServletContext().
				getRealPath(Constants.RDC_JAR);
			inputSrc = RDCUtils.extract(jar, rules);
		} else {
			digester.setValidating(true);
			inputSrc = new InputSource(((PageContext)ctx).getServletContext().
				getRealPath(rules));
		}
		try {
			dp.parse(inputSrc);
		} catch (SAXException sx) {
			throw new IOException("Cannot parse the config: " + 
				groupTag.getConfig());
		} // end of try-catch
		Document d = dp.getDocument();
		NodeList conditionsList = null;
		try {
			conditionsList = XPathAPI.selectNodeList(d.getDocumentElement(),
				XPATH_CONDITION);
		} catch (TransformerException t) {
			t.printStackTrace();
		}

		if (conditionsList != null) {
			HashSet conditionNames = new HashSet();
			conditionNames.add("condition");
			for (int i = 0; i < conditionsList.getLength(); i++) {
				String name = name = ((Element)conditionsList.item(i)).
					getAttribute("name");
				RDCUtils.mustExist(name, "RuleBasedDirectedDialog: Cannot" +
					" add condition with empty name");
				RDCUtils.mustSatisfy(!conditionNames.contains(name),
					"RuleBasedDirectedDialog: Cannot add condition with " +
					"name \"" + name + "\" since a condition with that " +
					"name is already defined.");
				String impl = ((Element)conditionsList.item(i)).
					getAttribute("impl");
				RDCUtils.mustExist(impl, "RuleBasedDirectedDialog: Cannot" +
					" add condition with empty impl");
				Class implClass = null;
				try {
					implClass = Class.forName(impl);
				} catch (ClassNotFoundException cnfe) {
					cnfe.printStackTrace();
					continue;
				}
				RDCUtils.mustSatisfy(RDCUtils.implementsInterface(implClass,
					Condition.class), "RuleBasedDirectedDialog: Cannot" +
					" add condition with name \"" + name + "\". Impl:" +
				   	impl + " does not implement the RuleBasedDirectedDialog." +
				   	"Condition interface.");
				RDCUtils.mustSatisfy(RDCUtils.hasMethod(implClass, 
					"getAttrPropMap", null), "RuleBasedDirectedDialog: Cannot"+
					" add condition with name \"" + name + "\" since" +
					" the impl class " + impl + " does not define an" +
					" accessible method \"static HashMap getAttrPropMap()\"");
				Method m = null;
				HashMap attrPropMap = null;
				try {
					m = implClass.getMethod("getAttrPropMap", null);
					attrPropMap = (HashMap) m.invoke(null, null);
				} catch (Exception e) {
					RDCUtils.warnIf(true, "RuleBasedDirectedDialog: Could not" +
						" invoke getAttrPropMap() method of class: " + impl);
					continue;
				}
				Object[] keysArray = attrPropMap.keySet().toArray();
				String[] attrs = new String[keysArray.length];
				String[] props = new String[keysArray.length];
				for (int j = 0; j < keysArray.length; j++) {
					attrs[j] = (String) keysArray[j];
					RDCUtils.mustExist(attrs[j], "RuleBasedDirectedDialog: " +
						"Cannot add condition element with empty attribute");
					props[j] = (String) attrPropMap.get(attrs[j]);
					RDCUtils.mustExist(attrs[j], "RuleBasedDirectedDialog: " +
						"Attribute " + attrs[j] + " of condition with name " +
						name +" is not mapped to a property of class " + impl);
					RDCUtils.mustSatisfy(RDCUtils.hasField(implClass, props[j]),
						 "RuleBasedDirectedDialog: Attribute " + attrs[j] +
						" of condition with name " + name + " is mapped " +
						"to a property " + props[j] + ", which does not " +
						"exist in the impl class " + impl);
				}
				digester.addObjectCreate(XPATH_NAVIGATION + "/initial/" + name,
					impl);
				digester.addSetProperties(XPATH_NAVIGATION + "/initial/" +name,
					attrs, props);
				digester.addSetNext(XPATH_NAVIGATION + "/initial/" + name,
					"addCondition");
				digester.addObjectCreate(XPATH_NAVIGATION + "/rule/" + name,
					impl);
				digester.addSetProperties(XPATH_NAVIGATION + "/rule/" + name,
					attrs, props);
				digester.addSetNext(XPATH_NAVIGATION + "/rule/" + name,
					"addCondition" );
				conditionNames.add(name);
			}
		}

		try {
			navigation = (Navigation) digester.parse(((PageContext)
				ctx).getServletContext().getRealPath(groupTag.getConfig()));
		} catch (Exception e) {
			retVal = false;
			((PageContext) ctx).getOut().write("<!-- RuleBasedDirectedDialog" +
				": Error parsing XML navigation rules:" + groupTag.getConfig()+
				"-->\n");
			e.printStackTrace();
		}

		return retVal;
	}

	/**
	 * Collect the required information from the user
	 *
	 *
	 */		
	public void collect(JspContext ctx, JspFragment bodyFragment) 
		throws JspException, IOException {
		
		checkRuleSetStatus();

		GroupModel groupModel = (GroupModel) stateMap.get(groupTag.getId());
		Map modelMap = groupModel.getLocalMap();

		if (modelMap.get(Constants.STR_INIT_ONLY_FLAG) == Boolean.TRUE) {
			// Start off with all children in dormant state
			setStateChildren(modelMap, Constants.FSM_DORMANT);
			groupState = Constants.GRP_ALL_CHILDREN_DORMANT;
			groupModel.setState(Constants.GRP_STATE_RUNNING);
			modelMap.put(Constants.STR_INIT_ONLY_FLAG, Boolean.FALSE);
		}

		if (groupModel.getState() == Constants.GRP_STATE_RUNNING) {
			do {
				dialogManager(groupTag);
				if (bodyFragment != null) {
					bodyFragment.invoke(null);
				}
			} while (renderNextChild(groupModel));
		}
	}
	
	/**
	 * Check the health of the navigation rule set, and provide
	 * client feedback for error conditions.
	 * 
	 * @param navigation The Navigation rule set
	 */
	private void checkRuleSetStatus() {
		
		if (navigation == null) {
			try {
				((PageContext) groupTag.getJspContext()).getOut().
				write("<!-- RuleBasedDirectedDialog: Error parsing XML " +
				"navigation rules - " + groupTag.getConfig() + "-->\n");
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			return;
		}
		
		String navHealth = (String)navigation.get(Navigation.
			NAV_RULE_SET_CREATION);
		StringBuffer strbuf = new StringBuffer("<!--");
		if (navHealth == null) {
			strbuf.append("The initial navigation rule");
		} else if (!navHealth.equals(Navigation.NAV_SUCCESS)) {
			strbuf.append("The navigation rule originating from component " +
				"with ID \"" + navHealth + "\"");
		} else {
			return;
		}
		strbuf.append(" has been overwritten. Correct the XML navigation " +
			"rule set in " + groupTag.getConfig() + " -->\n");

		try {
			((PageContext) groupTag.getJspContext()).getOut().
			write(strbuf.toString());
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	/** 
	 * This method does the rule based dialog management based on the
	 * navigational rules supplied
	 */
	private void dialogManager(GroupTag groupTag) {

		GroupModel groupModel = (GroupModel) stateMap.get(groupTag.getId());
		Map children = groupModel.getLocalMap();
		if (children == null) {
			return;
		}

		ArrayList activeChildren = groupModel.getActiveChildren();
		String currentExec = null;
		NavigationRule navRule = null;

		if (activeChildren.size() == 0) {
			/* 
			 * SAMPLE XML navigation rule executed here:
			 * <intial target="first-rdc-id" />
			 */
			navRule = (NavigationRule) navigation.get(null);
		} else {
			// Only one child executes at a time in this strategy
			currentExec = (String) activeChildren.get(0);
			BaseModel model = (BaseModel) children.get(currentExec);
			if (!DMUtils.isChildDone(model)) {
				return;
			}
			navRule= (NavigationRule) navigation.get(currentExec);
		}
			
		if (navRule != null) {
			
			if (navRule.getConditions().size() == 0) {
				currentExec = navRule.getTarget();
				invokeChild(children, activeChildren, currentExec);
			} else {
				ArrayList conditionals = (ArrayList) 
					navRule.getConditions();
				currentExec = identifyTarget(conditionals, groupTag,
					groupModel, lruCache, tempVars);
				if (currentExec != null) {
					invokeChild(children, activeChildren, currentExec);
				} else {
					currentExec = navRule.getDefaultTarget();
					if (currentExec != null) {
						invokeChild(children, activeChildren, currentExec);
					} else {
						if (activeChildren.size() > 0) {
							activeChildren.remove(0); 
						}
						groupState = Constants.GRP_ALL_CHILDREN_DONE;
						System.out.println("RuleBasedDirectedDialog: None" + 
						" of the conditions satisfied for " + 
						(navRule.getFrom() ==  null ? "initial" : "") +
						" navigation rule " +
						(navRule.getFrom() !=  null ? "from: " + 
						navRule.getFrom() : "") + " - finished execution" +
						" of group with ID " + groupTag.getId());
					}
				}
			}
				
		} else {
			// No navigation rule available, we're all done
			if (activeChildren.size() > 0) {
				activeChildren.remove(0);
			}
			groupState = Constants.GRP_ALL_CHILDREN_DONE;
		}

	} // end method dialogManagerOD()

	/** 
	 * Render next child in execution order if:
	 * a) The currently active child is done
	 * and
	 * b) The currently active child is not the last child
	 * in the execution order
	 */
	private static boolean renderNextChild(GroupModel groupModel) {
		Map children = groupModel.getLocalMap();
		ArrayList activeChildren = groupModel.getActiveChildren();
		// Only one child executes at a time in this strategy
		if (activeChildren.size() > 0) {
			BaseModel model = (BaseModel)children.get(activeChildren.get(0));
			if (DMUtils.isChildDone(model)) {
				return true;
			}
		}
		return false;
	}

	/** 
	 * Activate child so it takes over the dialog from the next turn
	 */
	private void invokeChild(Map children, ArrayList activeChildren,
		String id) {
		if (id == null) {
			if (activeChildren.size() > 0) {
				activeChildren.remove(0); 
			}
			groupState = Constants.GRP_ALL_CHILDREN_DONE;
			(new Exception("RuleBasedDirectedDialog: " + 
			"Null ID of identified target")).printStackTrace();
			return;
		}
		BaseModel model = (BaseModel) children.get(id);
		if (model != null) {
			if (groupState != Constants.GRP_SOME_CHILD_RUNNING) {
				groupState = Constants.GRP_SOME_CHILD_RUNNING;
			}
			if (activeChildren.size() > 0) {
				activeChildren.remove(0); 
			} 
			activeChildren.add(id);
			if (model instanceof BaseModel) {
				model.setState(Constants.FSM_INPUT);
			} else if (model instanceof GroupModel) {
				model.setState(Constants.GRP_STATE_RUNNING);
			} else if (model instanceof ComponentModel) {
				model.setState(Constants.FSM_INPUT);
			}
		} else {
			// Invalid child ID specified in XML navigation rules
			// Abort group execution -- return values collected
			// upto this point
			if (activeChildren.size() > 0) {
				// Directed dialog
				activeChildren.remove(0); 
			} 
			groupState = Constants.GRP_ALL_CHILDREN_DONE;
			(new Exception("RuleBasedDirectedDialog: " + 
			"ID of target in navigation rule is invalid " + 
			id)).printStackTrace();
		}
	}

	
	/** 
	 * Evaluate the given list of Condition objects with respect
	 * to the state of the given GroupModel.
	 *
	 * @param Condition
	 */	
	private static String identifyTarget(ArrayList conditions, GroupTag groupTag,
		GroupModel groupModel, LinkedHashMap lruCache, ArrayList tempVars) {
		String currentExec = null;
		boolean goodCondition = false;
		// Assumes navigation rules define a deterministic
		// state transition graph
		for (int i = 0; i < conditions.size() && 
			!goodCondition; i++) {
			Object condData = conditions.get(i);
			if (condData instanceof Condition) {
				Condition cond = (Condition) condData;
				cond.setExecutionContext(groupTag, groupModel, lruCache,
					tempVars);
				goodCondition = cond.isSatisfied();
				if (goodCondition) {
					currentExec = ((Condition) condData).getTarget();
				}
			} else {
				(new Exception("RuleBasedDirectedDialog: List of " +
				"conditionals contains instance of unexpected class - " + 
				condData.getClass().getName())).printStackTrace();
			}
		}
		cleanup(groupTag.getJspContext(), tempVars, lruCache);
		return currentExec;
	}	
	
	/** 
	 * Remove all temporary attributes from this page context and clear
	 * the cache
	 *
	 */
	private static void cleanup(JspContext ctx,	ArrayList attributeList, 
		LinkedHashMap cache) {
		for (int i = 0; i < attributeList.size(); i++) {
			ctx.removeAttribute((String)attributeList.get(i));
		}
		attributeList.clear();
		cache.clear();
	}
	
	/** 
	 * Java Object encapsulating the XML navigation rules.
	 *
	 * Corresponds to the root &lt;navigation&gt; element in XML
	 * navigation rules.
	 */
	public static class Navigation {
		
		private HashMap navMap = null;
		// Following strings should not be XML NMTOKENs
		public static final String NAV_RULE_SET_CREATION = "<";
		public static final String NAV_SUCCESS = ">";
		
		public Navigation() {
			navMap = new HashMap();
			navMap.put(NAV_RULE_SET_CREATION, NAV_SUCCESS);
		}
		
		public Object get(String key) {
			return navMap.get(key);
		}

		// This is a directed dialog strategy where only one child
		// executes at a time.
		// It is expected that one unique target is specified by the XML
		// navigation rule set for any given state transition within
		// the group.
		public void addNavigationRule(NavigationRule navRule) {
			String key = navRule.getFrom();
			if (navMap.containsKey(key)) {
				navMap.put(NAV_RULE_SET_CREATION, key);
			}
			navMap.put(key, navRule);
		}

	}

	/** 
	 * Java Object corresponding to an individual XML navigation
	 * rule defined in the XML navigation rules structure.
	 * 
	 * Corresponds to &lt;rule&gt; element in XML navigation rules.
	 */	
	public static class NavigationRule {

		private String from;
		private String target;
		private String defaultTarget;
		private ArrayList conditions;
		
		public NavigationRule() {
			from = null;
			target = null;
			defaultTarget = null;
			conditions = new ArrayList();
		}
		
		public String getFrom() {
			return from;
		}

		public void setFrom(String from) {
			this.from = from.trim();
		}
		
		public String getTarget() {
			return target;
		}

		public void setTarget(String target) {
			this.target = target.trim();
		}

		public String getDefaultTarget() {
			return defaultTarget;
		}
				
		public void setDefaultTarget(String defaultTarget) {
			this.defaultTarget = defaultTarget.trim();
		}

		public ArrayList getConditions() {
			return conditions;
		}

		public void addCondition(Condition condition) {
			conditions.add(condition);
		}

	}


	/**
	 * Condition interface
	 * 
	 * Implementing classes must also define an accessible method
	 * of the following signature: static HashMap getAttrPropMap()
	 */
	public static interface Condition {

		public void setExecutionContext(GroupTag groupTag,
			GroupModel groupModel, LinkedHashMap lruCache,
			ArrayList tempVars);

		public boolean isSatisfied();

		public String getTarget();
	
	}

	/** 
	 * Java Object corresponding to an individual condition defined within a
	 * rule in the XML navigation rules structure.
	 * 
	 * Corresponds to &lt;condition&gt; element in XML navigation rules.
	 */
	public static class ConditionImpl implements Condition {
		
		private static final HashMap opCodes = new HashMap();
		private static final HashMap lvalueTypes = new HashMap();
		private static final HashMap rvalueTypes = new HashMap();
		private static final int UNSUPPORTED_OPERATION = 873264831;
		
		static {
			opCodes.put("less-than", "-1");
			opCodes.put("equal-to", "0");
			opCodes.put("greater-than", "1");
		}
		
		protected String lvalue;
		protected String operation;
		protected String rvalue;
		protected String target;
		protected GroupTag groupTag;
		protected GroupModel groupModel;
		protected LinkedHashMap lruCache;
		protected ArrayList tempVars;
		protected boolean isExecCtxSet;
		
		public ConditionImpl() {
			lvalue = null;
			operation = null;
			rvalue = null;
			target = null;
			isExecCtxSet = false;
		}
			
		public void setExecutionContext(GroupTag groupTag,
			GroupModel groupModel, LinkedHashMap lruCache,
			ArrayList tempVars) {
			this.groupTag = groupTag;
			this.groupModel = groupModel;
			this.lruCache = lruCache;
			this.tempVars = tempVars;
			if (groupTag != null && groupModel != null) {
				isExecCtxSet = true;
			}
		}
		
		public static HashMap getAttrPropMap() {
			HashMap attrPropMap = new HashMap();
			attrPropMap.put("lvalue", "lvalue");
			attrPropMap.put("operation", "operation");
			attrPropMap.put("rvalue", "rvalue");
			attrPropMap.put("target", "target");
			return attrPropMap;
		}

		public String getLvalue() {
			return lvalue;
		}

		public String getRvalue() {
			return rvalue;
		}
		
		public String getOperation() {
			return operation;
		}
		
		public String getTarget() {
			return target;
		}
				
		public void setLvalue(String string) {
			lvalue = string;
		}

		public void setRvalue(String string) {
			rvalue = string;
		}

		public void setOperation(String operation) {
			this.operation = operation.trim();
		}

		public void setTarget(String target) {
			this.target = target.trim();
		}

		public int getOpCode() {
			int i = UNSUPPORTED_OPERATION;
			try {
				i = Integer.parseInt((String)opCodes.get(operation));
			} catch (NumberFormatException nfe) {
				// unsupported operation - do nothing
			}
			return i;
		}
		
		public static String getLvalueType(int opCode) {
			return (String)lvalueTypes.get("" + opCode);
		}
		
		public static String getRvalueType(int opCode) {
			return (String)rvalueTypes.get("" + opCode);
		}
		
		public void pre() {
			// pre hook for subclasses
		}
		
		public void post() {
			// post hook for subclasses
		}
		
		public boolean isSatisfied() {
			
			pre();
			int opCode = getOpCode();
			String lvalueClassName = getLvalueType(opCode);
			String rvalueClassName = getRvalueType(opCode);
			// operands are assumed to be strings by default
			Class lvalueClass = java.lang.String.class;
			Class rvalueClass = java.lang.String.class;
			if (!RDCUtils.isStringEmpty(lvalueClassName)) {
				lvalueClass = RDCUtils.getClass(lvalueClassName);
			}
			if (!RDCUtils.isStringEmpty(rvalueClassName)) {
				rvalueClass = RDCUtils.getClass(rvalueClassName);
			}
			RDCUtils.mustSatisfy(lvalueClass != null && rvalueClass != null,
				"RuleBasedDirectedDialog.ConditionImpl: " + getClass().
				getName()+" condition has an undefined lvalue or rvalue type");
			String lvalue_expr = getLvalue();
			RDCUtils.warnIf(RDCUtils.isStringEmpty(lvalue_expr),
				"RuleBasedDirectedDialog.ConditionImpl: " + 
				getClass().getName() + " condition has a null expression");
			Object lvalue = DMUtils.proprietaryEval(groupTag, groupModel,
				lvalue_expr, lvalueClass, lruCache, tempVars);
			String rvalue_expr = getRvalue();
			RDCUtils.warnIf(RDCUtils.isStringEmpty(rvalue_expr),
				"RuleBasedDirectedDialog.ConditionImpl: " + 
				getClass().getName() + " condition has a null expression");
			Object rvalue = DMUtils.proprietaryEval(groupTag, groupModel,
				rvalue_expr, rvalueClass, lruCache, tempVars);
			post();
			//System.out.println("Lvalue: " + lvalue + ", Rvalue: " + rvalue);
	
			switch (opCode) {
				// 1) Currently only proof of concept string based comparison
				//    operations are supported. User can define any number of
				//    custom operations.
				// 2) Operations can be made to behave differently based on 
				//    lvalue and rvalue data types.
				// 3) This is essential since RDCs can return an instance of an
				//    arbitrary Java class
				case 0:
					// equal-to
					if (lvalue instanceof String && rvalue instanceof String) {		
						if (lvalue != null && ((String)lvalue).
							equals((String)rvalue)) {
							return true;
						} else {
							return false;
						}
					}
					return false;
				case -1:
					// less-than
					if (lvalue instanceof String && rvalue instanceof String) {	
						if (lvalue != null && ((String)lvalue).
							compareTo((String)rvalue) < 0) {
							return true;
						} else {
							return false;
						}
					}
					return false;
				case 1:
					// greater-than
					if (lvalue instanceof String && rvalue instanceof String) {
						if (lvalue != null && ((String)lvalue).
							compareTo((String)rvalue) > 0) {
							return true;
						} else {
							return false;
						}
					}
					return false;						
				default:
					return false;
			}
		}
	}

	/** 
	 * Custom error handler to communicate parsing errors in the
	 * XML navigation rules to the client.
	 */	
	private class NavigationRulesErrorHandler implements ErrorHandler {
		
		private static final String MSG_PREFIX = "<!-- " +
			"RuleBasedDirectedDialog: XML navigation rules - ";
		private static final String MSG_POSTFIX = " Correct the XML " +
			" navigation rule set. -->\n";
		
		public void error(SAXParseException s) {
			try {
				((PageContext) groupTag.getJspContext()).getOut().
				write(MSG_PREFIX + groupTag.getConfig() + " : Error - " +
				s.getMessage() + MSG_POSTFIX);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		
		public void fatalError(SAXParseException s) {
			try {
				((PageContext) groupTag.getJspContext()).getOut().
				write(MSG_PREFIX + groupTag.getConfig() + " : Fatal Error - " +
				s.getMessage() + MSG_POSTFIX);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}			
		}
		
		public void warning(SAXParseException s) {
			try {
				((PageContext) groupTag.getJspContext()).getOut().
				write(MSG_PREFIX + groupTag.getConfig() + " : Warning - " +
				s.getMessage() + MSG_POSTFIX);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}			
		}
	}

}
