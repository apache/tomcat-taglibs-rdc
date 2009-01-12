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
package org.apache.taglibs.rdc.sampleapps.musicstore.ws;

import java.io.Serializable;

/**
 * Class representing the Amazon Web Services shopping cart.
 * 
 * @author Jaroslav Gergic
 * @author Rahul Akolkar
 */
public final class Cart implements Serializable {

    private static final long serialVersionUID = 1L;
    protected String   cartId;
    protected String   hMAC;
    protected String   purchaseURL;
    protected CartItem cartItems[];

    public Cart(String cartId, String hmac, String purchaseURL) {
        this.cartId    = cartId;
        this.hMAC  = hmac;
        this.purchaseURL = purchaseURL;
        this.cartItems = null;
    }

    public String getCartId() {
        return cartId;
    }

    public String getHMAC() {
        return hMAC;
    }

    public String getPurchaseURL() {
        return purchaseURL;
    }

    public CartItem[] getCartItems() {
        return cartItems;
    }

    public void setCartItems(CartItem[] aCartItems) {
        cartItems = aCartItems;
  }

  /**
   * @return String
   */
  public String toString() {
    StringBuffer sb = new StringBuffer("Cart(");
    sb.append("cartId: ").append(cartId);
    sb.append(", hMAC: ").append(hMAC);
    sb.append(", purchaseURL: ").append(purchaseURL);
    sb.append(", CartItems: [");
    
    if (cartItems != null) {
      for (int i=0; i < cartItems.length; i++) {
        sb.append(cartItems[i]);
        if (i + 1 < cartItems.length)
          sb.append(", ");
      }
    }

    sb.append("])");   
    return sb.toString();  
  }
    

}
