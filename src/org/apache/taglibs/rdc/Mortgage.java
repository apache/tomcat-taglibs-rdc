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

import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.PageContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.apache.xpath.XPathAPI;
import org.apache.xpath.objects.XObject;

import org.apache.taglibs.rdc.core.ComponentModel;
import org.apache.taglibs.rdc.core.Constants;
import org.apache.taglibs.rdc.MortgageData;

/**
 * DataModel for Mortgage Composite RDC
 *
 * @author Sindhu Unnikrishnan
 * @author Rahul Akolkar
 */
public class Mortgage extends ComponentModel
{

	public Mortgage(){
		super();
	}

	/** 
	  * Stores the id and file attributes from the config xml to the 
	  * LinkedHashMap
	  *
	  * @param configMap Map that holds the id's and attributes
	  * @param uri the config URI of the RDC
	  */
	public void configHandler(JspContext ctx, String uri) {

		String uriPath = uri;
		DocumentBuilder builder = null;
		Document doc = null;
		XObject xPathResult = null;
		NodeList nodelist = null;
		Node node = null;
		URI absTest = null;
		
		try {
			absTest = new URI(uriPath);
		} catch (URISyntaxException uriexp) {
			uriexp.printStackTrace();
		}
		if (!absTest.isAbsolute()) {
			uriPath = ((PageContext) ctx).getServletContext()
					.getRealPath(uriPath);
		}

		try {
			builder = DocumentBuilderFactory.newInstance().
				newDocumentBuilder();
			doc = builder.parse(uriPath);
			xPathResult =
				XPathAPI.eval(doc.getDocumentElement(), 
					Constants.XPATH_COMPONENT_CONFIG);
			nodelist = xPathResult.nodelist();
		} catch (Exception e) {
			e.printStackTrace();
		}

		XObject attrId = null, attrFile = null;
		for (int i = 0; i < nodelist.getLength(); i++) {
			node = nodelist.item(i);
			if (node == null) {
				continue;
			}
			try {
				attrId = XPathAPI.eval(node, Constants.XPATH_ATTR_ID);
				attrFile = XPathAPI.eval(node, Constants.XPATH_ATTR_FILE);
			} catch (TransformerException te) {
				te.printStackTrace();
			}
			configMap.put(attrId.toString(), attrFile.toString());
		}
	}

}
