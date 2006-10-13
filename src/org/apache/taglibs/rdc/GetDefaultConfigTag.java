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
package org.apache.taglibs.rdc;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.apache.xerces.parsers.DOMParser;

import org.apache.taglibs.rdc.core.BaseModel;
import org.apache.taglibs.rdc.core.Constants;

/**
 * <p>This tag is used to retrieve the default config for a component
 * from a jar file, parse it with validation, and set the component's
 * BaseModel's configuration.</p>
 * 
 * @author Rahul Akolkar
 */
public class GetDefaultConfigTag
    extends SimpleTagSupport {

    /**
     * Describe name: Name of Jar Entry
     */
    private String name;
    
    /**
     * Describe model: BaseModel of component that needs its default config
     */
    private BaseModel model;
    
    /**
     * Get the <code>Name</code> value.
     *
     * @return a <code>String</code> value
     */
    public final String getName() {
        return name;
    }
    
    /**
     * Set the <code>Name</code> value.
     *
     * @param newName The new Name value.
     */
    public final void setName(final String newName) {
        this.name = newName;
    }
    
    /**
     * Get the <code>BaseModel</code> value.
     *
     * @return a <code>BaseModel</code> value
     */
    public final BaseModel getModel() {
        return model;
    }
    
    /**
     * Set the <code>BaseModel</code> value.
     *
     * @param model The new model value.
     */
    public final void setModel(final BaseModel model) {
        this.model = model;
    }

    public void doTag()
        throws IOException, JspException {

        final String jar = ((PageContext) getJspContext()).
            getServletContext().getRealPath(Constants.RDC_JAR);            
        InputSource inputSrc = RDCUtils.extract(jar, name);
 
        DOMParser dp = new DOMParser();
        try {
            dp.parse(inputSrc);
        } catch (SAXException sx) {
            throw new IOException("Cannot parse the default config: " + name);
        } // end of try-catch
        Document d = dp.getDocument();
        if (d == null) {
            throw new IOException("Could not get document from located Jar entry.");
        } // end of if (d == null)

        if (model == null) {
            throw new JspException("Null BaseModel");
        } // end of if (model == null)
        model.setConfiguration(d);

    }
}
