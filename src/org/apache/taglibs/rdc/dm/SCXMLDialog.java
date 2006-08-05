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

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.commons.scxml.Context;
import org.apache.commons.scxml.Evaluator;
import org.apache.commons.scxml.EventDispatcher;
import org.apache.commons.scxml.SCXMLExecutor;
import org.apache.commons.scxml.Status;
import org.apache.commons.scxml.TriggerEvent;
import org.apache.commons.scxml.env.SimpleDispatcher;
import org.apache.commons.scxml.env.SimpleErrorReporter;
import org.apache.commons.scxml.env.SimpleSCXMLListener;
import org.apache.commons.scxml.env.jsp.ELEvaluator;
import org.apache.commons.scxml.env.jsp.RootContext;
import org.apache.commons.scxml.env.servlet.ServletContextResolver;
import org.apache.commons.scxml.io.SCXMLDigester;
import org.apache.commons.scxml.model.ModelException;
import org.apache.commons.scxml.model.SCXML;
import org.apache.commons.scxml.model.State;

import org.apache.taglibs.rdc.core.BaseModel;
import org.apache.taglibs.rdc.core.Constants;
import org.apache.taglibs.rdc.core.GroupModel;
import org.apache.taglibs.rdc.core.GroupTag;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * A dialog management strategy for the RDC group container which uses
 * a SCXML configuration file to define the dialog for each &lt;group&gt;
 * instance.
 * 
 * SCXML, or the "State Chart eXtensible Markup Language", provides a 
 * generic state-machine based execution environment based on CCXML and 
 * Harel State Tables.
 * 
 * The URI of the SCXML document is the config attribute of the
 * &lt;group&gt; tag.
 * 
 * @author Rahul Akolkar
 * @author Jaroslav Gergic
 */
public class SCXMLDialog extends DialogManagerImpl {
    
    private static final String ERR_DIGESTER_FAIL = "<!-- Error parsing " +
        "SCXML document for group: \"{0}\", with message: \"{1}\" -->\n";
    
    // Logging
    private static Log log = LogFactory.getLog(SCXMLDialog.class);
    
    /**
     * The SCXML engine that will execute the SCXML document specified
     * in the group configuration.
     */
    private SCXMLExecutor exec;
    
    public SCXMLDialog() {
        super();
    }
    
    public boolean initialize(JspContext ctx, JspFragment bodyFragment) 
    throws JspException, IOException {
        
        boolean retVal = super.initialize(ctx, bodyFragment);
        GroupModel groupModel = (GroupModel) stateMap.get(groupTag.getId());
        
        if (groupModel.getInstanceData() != null) {
            exec = (SCXMLExecutor) groupModel.getInstanceData();
            return retVal;
        }

        SCXML scxml = null;
        ServletContext sc = ((PageContext) ctx).getServletContext();
        try {
            scxml = SCXMLDigester.digest(sc.getRealPath(groupTag.getConfig()),
                new SCXMLErrorHandler(), new ServletContextResolver(sc));
        } catch (Exception e) {
            MessageFormat msgFormat = new MessageFormat(ERR_DIGESTER_FAIL);
            String errMsg = msgFormat.format(new Object[] {groupTag.
                getConfig(), e.getMessage()});
            log.error(errMsg, e);
            ((PageContext) ctx).getOut().write(errMsg);            
        }

        if (scxml == null) {
            retVal = false;
            ((PageContext) ctx).getOut().write("<!-- SCXMLDialog" +
                ": Error parsing SCXML:" + groupTag.getConfig()+
                "-->\n");
        } else {
            // TODO - Remove debugging statement and else above
            //System.out.println(SCXMLDigester.serializeSCXML(scxml));
        }

        Evaluator engine = new ELEvaluator();
        EventDispatcher ed = new SimpleDispatcher();
        Context rootCtx = new RootContext(ctx);
        try {
            exec = new SCXMLExecutor(engine, ed, new SimpleErrorReporter());
            exec.setRootContext(rootCtx);
            exec.setStateMachine(scxml);
            exec.addListener(scxml, new SimpleSCXMLListener());
            exec.go();
        } catch (ModelException me) {
            retVal = false;
            ((PageContext) ctx).getOut().write("<!-- SCXMLDialog" +
                ": Model Exception in:" + groupTag.getConfig()+
                ", root cause: " + me.getMessage() + "-->\n");
        }
        groupModel.setInstanceData(exec);
        
        return retVal;
    }
    
