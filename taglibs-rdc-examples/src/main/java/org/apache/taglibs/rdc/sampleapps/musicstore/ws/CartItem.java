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

/**
 * A cart item (item containing additional details about the cart in which 
 * it is currently placed).
 * 
 * @author Jaroslav Gergic
 */
public class CartItem extends Item {
  private static final long serialVersionUID = 1L;
  protected String cartitemId;
  protected String merchantId;
  protected int    quantity;
  
  /**
   * @param cartitemId
   * @param asin
   * @param merchantId
   * @param quantity
   * @param title
   * @param group
   * @param listPrice
   */
  public CartItem(String cartitemId, String asin, String merchantId,
                    int quantity, String title, String group, int listPrice)  {
    super(asin, group, title, listPrice, 0);
    this.cartitemId = cartitemId;
    this.merchantId = merchantId;
    this.quantity = quantity;
  }
  
  /**
   * Access method for the cartitemId property.
   * 
   * @return   the current value of the cartitemId property
   */
  public String getCartItemId() {
    return cartitemId;   
  }
  
  /**
   * Access method for the merchantId property.
   * 
   * @return   the current value of the merchantId property
   */
  public String getMerchantId() {
    return merchantId;   
  }
  
  /**
   * Access method for the quantity property.
   * 
   * @return   the current value of the quantity property
   */
  public int getQuantity() {
    return quantity;   
  }
    
  /**
   * @return java.lang.String
   */
  public String toString() {
    StringBuffer sb = new StringBuffer("CartItem(");
    super.innerToString(sb);
    sb.append(", CartItemId: ").append(cartitemId);
    sb.append(", MerchantId: ").append(merchantId);
    sb.append(", quantity: ").append(quantity);
    sb.append(")");   
    return sb.toString();   
  }
}
