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
package org.apache.taglibs.rdc.dm;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.el.VariableResolver;
import org.apache.commons.el.ExpressionEvaluatorImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.taglibs.rdc.RDCUtils;
import org.apache.taglibs.rdc.core.BaseModel;
import org.apache.taglibs.rdc.core.ComponentModel;
import org.apache.taglibs.rdc.core.Constants;
import org.apache.taglibs.rdc.core.GroupModel;
import org.apache.taglibs.rdc.core.GroupTag;
/**
 * Utility methods for the dm package
 * 
 * @author Rahul Akolkar
 */
public class DMUtils {

    // ******************
    // PRIVATE CONSTANTS
    // ******************
    private static final String NESTED_DATAMODEL_SEPARATOR = "|";
    
    private static final String FILLER = ".*";
    private static final String ID_TOK = "\\$((\\w|_|\\" +
        NESTED_DATAMODEL_SEPARATOR + ")+)";
    private static final String START_EXPR = "\\#\\{";
    private static final String FILLER_NO_END_EXPR = "[^\\}]*";
    
    private static final Pattern PATTERN_ID = 
        Pattern.compile(FILLER + ID_TOK + FILLER);
    private static final int ID_GROUP = 1;
    private static final Pattern PATTERN_OPEN_EXPR = 
        Pattern.compile(FILLER + START_EXPR + FILLER_NO_END_EXPR);
    
    // Error messages (to be i18n'zed)
    private static final String ERR_NO_SUCH_MODEL = "No model for " +
        "\"{0}\" found as child of \"{1}\"";
    private static final String ERR_NULL_EXPR = "EL error, \"{0}\"" + 
        "; JspContext or expression or return type is null";
    private static final String ERR_BAD_EXPR = "EL error while " +
        "evaluating expression: \"{0}\"";
    
    // Logging
    private static Log log = LogFactory.getLog(DMUtils.class);

    // ******************
    // PUBLIC METHODS
    // ******************            
    /** 
     * Return true if the given component or container is done.
     *
     * @param BaseModel
     */    
    public static boolean isChildDone(BaseModel model) {
        if (model instanceof GroupModel) {
            GroupModel grpModel = (GroupModel) model;
            if (grpModel.getGroupConfirm() != null) {
                if (grpModel.getState() == Constants.GRP_STATE_DONE) {
                    return true;
                }
            } else {
                if (grpModel.getActiveChildren().size() == 0) {
                    grpModel.setState(Constants.GRP_STATE_DONE);
                    return true;
                }
            }
            return false;
        } else if (model instanceof ComponentModel && 
                   model.getState() == Constants.FSM_DONE) {
            return true;
        } else if (model instanceof BaseModel && 
                model.getState() == Constants.FSM_DONE) {
            return true;
        }
        return false;            
    }

    // ******************
    // PACKAGE METHODS
    // ******************
    /** 
     * Activate child so it takes over the dialog from the next turn
     */
    static int invokeDormantChild(Map children, List activeChildren,
        String id) {
        if (id == null) {
            log.info("Null ID of identified target");
            return Constants.GRP_ALL_CHILDREN_DONE;
        }
        BaseModel model = (BaseModel) children.get(id);
        if (model != null) {
            if (model.getState() == Constants.FSM_DORMANT) {
                activeChildren.add(id);
                if (model instanceof BaseModel) {
                    model.setState(Constants.FSM_INPUT);
                } else if (model instanceof GroupModel) {
                    model.setState(Constants.GRP_STATE_RUNNING);
                } else if (model instanceof ComponentModel) {
                    model.setState(Constants.FSM_INPUT);
                }
            }
            return Constants.GRP_SOME_CHILD_RUNNING;
        } else {
            log.info("ID \"" + id + "\" of target in group dialog " +
                "configuration is invalid");
            return Constants.GRP_ALL_CHILDREN_DONE;
        }
    }