    /**
     * Collect the required information based on the SCXML config
     */    
    public void collect(JspContext ctx, JspFragment bodyFragment) 
    throws JspException, IOException {
        
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
            Status s;
            do {
                s = exec.getCurrentStatus();
                dialogManager(groupTag);
                if (bodyFragment != null) {
                    bodyFragment.invoke(null);
                }
            } while (renderNext(groupModel, s));
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

        List activeChildren = groupModel.getActiveChildren();
        List eventList = new ArrayList();
        Set currentStates = exec.getCurrentStatus().getStates();
        if (currentStates != null && currentStates.size() > 0) {
            Iterator iter = currentStates.iterator();
            while (iter.hasNext()) {
                String id = ((State) iter.next()).getId();
                BaseModel model = (BaseModel) children.get(id);
                groupState = DMUtils.invokeDormantChild(children, 
                    activeChildren, id);
                if (!DMUtils.isChildDone(model)) {
                    continue;
                } else {
                    activeChildren.remove(model.getId());
                    eventList.add(model.getId() + ".done");
                }
            }
        } else {
            if (activeChildren.size() > 0) {
                activeChildren.clear();
            }
            groupState = Constants.GRP_ALL_CHILDREN_DONE;
            return;
        }
        
        if (eventList.size() == 0) {
            return;
        } else {
            //fire events one at a time
            for (int i = 0; i < eventList.size(); i++) {
                TriggerEvent evts[] = new TriggerEvent[] { 
                    new TriggerEvent((String) eventList.get(i), 
                    TriggerEvent.SIGNAL_EVENT, null) };
                log.trace("Triggering event " + eventList.get(i));
                try {
                    exec.triggerEvents(evts);
                } catch (ModelException me) {
                    log.error(me.getMessage(), me);
                }
            }
        }

    } // end method dialogManager()
    
    /** 
     * Render new states due to events triggered in last dialogManager()
     * invocation if:
     * a) All currently active child are done
     * and
     * b) The current executor status reached is not final
     */
    private boolean renderNext(GroupModel groupModel, Status s) {
        Map children = groupModel.getLocalMap();
        List activeChildren = groupModel.getActiveChildren();
        int sz = activeChildren.size();
        if (sz > 0) {
            for (int i = 0; i < sz; i++) {
                BaseModel model = (BaseModel)children.
                    get(activeChildren.get(i));
                if (!DMUtils.isChildDone(model)) {
                    return false;
                }
            }
        } else {
            if (s.isFinal()) {
                if (activeChildren.size() > 0) {
                    activeChildren.clear();
                }
                groupState = Constants.GRP_ALL_CHILDREN_DONE;
                log.info("A final configuration reached");
                return false;
            }
        }
        return true;
    }
    
    /** 
     * Custom error handler to communicate parsing errors in the
     * XML navigation rules to the client.
     */    
    private class SCXMLErrorHandler implements ErrorHandler {
        
        private static final String MSG_PREFIX = "<!-- " +
            "SCXMLDialog :";
        private static final String MSG_POSTFIX = " Correct the SCXML. " +
            "-->\n";
        
        public void error(SAXParseException s) {
            try {
                ((PageContext) groupTag.getJspContext()).getOut().
                write(MSG_PREFIX + groupTag.getConfig() + " Error - " +
                s.getMessage() + MSG_POSTFIX);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        
        public void fatalError(SAXParseException s) {
            try {
                ((PageContext) groupTag.getJspContext()).getOut().
                write(MSG_PREFIX + groupTag.getConfig() + " Fatal Error - " +
                s.getMessage() + MSG_POSTFIX);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }            
        }
        
        public void warning(SAXParseException s) {
            try {
                ((PageContext) groupTag.getJspContext()).getOut().
                write(MSG_PREFIX + groupTag.getConfig() + " Warning - " +
                s.getMessage() + MSG_POSTFIX);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }            
        }
    }
    
}
