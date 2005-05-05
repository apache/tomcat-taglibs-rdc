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
package org.apache.taglibs.rdc;

import java.io.IOException;
import java.io.StringWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
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
		try {
			NodeList nodesOfInterest = XPathAPI.selectNodeList(configuration.
				getDocumentElement(), elementXPath);
			XMLSerializer output = new XMLSerializer(out, format);
			for (int i = 0; i < nodesOfInterest.getLength(); i++) {
				output.serialize((Element) nodesOfInterest.item(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return out.toString();
	}
}