    /** 
     * Two pass evaluation:
     * 1) Substitute value-of RDCs
     * 2) Evaluate using the EL evaluator
     *
     * @param String to be evaluated
     */    
    static Object proprietaryEval(GroupTag groupTag, GroupModel groupModel,
        String expr, Class retClass, LinkedHashMap lruCache,
        List tempVars) {
            
        Matcher matcher = PATTERN_ID.matcher(expr);
        while (matcher.matches()) {
            String matched = matcher.group(ID_GROUP);
            int start = expr.lastIndexOf(matched) - 1; // -1 for $
            int end = start + 1 + matched.length();
            String startStr = "", endStr = "";
            if (start > 0) {
                startStr = expr.substring(0, start);
            }
            if (end < expr.length()) {
                endStr = expr.substring(end);
            }
            Object[] exprData = null;
            Object rdcValue = null;
            String pageVar = null;
            if (lruCache != null && lruCache.containsKey(matched)) {
                exprData = (Object[])lruCache.get(matched);
                rdcValue = exprData[0];
                pageVar = (String)exprData[1];
            } 
            if (exprData == null) {
                rdcValue = valueOfIDExpr(groupModel, matched);
                pageVar = RDCUtils.getNextRDCVarName();
                tempVars.add(pageVar);
                addToCache(lruCache, matched, rdcValue, pageVar);
            }
            groupTag.getJspContext().setAttribute(pageVar, rdcValue);
            expr = wrapAsNeeded(startStr, pageVar, endStr);
            matcher = PATTERN_ID.matcher(expr);
        }
        return valueOfELExpr(groupTag.getJspContext(), expr, retClass);
    }


    // ******************
    // PRIVATE METHODS
    // ******************
    /** 
     * LRU Cache implementation using LinkedHashMap 
     *
     */
    private static void addToCache(LinkedHashMap cache,
        String key, Object value, String var) {
        
        final int MAX_CACHE_SIZE = 50;
        if (cache.size() == MAX_CACHE_SIZE) {
            cache.remove(cache.keySet().iterator().next());
        }
        cache.put(key, new Object[] { value, var });
    }

    /** 
     * Make sure the temporary page context variable gets evaluated in an
     * EL expression
     *
     */
    private static String wrapAsNeeded(String startStr, String pageVar,
        String endStr) {
        Matcher openExpr = PATTERN_OPEN_EXPR.matcher(startStr);
        if (openExpr.matches()) {
            return startStr + pageVar + endStr;
        }
        return startStr + "#{" + pageVar + "}" + endStr;
    }

    /** 
     * Obtain the value of the given ID with respect to the given GroupModel.
     *
     * @param GroupModel The group model which holds a child of the given ID
     * @param String The ID whose value is required
     */
    private static Object valueOfIDExpr(GroupModel groupModel, String id) {    
        Object value = null;
        BaseModel iteratorModel = groupModel;
        if (id == null) {
            return null;
        }
        StringTokenizer idTokenizer = new StringTokenizer(id, 
            NESTED_DATAMODEL_SEPARATOR);
        BaseModel model = null;
        while (idTokenizer.hasMoreTokens()) {
            String idTok = idTokenizer.nextToken();
            if (iteratorModel instanceof GroupModel) {
                model = (BaseModel) ((GroupModel)iteratorModel).
                    getLocalMap().get(idTok);
            } else if (iteratorModel instanceof ComponentModel) {
                model = (BaseModel) ((ComponentModel)iteratorModel).
                    getLocalMap().get(idTok);            
            }
            if (model == null) {
                // Invalid child ID specified in XML navigation rules
                MessageFormat msgFormat = new MessageFormat(ERR_NO_SUCH_MODEL);
                log.warn(msgFormat.format(new Object[] {idTok, 
                    iteratorModel.getId()}));
                return "$" + id;
            }
            iteratorModel = model;
        }
        value = model.getValue();
        return value;
    }

    /** 
     * Obtain the value of the given JSP 2.0 EL expression in the group
     * tag's PageContext.
     *
     * @param JspContext The context in which the expression should be
     *                   evaluated
     * @param String The JSP 2.0  EL expression
     * @param Class retType The expected return type
     */        
    private static Object valueOfELExpr(JspContext ctx, String expr_,
        Class retType) {
        
        if (ctx == null || expr_ == null || retType == null) {
            MessageFormat msgFormat = new MessageFormat(ERR_NULL_EXPR);
            log.warn(msgFormat.format(new Object[] {expr_}));
            return null;
        }
        String expr = expr_.replaceAll("\\#\\{", "\\$\\{");
        Object value = null;
        if (retType == null) {
            retType = java.lang.String.class;
        }
        ExpressionEvaluatorImpl exprEvaluator = new ExpressionEvaluatorImpl();
        VariableResolver varResolver = ctx.getVariableResolver();
        try {
            value = exprEvaluator.evaluate(expr, retType, varResolver, null);
        } catch (javax.servlet.jsp.el.ELException e) {
            MessageFormat msgFormat = new MessageFormat(ERR_BAD_EXPR);
            log.warn(msgFormat.format(new Object[] {e.getMessage()}), e);
            return null;
        } // end of try-catch
        return value;        
    }
}
