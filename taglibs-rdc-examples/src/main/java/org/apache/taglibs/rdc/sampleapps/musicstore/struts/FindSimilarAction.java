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
package org.apache.taglibs.rdc.sampleapps.musicstore.struts;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.taglibs.rdc.sampleapps.musicstore.HTMLMenuLinks;
import org.apache.taglibs.rdc.sampleapps.musicstore.MusicStoreAppBean;
import org.apache.taglibs.rdc.sampleapps.musicstore.ws.MusicAlbum;
import org.apache.taglibs.rdc.sampleapps.musicstore.ws.MusicStore;

import org.apache.taglibs.rdc.SelectOne;

/**
 * Struts action class for finding albums similar to the currently chosen one
 * (music store sample app)
 * 
 * @author Rahul Akolkar
 * @author Jaroslav Gergic
 * @author Thomas Ling
 */
public class FindSimilarAction extends Action {

    public FindSimilarAction() {
        super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        HttpSession session = request.getSession();
        MusicStoreAppBean msBean = (MusicStoreAppBean) session
                .getAttribute(MusicStoreAppBean.SESSION_KEY);

        MusicAlbum album = msBean.getCurrentAlbum();
        
        if (album == null) {
            msBean.setErrorDescription("Current album not found in "
                    + "MusicStoreAppBean");
            return mapping.findForward("onerror");
        }
        
        MusicStore ms = msBean.getMusicStore();
        MusicAlbum albums[] = ms.getSimilarItems(album);
        //override the old Albums array in session
        msBean.setAlbums(albums);
        
        if (msBean.getChannel() == MusicStoreAppBean.GUI_APP) {
            // GUI Channel
            HTMLMenuLinks menuLinks = msBean.getMenuLinks();
            menuLinks.generateAlbumLinks(albums);
        } else {
            // Voice Channel
            //cleanup RDC history
            session.removeAttribute("dialogMap");
            SelectOne.Options options = new SelectOne.Options();
            for (int i = 0; i < albums.length; i++) {
                options.add(albums[i].getASIN(), albums[i].getTitle()
                        .replaceAll("&", "and"));
            }
            msBean.setOptions(options);
            msBean.setChoice(" items similar to " + album.getTitle());
        }
        return mapping.findForward("OK");
    }
}