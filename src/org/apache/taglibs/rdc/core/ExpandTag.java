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
//$Id$
package org.apache.taglibs.rdc.core;

import java.io.IOException;
import java.io.StringWriter;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import org.apache.jasper.runtime.PageContextImpl;


/**
 * <p>Implements tag <rdc:expand>Hello from #{model.name}</rdc:expand>
 * This tag can be used to evaluate text containing expressions
 * that use the custom syntax "#{...}". It is useful in
 * authoring prompt templates  that refer to run-time values.</p>
 * 
 * @author <a href="mailto:tvraman@almaden.ibm.com">T. V. Raman</a>
 * @version 1.0
 */

public class ExpandTag
    extends SimpleTagSupport {
    
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
	    	throw new JspTagException("'rdc:expand' used without a body");
		}
		StringWriter bodyExpansion = new StringWriter();
        body.invoke(bodyExpansion);
        String expansion =bodyExpansion.getBuffer().toString();
        String expression = expansion.replaceAll("#\\{", "\\$\\{");
        PageContext pageContext = (PageContext) getJspContext(  );
        JspWriter out = pageContext.getOut();
        try {
            String result = (String)
                PageContextImpl.proprietaryEvaluate(expression,
                                                    java.lang.String.class,
                                                    pageContext, null, false);
            out.write(result);           
        } catch (javax.servlet.jsp.el.ELException e) {
            out.write("<!-- Error evaluating expression: "
                      +expression
                      +"-->\n");
        } // end of try-catch
    }
}
