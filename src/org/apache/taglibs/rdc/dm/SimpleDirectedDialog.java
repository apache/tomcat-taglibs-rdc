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

import java.util.Map;
import java.util.Iterator;
import java.util.List;
import java.io.IOException;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;

import org.apache.taglibs.rdc.core.BaseModel;
import org.apache.taglibs.rdc.core.GroupModel;
import org.apache.taglibs.rdc.core.Constants;


/**
 * <p>A dialog management strategy for the RDC group container which
 * implements a simple directed dialog (document order).</p>
 * 
 * @author Rahul Akolkar
 */
public class SimpleDirectedDialog extends DialogManagerImpl {

    /* Constructor */
    public SimpleDirectedDialog() {
        super();
    }

    /**
     * Collect the required information from the user
     *
     *
     */        
    public void collect(JspContext ctx, JspFragment bodyFragment) 
        throws JspException, IOException {
        
        GroupModel groupModel = (GroupModel) stateMap.get(groupTag.getId());
        Map modelMap = groupModel.getLocalMap();

        if (modelMap.get(Constants.STR_INIT_ONLY_FLAG) == Boolean.TRUE) {
            // DD starts off with all children in dormant state
            setStateChildren(modelMap, Constants.FSM_DORMANT);
            groupState = Constants.GRP_ALL_CHILDREN_DORMANT;
            groupModel.setState(Constants.GRP_STATE_RUNNING);
            modelMap.put(Constants.STR_INIT_ONLY_FLAG, Boolean.FALSE);
        } else {
            groupState = getGroupState(groupModel);
        }
    
        // Real work at the component level is done here
        if (groupModel.getState() == Constants.GRP_STATE_RUNNING) {
            do {
                dialogManagerDD(groupModel);
                if (bodyFragment != null) {
                    bodyFragment.invoke(null);
                }
            } while ((groupState = getGroupState(groupModel)) == 
                    Constants.GRP_SOME_CHILD_DORMANT);
        }        
    }
    

    /** 
     * This method does the directive dialog management and sets the state
     * to begin if they are in dormant state
     *
     * @param children Map that holds the id's
     */
    private void dialogManagerDD(GroupModel groupModel) {

        Map children = groupModel.getLocalMap();
        if (children == null) {
            return;
        }

        Iterator iter = children.keySet().iterator();
        String currentItem = null;
        int currentState;
        BaseModel model = null;
        
        while (iter.hasNext()) {
            currentItem = (String) iter.next();
            if (currentItem.equals(Constants.STR_INIT_ONLY_FLAG)) {
                continue;
            }
    
            model = (BaseModel) children.get(currentItem);
            currentState = model.getState();
            List activeChildren = groupModel.getActiveChildren();

            if (currentState != Constants.FSM_DONE &&
                currentState != Constants.GRP_STATE_DONE){
                // If the state is FSM_DORMANT, set it to FSM_INPUT
                if (currentState == Constants.FSM_DORMANT) {
                    if (activeChildren.size() > 0) {
                        activeChildren.remove(0);
                    }
                    activeChildren.add(model.getId());
                    if (model instanceof GroupModel) {
                        model.setState(Constants.GRP_STATE_RUNNING);
                    } else {
                        model.setState(Constants.FSM_INPUT);
                    }
                }
                // Else, let child execute till completion
                return;
            }
            // If state is done, move on to next child
        } // end while iterate over Map children

    } // end method dialogManagerDD()

    /** 
     * Returns the group state based on state of its children
     * 
     * One of these states will be returned
     * GRP_SOME_CHILD_RUNNING -> either of the children are in the execution 
     *             phase, if  the children are not done they are in running state
     *                    
     * GRP_SOME_CHILD_DORMANT -> either of the children are in input state 
     *          could be say "done->input->input"
     *  
     * GRP_ALL_CHILDREN_DONE -> if the child list is empty or if all the 
     *             children are done say "done->done->done"
     *
     * @param children Map that holds the id's
     * @return the child state
     */
    private int getGroupState(GroupModel groupModel) {
        Map children = groupModel.getLocalMap();
        if (children == null) {
            return Constants.GRP_ALL_CHILDREN_DONE;
        }

        Iterator iter = children.keySet().iterator();
        String current;
        int childState;

        //Loop while there are elements in the list
        while (iter.hasNext()) {
            current = (String) iter.next();
            if (current.equals(Constants.STR_INIT_ONLY_FLAG)) {
                continue;
            }

            childState = ((BaseModel) children.get(current)).getState();

            if (childState == Constants.FSM_DORMANT) {
                return Constants.GRP_SOME_CHILD_DORMANT;
            } else if (childState != Constants.FSM_DONE &&
                       childState != Constants.GRP_STATE_DONE) {
                return Constants.GRP_SOME_CHILD_RUNNING;
            }
        } // end while there elements in child List

        List activeChildren = groupModel.getActiveChildren();
        if (activeChildren.size() > 0) {
            activeChildren.remove(0);
        }
        return Constants.GRP_ALL_CHILDREN_DONE;
    }
    
}
