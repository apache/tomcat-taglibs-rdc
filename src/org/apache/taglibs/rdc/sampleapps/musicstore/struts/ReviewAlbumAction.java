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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.taglibs.rdc.sampleapps.musicstore.MusicStoreAppBean;
import org.apache.taglibs.rdc.sampleapps.musicstore.ws.MusicAlbum;

/**
 * Struts action class for finding albums similar to the currently
 * chosen one (music store sample app)
 * 
 * @author Rahul Akolkar 
 * @author Jaroslav Gergic
 */
public class ReviewAlbumAction extends Action {

    public ReviewAlbumAction() {
        super();
    }

    public ActionForward execute(ActionMapping mapping,    ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
            
        IndividualAlbumForm iaForm = (IndividualAlbumForm) form;
        String asin = iaForm.getAsin();
        HttpSession session = request.getSession();
        MusicStoreAppBean msBean = (MusicStoreAppBean) session.
            getAttribute(MusicStoreAppBean.SESSION_KEY);
        MusicAlbum[] albums = msBean.getAlbums();
        if (albums == null) {
            msBean.setErrorDescription("Albums array not found in session");
            return mapping.findForward("notFound");
        }        
        MusicAlbum currentAlbum = null;
        for (int i = 0; i < albums.length; i++) {
            if (asin.equals(albums[i].getASIN())) {
                currentAlbum = albums[i];
                break;
            }
        }
    
    if (msBean.getChannel() == MusicStoreAppBean.VOICE_APP) {
          //cleanup RDC history
          session.removeAttribute("dialogMap");
    }

        if (currentAlbum == null) {
            msBean.setErrorDescription("Album (" + asin + ") not found.");
            return mapping.findForward("notFound");
        } else {
            msBean.setCurrentAlbum(currentAlbum);
            return mapping.findForward("albumfound");
        }
    }

}
