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
package org.apache.taglibs.rdc.core;

import java.io.IOException;
import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.taglibs.rdc.RDCUtils;

/**
 * <p>This is the implementation of the RDC helper tag group.
 * Group is a container with pluggable DM strategies.</p>
 *
 * @author Rahul Akolkar
 */
public class GroupTag extends SimpleTagSupport {

    // Error messages (to be i18n'zed)
    private static final String ERR_NO_STRATEGY = "<!-- GroupTag: " +
        "strategy attribute value - \"{0}\" contains whitespace only -->\n";
    private static final String ERR_NO_SUCH_STRATEGY = "<!-- GroupTag: No " +
        "strategy class found - \"{0}\" -->\n";
    private static final String ERR_NOT_A_DM_STRATEGY = "<!-- GroupTag: " +
        "Class - \"{0}\" does not implement the DialogManager interface -->\n";
    private static final String ERR_INSTANTIATING_STRATEGY = "<!-- GroupTag:" +
        " Error instantiating the specified strategy class - \"{0}\" -->\n";

    // Logging
    private static Log log = LogFactory.getLog(GroupTag.class);

    // CLASS PROPERTIES
    // The unique identifier associated with the group
    private String id;
    // Holds the submit url
    private String submit;
    // Holds the configuration details for the dialog management strategy
    // The interpretation of this attribute is upto the DM strategy plugged in
    private String config;
    // Indicates whether group level confirmation is requested
    // Overrides atom level confirmation, if specified
    private Boolean confirm;
    // The class name that implements the dialog management strategy
    private String strategy;
    // The DialogManager overseeing this group instance
    private DialogManager dm = null;

    /* Constructor */
    public GroupTag() {
        super();
        id = null;
        submit = null;
        config = null;
        strategy = null;
        confirm = Boolean.FALSE;
        dm = null;
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
     * @param confirm The group confirmation
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
     * Make JspContext visible to the DM strategy
     *
     * @return JspContext
     */
    public JspContext getJspContext() {
        return super.getJspContext();
    }

    /**
     * Has the state machine for the group
     *
     * Uses a pluggable dialog management strategy
     */
    public void doTag() throws JspException, IOException {

        if (dm == null) {
            PageContext ctx = (PageContext) getJspContext();
            try {
                dm = getStrategyInstance(ctx);
            } catch (ClassNotFoundException cnfe) {
                write(ctx, ERR_NO_SUCH_STRATEGY,
                    new Object[] {strategy}, cnfe);
            }
        }

        if (submit == null || submit.length() == 0) {
            submit = ((HttpServletRequest) ((PageContext) getJspContext()).
                getRequest()).getRequestURI();
        }

        dm.setGroupTag(this);

        if (!dm.initialize(getJspContext(), getJspBody())) {
            return;
        }

        dm.collect(getJspContext(), getJspBody());

        // Deprecated (since RDC 1.1)
        dm.confirm();

        dm.finish(getJspContext());

    }

    /**
     * Instantiate the strategy for this group instance.
     *
     * @param ctx The host JSP PageContext
     * @return DialogManager The DM strategy instance
     */
    private DialogManager getStrategyInstance(PageContext ctx)
    throws ClassNotFoundException {
        final Object[] errMsgArgs = new Object[] {strategy};
        if (RDCUtils.isStringEmpty(strategy)) {
            write(ctx, ERR_NO_STRATEGY, errMsgArgs, null);
            return null;
        }
        Class dmClass = null;
        DialogManager dialogManager = null;
        try {
            dmClass = Class.forName(strategy);
        } catch (ClassNotFoundException cnfe) {
            // Try the classloader for this thread
            dmClass = Class.forName(strategy, true, Thread.currentThread().
                        getContextClassLoader());
        } catch (Exception e) {
            // Log error
            log.error(e.getMessage(), e);
            write(ctx, ERR_INSTANTIATING_STRATEGY, errMsgArgs, null);
        }

        if (dmClass != null) {
            try {
                dialogManager = (DialogManager) dmClass.newInstance();
            } catch (ClassCastException cce) {
                write(ctx, ERR_NOT_A_DM_STRATEGY, errMsgArgs, cce);
            } catch (Exception e) {
                // Log error
                log.error(e.getMessage(), e);
                write(ctx, ERR_INSTANTIATING_STRATEGY, errMsgArgs, e);
            }
        }
        return dialogManager;
    }
    
    /**
     * Attempt to write given message to JSP writer.
     *
     * @param ctx The host JSP PageContext
     * @param msg The message to be sent to the JSP writer
     * @return boolean Outcome of write attempt
     */
    private void write(final PageContext ctx, final String errMsgName,
            final Object[] msgArgs, final Exception e) {
        MessageFormat msgFormat = new MessageFormat(errMsgName);
        String errMsg = msgFormat.format(msgArgs);
        // Log error
        if (e == null) {
            log.error(errMsg);
        } else {
            log.error(errMsg, e);
        }
        // Attempt to send message to client
        try {
            ctx.getOut().write(errMsg);
        } catch (IOException ioe) {
            log.error(ioe.getMessage(),ioe);
        }
    }

}
