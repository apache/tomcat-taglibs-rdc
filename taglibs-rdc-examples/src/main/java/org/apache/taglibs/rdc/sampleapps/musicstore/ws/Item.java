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
 * A generic Amazon Item - an unspecified type
 * It should be normally abstract, but having it a concrete class allows us to 
 * proceed with generic item operations (like searches)
 * 
 * @author Jaroslav Gergic
 * @author Rahul Akolkar
 */
public class Item implements java.io.Serializable {
  protected String aSIN;
  protected String productGroup;
  protected String title;
  protected int rank;

  /**
   * Price in US dollars multiplied by 100 (no decimal symbol required)
   */
  protected int listPrice;
  
  /**
   * @param asin
   * @param productGroup
   * @param title
   * @param listPrice
   * @param rank
   */
  public Item(String asin, String productGroup, String title, 
                int listPrice, int rank) {
    this.aSIN = asin;
    this.productGroup = productGroup;
    this.title = title;
    this.listPrice = listPrice;   
    this.rank = rank;
  }
  
  /**
   * Access method for the aSIN property.
   * 
   * @return   the current value of the aSIN property
   */
  public String getASIN() {
    return aSIN;   
  }
  
  /**
   * Access method for the productGroup property.
   * 
   * @return   the current value of the productGroup property
   */
  public String getProductGroup() {
    return productGroup;   
  }

  
  /**
   * Set title property.
   * 
   * @param   the new value of the title property
   */
  public void setTitle(String title) {
    this.title = title;   
  }
  
  /**
   * Access method for the title property.
   * 
   * @return   the current value of the title property
   */
  public String getTitle() {
    return title;   
  }
  
  /**
   * Access method for the listPrice property.
   * 
   * @return   the current value of the listPrice property
   */
  public int getListPrice() {
    return listPrice;   
  }
  
  /**
   * @param buf
   */
  protected void innerToString(StringBuffer buf) {
    buf.append("ASIN: ").append(aSIN);
    buf.append(", ProductGroup: ").append(productGroup);
    buf.append(", Title: ").append(title);
    buf.append(", ListPrice: ").append(listPrice);   
  }
  
  /**
   * @return java.lang.String
   */
  public String toString() {
    StringBuffer sb = new StringBuffer("Item(");
    innerToString(sb);
    sb.append(')');
    return sb.toString();   
  }

  /**
   * @return Returns the rank.
   */
  public int getRank() {
    return rank;
  }
}
