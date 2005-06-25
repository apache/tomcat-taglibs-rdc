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

import org.apache.taglibs.rdc.core.Grammar;
import org.apache.taglibs.rdc.sampleapps.musicstore.MusicStoreAppBean;
import org.apache.taglibs.rdc.sampleapps.musicstore.ProactiveHelp;
import org.apache.taglibs.rdc.sampleapps.musicstore.HTMLMenuLinks;

/**
 * Struts action class for initializing session data before initial
 * JSP [ mainmenu.jsp ] (music store sample app)
 * 
 * @author Rahul Akolkar
 * @author Jaroslav Gergic
 * @author Thomas Ling
 */
public class MainMenuAction extends Action {

  /**
   * User Agent strings 
   */
  private static final String USERAGENT = "User-Agent";
  private static final String MOZILLA   = "Mozilla";
  private static final String IBMVXML20 = "IBM VoiceXML 2.0";

  public MainMenuAction() {
    super();
  }

  public ActionForward execute(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws Exception {
    	
    HttpSession session = request.getSession();
    MusicStoreAppBean msBean = (MusicStoreAppBean)
      session.getAttribute(MusicStoreAppBean.SESSION_KEY);

    if (msBean == null) {
		  String subscriptionId = session.getServletContext().
			getInitParameter("com.amazon.ecs.subscriptionId");
		  msBean = new MusicStoreAppBean(subscriptionId);
		  session.setAttribute(MusicStoreAppBean.SESSION_KEY, msBean);
      
      // Initialize channel information
      String userAgent = request.getHeader(USERAGENT);
      if (userAgent.indexOf(MOZILLA) >= 0) {
        if (request.getParameter("VDEBUG") == null) {
          msBean.setChannel(MusicStoreAppBean.GUI_APP);  
        } else {
          // if VDEBUG is set, output voice page instead     
          msBean.setChannel(MusicStoreAppBean.VOICE_DBG);
        }
      } else {
        msBean.setChannel(MusicStoreAppBean.VOICE_APP);
      }
	  }
    
    if (msBean.getChannel() == MusicStoreAppBean.GUI_APP) {
      // GUI Channel
      HTMLMenuLinks menuLinks = msBean.getMenuLinks();

      if (menuLinks == null) {
        String baseURI = "http://" + request.getServerName();
        int port = request.getServerPort();
        if (80 != port && 0 != port && 443 != port) {
          baseURI += ":" + Integer.toString(port, 10);
        }
        baseURI += request.getContextPath();
        menuLinks = new HTMLMenuLinks(baseURI);
        msBean.setMenuLinks(menuLinks);
      }

      String genre = request.getParameter("genre");
      if (genre == null) {
        if (request.getParameter("browseMusic") == null) {
          menuLinks.generateCategoryLinks(
                  HTMLMenuLinks.DEFAULT_GENRE, true);
        } else {
          menuLinks.generateGenreLinks();
        }
      } else {
        menuLinks.generateCategoryLinks(genre, false);
      }
    } else {
      // Voice Channel 
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
	    //populate amazon menu template grammars
	    Grammar[] grammarList = new Grammar[] {
	      new Grammar("../../grammar/musicstore-app/genre_or_category.grxml", 
                    Boolean.FALSE, Boolean.FALSE, "mainGrammar"),
	      new Grammar("../../grammar/musicstore-app/genre_or_category.grxml#category_only", 
                    Boolean.FALSE, Boolean.FALSE, "categoryGrammar")
	    };
	    request.setAttribute("grammarList", grammarList);
    }
    return mapping.findForward("OK");
  }

}
