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
package org.apache.taglibs.rdc.core;

import java.io.IOException;
import java.io.StringWriter;
import java.text.MessageFormat;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.el.VariableResolver;
import org.apache.commons.el.ExpressionEvaluatorImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>Implements the tag 
 * &lt;rdc:expand&gt;Hello from #{model.name}&lt;/rdc:expand&gt;.
 * This tag can be used to evaluate text containing expressions
 * that use the custom syntax "#{...}". It is useful in
 * authoring prompt templates  that refer to run-time values.</p>
 * 
 * @author <a href="mailto:tvraman@almaden.ibm.com">T. V. Raman</a>
 * @author Rahul Akolkar
 * @version 1.0
 */

public class ExpandTag
    extends SimpleTagSupport {
    
	// Error messages (to be i18n'zed)
	private static final String ERR_NO_BODY = "'rdc:expand' used without " +
		"a body";
	private static final String ERR_BAD_EL_EXPR = "<!-- Error evaluating " +
		" EL expression: \"{0}\" -->\n";
	
	// Logging
	private static Log log = LogFactory.getLog(ExpandTag.class);
	
    /* 
     * Rahul - 9/13/04
     * Do not use getJspContext().getExpressionEvaluator() --
     * which offers no guarantee whether multiple expressions
     * occuring in String "expression" will be evaluated
     */	
	private static final ExpressionEvaluatorImpl exprEvaluator = 
		new ExpressionEvaluatorImpl();
	
    /**
     * Captures the result of invoking body,
     * Replaces occurrences of #{ with ${,
     * and then evaluates the result using el.
     * The result of el evaluation is written to the JSP output stream.
     * in case of an el evaluation  error,
     * we produce appropriate error messages into the JSP output stream.
     * @exception java.io.IOException
     * @exception JspException
     * @exception JspTagException   
     */
    public void doTag()
        throws IOException, JspException, JspTagException   {
        JspFragment body = getJspBody();
        if (body == null) {
	    	throw new JspTagException(ERR_NO_BODY);
		}
		StringWriter bodyExpansion = new StringWriter();
        body.invoke(bodyExpansion);
        String expansion = bodyExpansion.getBuffer().toString();
        String expression = expansion.replaceAll("#\\{", "\\$\\{");
        
        PageContext pageContext = (PageContext) getJspContext();        
        VariableResolver varResolver = getJspContext().getVariableResolver();
        JspWriter out = pageContext.getOut();
        try {
        	// Rahul - 9/13/04 - EL functions are not supported.
        	// Since this is in the body of a <vxml:prompt> element, the 
        	// result needs to be a String
            String result = (String) exprEvaluator.evaluate(expression, 
            	java.lang.String.class, varResolver, null);
            out.write(result);           
        } catch (javax.servlet.jsp.el.ELException e) {
        	MessageFormat msgFormat = new MessageFormat(ERR_BAD_EL_EXPR);
        	String errMsg = msgFormat.format(new Object[] {expression});
        	// Log error and send a comment to client
			log.error(errMsg);
            out.write(errMsg);
        } // end of try-catch
    }
}
