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
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import org.apache.taglibs.rdc.sampleapps.musicstore.AmazonMenuTemplate;
import org.apache.taglibs.rdc.core.StrutsSubmitTag;
/**
 * Struts form bean for mainmenu.jsp (from music store sample app)
 * 
 * @author Rahul Akolkar
 * @author Thomas Ling
 */
public class AlbumFilterForm extends ActionForm {

    private AmazonMenuTemplate.AmazonMenuResult choice;
    private String genre;
    private String category;

    public AlbumFilterForm() {
        choice = null;
        genre  = null;
        category = null;
    }

    public AmazonMenuTemplate.AmazonMenuResult getChoice() {
        return choice;
    }

    public void setChoice(AmazonMenuTemplate.AmazonMenuResult result) {
        choice = result;
    }
    
    /**
     * Access method for the genre property.
     * 
     * @return   the current value of the genre property
     */
    public String getGenre() {
        return genre;   
    }
  
    /**
     * Sets the value of the genre property.
     * 
     * @param aGenre the new value of the genre property
     */
    public void setGenre(String aGenre) {
        genre = aGenre;   
    }
  
    /**
     * Access method for the category property.
     * 
     * @return   the current value of the category property
     */
    public String getCategory() {
        return category;   
    }
  
    /**
     * Sets the value of the category property.
     * 
     * @param aCategory the new value of the category property
     */
    public void setCategory(String aCategory) {
        category = aCategory;   
    }
    
    public ActionErrors validate(ActionMapping mapping,
        HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

         if (category != null && genre != null) {
            // canonicalize choice from GUI
            choice = new AmazonMenuTemplate.AmazonMenuResult();
            choice.setGenre(genre);
            choice.setCategory(category);
        } else {
            StrutsSubmitTag.populate(this, request, errors);
        }

        return errors;
    }

}
