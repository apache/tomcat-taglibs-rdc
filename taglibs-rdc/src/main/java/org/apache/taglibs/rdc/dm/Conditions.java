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
package org.apache.taglibs.rdc.dm;

import java.text.MessageFormat;
import java.util.Map;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;

/**
 * Container for all concrete implementations of the 
 * RuleBasedDirectedDialog.Condition interface
 * 
 * @author Rahul Akolkar
 */
public class Conditions {

    // Error messages (to be i18n'zed)
    private static final String ERR_BAD_EXPR = "Error evaluating " +
        "expression; received error message: \"{0}\"\n";
    
    // Logging
    private static Log log = LogFactory.getLog(Conditions.class);
    
    /** 
     * Java Object corresponding to an individual match condition
     * defined within a rule in the XML navigation rules structure
     * for the rule based directed dialog.
     * 
     * Target will be chosen if the lvalue matches rvalue.
     * 
     * Corresponds to &lt;match&gt; element in XML navigation rules.
     * 
     * @author Rahul Akolkar
     */
    public static class Match extends RuleBasedDirectedDialog.ConditionImpl {
        public Match() {
            operation = "equal-to";
        }
        public static Map getAttrPropMap() {
            Map attrPropMap = new HashMap();
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
     * 
     * @author Rahul Akolkar
     */
    public static class When extends RuleBasedDirectedDialog.ConditionImpl {
        public When() {
            operation = "equal-to";
            rvalue = "true";
        }
        public static Map getAttrPropMap() {
            Map attrPropMap = new HashMap();
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
     * 
     * @author Rahul Akolkar
     */
    public static class Unless extends RuleBasedDirectedDialog.ConditionImpl {
        public Unless() {
            operation = "equal-to";
            rvalue = "false";
        }
        public static Map getAttrPropMap() {
            Map attrPropMap = new HashMap();
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
     * 
     * @author Rahul Akolkar
     */
    public static class MatchAttribute extends 
        RuleBasedDirectedDialog.ConditionImpl {        

        private String element = null;
        private String name = null;
        public MatchAttribute() {
            operation = "equal-to";
        }
        public static Map getAttrPropMap() {
            Map attrPropMap = new HashMap();
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
                    element, Element.class,    lruCache, tempVars);
                attr = (String) DMUtils.proprietaryEval(groupTag, groupModel, 
                    name, String.class, lruCache, tempVars);
            } catch (Exception e) {
                MessageFormat msgFormat = new MessageFormat(ERR_BAD_EXPR);
                log.error(msgFormat.format(new Object[] {e.getMessage()}), e);
            }
            if (elem != null && elem.hasAttribute(attr)) {
                lvalue = elem.getAttribute(attr);
            }
        }
    }

}
