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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.taglibs.rdc.sampleapps.musicstore.AmazonMenuTemplate;
import org.apache.taglibs.rdc.sampleapps.musicstore.MusicStoreAppBean;
import org.apache.taglibs.rdc.sampleapps.musicstore.ws.MusicAlbum;
import org.apache.taglibs.rdc.sampleapps.musicstore.ws.MusicStore;
import org.apache.taglibs.rdc.sampleapps.musicstore.HTMLMenuLinks;

import org.apache.taglibs.rdc.SelectOne;

/**
 * Struts action class for the album filter (music store sample app)
 * 
 * @author Rahul Akolkar
 * @author Jaroslav Gergic
 * @author Thomas Ling
 * 
 * @see AlbumFilterForm
 */
public class AlbumFilterAction extends Action {

    public static final int CATEGORY_TOP_SELLERS = 57687842;

    public static final int CATEGORY_NEW_RELEASES = 38145244;

    public static final int CATEGORY_FEATURED_ITEMS = 52536577;

    private static Log log = LogFactory.getLog(AlbumFilterAction.class);

    public AlbumFilterAction() {
        super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        AlbumFilterForm afForm = (AlbumFilterForm) form;
        MusicAlbum albums[] = null;
        HttpSession session = request.getSession();
        MusicStoreAppBean msBean = (MusicStoreAppBean) session
                .getAttribute(MusicStoreAppBean.SESSION_KEY);
        
        try {
            AmazonMenuTemplate.AmazonMenuResult selection = afForm.getChoice();
            if (selection == null) {
                selection = msBean.getMenuResult();
            } else {
                msBean.setMenuResult(selection);
            }
            int genre = Integer.parseInt(selection.getGenre());
            int catgr = Integer.parseInt(selection.getCategory());
            MusicStore ms = msBean.getMusicStore();
            switch (catgr) {
            case CATEGORY_FEATURED_ITEMS:
                albums = ms.getFeaturedItems(genre);
            case CATEGORY_NEW_RELEASES:
                albums = ms.getNewReleases(genre);
            case CATEGORY_TOP_SELLERS:
                albums = ms.getTopSellers(genre);
                break;
            default:
                throw new IllegalArgumentException("Illegal category: " + 
                                                   catgr);
            }

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
                msBean.setChoice(selection.getReadableValue());
            }
            msBean.setAlbums(albums);
            return mapping.findForward("OK");
        } catch (Exception e) {
            log.error("An exception caught while applying parameters.", e);
            msBean.setErrorDescription(e.getMessage());
            return mapping.findForward("invalidParams");
        }
    }
}