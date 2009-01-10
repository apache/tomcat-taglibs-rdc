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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.taglibs.rdc.sampleapps.musicstore.MusicStoreAppBean;
import org.apache.taglibs.rdc.sampleapps.musicstore.ws.Cart;
import org.apache.taglibs.rdc.sampleapps.musicstore.ws.CartItem;

/**
 * Struts action class for checking out shopping cart (music store sample app)
 * 
 * @author Thomas Ling
 * @author Rahul Akolkar
 */
public class CheckoutAction extends Action {
    
    public CheckoutAction() {
        super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {

        HttpSession session = request.getSession();
        MusicStoreAppBean msBean = (MusicStoreAppBean) session.
            getAttribute(MusicStoreAppBean.SESSION_KEY);
        CheckoutForm coForm = (CheckoutForm) form;
        String action = coForm.getAction();
        Cart currentCart = msBean.getCart();
    
        if (currentCart == null && !action.equals("shopping")) {
            msBean.setErrorDescription("Shopping cart is empty or " +
                "cart not found.");
            return mapping.findForward("onerror");
        }

        // Voice specified calls 
        if (msBean.getChannel() == MusicStoreAppBean.VOICE_APP ||
            msBean.getChannel() == MusicStoreAppBean.VOICE_DBG ) {
            
            //cleanup session from RDCs
            session.removeAttribute("dialogMap");
      
            if (action.equals("checkout")) {
                String hostURI = getHostAsPrompt(request.getServerName());
                int port = request.getServerPort();          
                if (80 != port && 0 != port && 443 != port) {
                    hostURI += " colon " + Integer.toString(port, 10) ;
                } 
                String prompt = createCartLink(session, currentCart, hostURI);
                msBean.setCheckoutPrompt(prompt);
            } else if (action.equals("viewcart")) {
                StringBuffer prompt = new StringBuffer("These following titles " +
                    "are in your cart. ");
                CartItem[] items = currentCart.getCartItems();
                for (int i = 0; i < items.length; i++) {
                    prompt.append(items[i].getTitle()).append(", ");
                }
                request.setAttribute("promptContent", prompt.toString());
            }
        }

        if (action.equals("checkout")) {
            return mapping.findForward("goodbye");
        } else if (action.equals("viewcart")) {
            return mapping.findForward("viewcart");
        } else if (action.equals("shopping")) {
            return mapping.findForward("continueshop");
        } else {
            msBean.setErrorDescription("Error, unknown checkout action.");
            return mapping.findForward("onerror");
        }
    }
        
    private String getHostAsPrompt(String hostURI) {
        // Well, we tried ;-)
        if (hostURI.trim().equalsIgnoreCase("localhost")) {
            return " local host ";
        } else if (hostURI.indexOf('.') != -1){
            return hostURI.replaceAll(".", " dot ");
        }
        return hostURI;
    }
    
    private String createCartLink(HttpSession session, Cart cart, String hostURI) {

        final String NEXTCARTID = "application_NextCartID";
        final String CART_PREFIX = "msCart";
        ServletContext context = session.getServletContext();
        Integer cartID = (Integer)context.getAttribute(NEXTCARTID);
        Integer nextId = null;
        
        if (cartID == null) {
            cartID = new Integer(1);
            nextId = new Integer(2);
        } else {
            nextId = new Integer(cartID.intValue() + 1);        
        }      
        context.setAttribute(NEXTCARTID, nextId);
        
        // save URL with cart ID
        context.setAttribute(CART_PREFIX + cartID, cart.getPurchaseURL());
        StringBuffer sb = new StringBuffer("Your cart number is ").append(cartID);
        sb.append("<break time=\"500ms\"/> To complete the checkout, ");
        sb.append("please go to this website <break time=\"500ms\"/>");
        sb.append("h t t p colon slash slash <break time=\"500ms\"/> ");
        sb.append(hostURI).append(" <break time=\"500ms\"/> ");
        sb.append("slash r d c <break time=\"500ms\"/>dash <break time=\"500ms\"/>");
        sb.append("examples <break time=\"500ms\"/>slash <break time=\"500ms\"/>");
        sb.append(" get cart dot j s p <break time=\"500ms\"/>and retrieve your cart ");
        sb.append("using cart number ").append(cartID);
        return sb.toString();
    }

}
