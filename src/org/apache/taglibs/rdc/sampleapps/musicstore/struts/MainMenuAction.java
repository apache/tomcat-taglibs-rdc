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
 */
package org.apache.taglibs.rdc.sampleapps.musicstore.struts;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.taglibs.rdc.sampleapps.musicstore.MusicStoreAppBean;
import org.apache.taglibs.rdc.sampleapps.musicstore.ProactiveHelp;
/**
 * Struts action class for initializing session data before initial
 * JSP [ mainmenu.jsp ] (music store sample app)
 * 
 * @author Rahul Akolkar
 * @author Jaroslav Gergic
 */
public class MainMenuAction extends Action {
  
  public MainMenuAction() {
    super();
  }

  public ActionForward execute(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws Exception {
    	
    HttpSession session = request.getSession();
    MusicStoreAppBean msBean = (MusicStoreAppBean) session.
		getAttribute(MusicStoreAppBean.SESSION_KEY);
	if (msBean == null) {
		String subscriptionId = session.getServletContext().
			getInitParameter("com.amazon.ecs.subscriptionId");
		if (subscriptionId == null || subscriptionId.trim().length() == 0) {
			System.err.println("Check value of context parameter " +
				"com.amazon.ecs.SubscriptionId in the web.xml file!");
		}
		msBean = new MusicStoreAppBean(subscriptionId);
		session.setAttribute(MusicStoreAppBean.SESSION_KEY, msBean);
	}
    ProactiveHelp ph = msBean.getProactiveHelp();

    if(ph == null) {
      ph = new ProactiveHelp();
      ResourceBundle rb = ResourceBundle.
      	getBundle("org.apache.taglibs.rdc.sampleapps.musicstore.resources.MusicHints", 
      			  Locale.US);
      ph.setHints(rb);
      ph.setThreshold(60);
      ph.setUsageWeighted(true);
      msBean.setProactiveHelp(ph);
    }
    if (ph.strike()) {
    	request.setAttribute("proactiveHelp_hint", ph.nextHint());
    }
	//cleanup RDC history
	session.removeAttribute("dialogMap");
    return mapping.findForward("OK");
  }

}
