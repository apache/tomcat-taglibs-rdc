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

import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.taglibs.rdc.core.GroupModel;
import org.apache.taglibs.rdc.core.GroupTag;

/**
 * @author Rahul Akolkar
 */
public class Conditions {
	
	/** 
	 * Java Object corresponding to an individual match condition
	 * defined within a rule in the XML navigation rules structure
	 * for the rule based directed dialog.
	 * 
	 * Target will be chosen if the lvalue matches rvalue.
	 * 
	 * Corresponds to &lt;match&gt; element in XML navigation rules.
	 */
	public static class Match extends RuleBasedDirectedDialog.ConditionImpl {
		public Match() {
			operation = "equal-to";
		}
		public static HashMap getAttrPropMap() {
			HashMap attrPropMap = new HashMap();
			attrPropMap.put("lvalue", "lvalue");
			attrPropMap.put("rvalue", "rvalue");
			attrPropMap.put("target", "target");
			return attrPropMap;
		}
	}
	
	/** 
	 * Java Object corresponding to an individual when condition
	 * defined within a rule in the XML navigation rules structure
	 * for the rule based directed dialog.
	 * 
	 * Target will be chosen if the test succeeds.
	 * 
	 * Corresponds to &lt;when&gt; element in XML navigation rules.
	 */
	public static class When extends RuleBasedDirectedDialog.ConditionImpl {
		public When() {
			operation = "equal-to";
			rvalue = "true";
		}
		public static HashMap getAttrPropMap() {
			HashMap attrPropMap = new HashMap();
			attrPropMap.put("test", "lvalue");
			attrPropMap.put("target", "target");
			return attrPropMap;
		}
	}	
	
	/** 
	 * Java Object corresponding to an individual unless condition
	 * defined within a rule in the XML navigation rules structure
	 * for the rule based directed dialog.
	 * 
	 * Target will be chosen if the test fails.
	 * 
	 * Corresponds to &lt;unless&gt; element in XML navigation rules.
	 */
	public static class Unless extends RuleBasedDirectedDialog.ConditionImpl {
		public Unless() {
			operation = "equal-to";
			rvalue = "false";
		}
		public static HashMap getAttrPropMap() {
			HashMap attrPropMap = new HashMap();
			attrPropMap.put("test", "lvalue");
			attrPropMap.put("target", "target");
			return attrPropMap;
		}		
	}	
	
	/** 
	 * Java Object corresponding to an individual match-attribute condition
	 * defined within a rule in the XML navigation rules structure
	 * for the rule based directed dialog.
	 * 
	 * Target will be chosen if the value of the specified attribute of the
	 * specified  matches rvalue.
	 * 
	 * Corresponds to &lt;match-attribute&gt; element in XML navigation rules.
	 */
	public static class MatchAttribute extends 
		RuleBasedDirectedDialog.ConditionImpl {		

		private String element = null;
		private String name = null;
		public MatchAttribute() {
			operation = "equal-to";
		}
		public static HashMap getAttrPropMap() {
			HashMap attrPropMap = new HashMap();
			attrPropMap.put("element", "element");
			attrPropMap.put("name", "name");
			attrPropMap.put("value", "rvalue");
			return attrPropMap;
		}	
		public String getElement() {
			return element;
		}
		public void setElement(String element) {
			this.element = element;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public void pre() {
			Element elem = null;
			String attr = null;
			try {
				elem = (Element) DMUtils.proprietaryEval(groupTag, groupModel, 
					element, Element.class,	lruCache, tempVars);
				attr = (String) DMUtils.proprietaryEval(groupTag, groupModel, 
					name, String.class, lruCache, tempVars);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (elem != null && elem.hasAttribute(attr)) {
				lvalue = elem.getAttribute(attr);
			}
		}
	}

}
