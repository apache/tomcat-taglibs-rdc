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

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.taglibs.rdc.core.StrutsSubmitTag;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;

/**
 * Struts form bean for listalbums.jsp (from music store sample app)
 * 
 * @author Jaroslav Gergic
 * @author Rahul Akolkar
 */
public class IndividualAlbumForm extends ActionForm {
  private String asin;

  public IndividualAlbumForm() { }
  
  /**
   * Access method for the asin property.
   * 
   * @return   the current value of the asin property
   */
  public String getAsin() {
    return asin;
  }
  
  /**
   * Sets the value of the asin property.
   * 
   * @param aAsin the new value of the asin property
   */
  public void setAsin(String aAsin) {
    asin = aAsin;
  }

  public ActionErrors validate(ActionMapping mapping,
                   HttpServletRequest request) {
    ActionErrors errors = new ActionErrors();
    //try block is to make this compatible with regular Struts submit
    try {
      StrutsSubmitTag.populate(this, request, errors);
    } catch(NullPointerException e) {
      //ignore it, it was a regular struts submit
    }
    return errors;
  }
}
