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

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.taglibs.rdc.sampleapps.musicstore.MusicStoreAppBean;
import org.apache.taglibs.rdc.sampleapps.musicstore.ws.Cart;
import org.apache.taglibs.rdc.sampleapps.musicstore.ws.MusicAlbum;
import org.apache.taglibs.rdc.sampleapps.musicstore.ws.MusicStore;

/**
 * Struts action class for adding music album to shopping cart 
 * (music store sample app)
 * 
 * @author Thomas Ling
 * @author Rahul Akolkar
 */
public class AddToCartAction extends Action {

    public AddToCartAction() {
        super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        HttpSession session = request.getSession();
		MusicStoreAppBean msBean = (MusicStoreAppBean) session.
			getAttribute(MusicStoreAppBean.SESSION_KEY);
        MusicStore ms = msBean.getMusicStore();
        Cart currentCart = msBean.getCart();
        MusicAlbum currentAlbum = msBean.getCurrentAlbum();

        if (currentCart != null) {
            currentCart = ms.addToCart(currentCart, currentAlbum, 1);
        } else {
            if (currentAlbum != null) {
                currentCart = ms.createCart(currentAlbum, 1);
            }
        }
		//cleanup session from RDCs
		session.removeAttribute("dialogMap");
		
        if (currentAlbum == null || currentCart == null) {
            msBean.setErrorDescription("Failed to add album to your " +
            	"shopping cart. please try again.");
            return mapping.findForward("onerror");
        }

        msBean.setCart(currentCart);
        String title = currentAlbum.getTitle();
        request.setAttribute("promptContent", "Added " + title + 
			" to the shopping cart.");
		return mapping.findForward("OK");

    }
}