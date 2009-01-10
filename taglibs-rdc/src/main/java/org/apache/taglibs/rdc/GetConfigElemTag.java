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
import java.io.StringWriter;
import java.text.MessageFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * <p>This tag is used to retrieve an element from the config.</p>
 * 
 * @author Rahul Akolkar
 */
public class GetConfigElemTag
    extends SimpleTagSupport {
    
    // Error messages (to be i18n'zed)
    private static final String ERR_NULL_DOCUMENT = "Attempting to obtain " +
        "element \"{0}\" from null Document, check URLs specified via " +
        "config attributes of RDCs or optionList attributes of select1 RDCs";
    private static final String ERR_PROCESS_XPATH = "Failed to obtain" +
        " element from configuration file with XPath \"{0}\"";
    
    // Logging
    private static Log log = LogFactory.getLog(GetConfigElemTag.class);
    
    /**
     * Describe xml: Config file as a parsed document
     */
    private Document xml;
    
    /**
     * Describe locator: The XPath expression
     */
    private String locator;
    
    /**
     * Set the xml value.
     *
     * @param xml The parsed config document
     */
    public final void setXml(final Document xml) {
        this.xml = xml;
    }
    
    /**
     * Set the locator value.
     *
     * @param locator The XPath expression
     */
    public final void setLocator(final String locator) {
        this.locator = locator;
    }

    /**
     * Get the element specified by the locator and render to JspWriter
     *
     */
    public void doTag()
        throws IOException, JspException {

        getJspContext().getOut().write(render(xml, locator));    

    }
    
    private static OutputFormat format;
    static {
        format = new OutputFormat();
        format.setOmitXMLDeclaration(true);
    }

    private static String render(Document configuration, String elementXPath) {
        StringWriter out = new StringWriter();
        if (configuration == null) {
            MessageFormat msgFormat = new MessageFormat(ERR_NULL_DOCUMENT);
            log.error(msgFormat.format(new Object[] {elementXPath}));
            return out.toString();
        }
        try {
            NodeList nodesOfInterest = XPathAPI.selectNodeList(configuration.
                getDocumentElement(), elementXPath);
            XMLSerializer output = new XMLSerializer(out, format);
            for (int i = 0; i < nodesOfInterest.getLength(); i++) {
                output.serialize((Element) nodesOfInterest.item(i));
            }
        } catch (Exception e) {
            MessageFormat msgFormat = new MessageFormat(ERR_PROCESS_XPATH);
            log.warn(msgFormat.format(new Object[] {elementXPath}), e);
        }        
        return out.toString();
    }
}

