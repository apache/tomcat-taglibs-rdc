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

import java.util.Date;
import java.text.SimpleDateFormat;


/**
 * Class representing a music album.
 * 
 * @author Jaroslav Gergic
 * @author Rahul Akolkar
 * @author Thomas Ling
 */
public class MusicAlbum extends Item implements java.io.Serializable {

    protected String artist;
    protected Date releaseDate;
    protected String label;
    public static SimpleDateFormat DATE_FORMAT = 
        new SimpleDateFormat("yyyy-MM-dd");
    protected int genres[];
    protected Image smallImage;
    protected OfferSummary offerSummary;

    /**
     * @param asin
     * @param group
     * @param title
     * @param listPrice
     * @param artist
     * @param releaseDate
     * @param label
     * @param rank
     */
    public MusicAlbum(String asin, String group, String title, int listPrice,
            String artist, Date releaseDate, String label, int rank) {
        super(asin, group, title, listPrice, rank);
        this.artist = artist;
        this.releaseDate = releaseDate;
        this.label = label;
        this.genres = null;
        this.smallImage = null;
        this.offerSummary = null;
    }

    /**
     * Access method for the artist property.
     * 
     * @return the current value of the artist property
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Access method for the releaseDate property.
     * 
     * @return the current value of the releaseDate property
     */
    public Date getReleaseDate() {
        return releaseDate;
    }

    /**
     * Access method for the label property.
     * 
     * @return the current value of the label property
     */
    public String getLabel() {
        return label;
    }

    /**
     * Access method for the genres property.
     * 
     * @return the current value of the genres property
     */
    public int[] getGenres() {
        return genres;
    }

    /**
     * Sets the value of the genres property.
     * 
     * @param aGenres the new value of the genres property
     */
    public void setGenres(int[] aGenres) {
        genres = aGenres;
    }

    /**
     * @return Returns the smallImage.
     */
    public Image getSmallImage() {
          return smallImage;   
    }
    
    /**
     * @param smallImage The smallImage to set.
     */
    public void setSmallImage(Image smallImage) {
          this.smallImage = smallImage;   
    }

    /**
     * @return Returns the price offer summary.
     */
    public OfferSummary getOfferSummary() {
        return offerSummary;
    }
    /**
     * @param offerSummary The offerSummary to set.
     */
    public void setOfferSummary(OfferSummary offerSummary) {
        this.offerSummary = offerSummary;
    }

    /**
     * Get the album summary
     * 
     * @return java.lang.String The album summary
     */  
    public String getSummary() {
      String priceString = Integer.toString(this.listPrice);
      int delim = priceString.length() - 2;
      priceString = "$" + priceString.substring(0, delim) + "." + 
          priceString.substring(delim);
      StringBuffer sb = new StringBuffer();
      sb.append(this.title).append(" by ").append(this.artist);
      sb.append(" is available for ").append(priceString);
      return sb.toString();
    }
  
    /**
     * Get the album details
     * 
     * @return java.lang.String The album details
     */
    public String getDetails() {
      java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMMMM dd yyyy");
      String date = sdf.format(this.releaseDate);
      StringBuffer sb = new StringBuffer();
      sb.append(this.title).append(" has  been released by ").append(this.label);
      sb.append(" on ").append(date);
      return sb.toString();
    }
    
    /**
     * @return java.lang.String
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("MusicAlbum(");
        super.innerToString(sb);
        sb.append(", Artist: ").append(artist);
        sb.append(", ReleaseDate: ").append(DATE_FORMAT.format(releaseDate));
        sb.append(", Label: ").append(label);
        sb.append(", Genres: [");
        if (genres != null) {
            for (int i = 0; i < genres.length; i++) {
                sb.append(genres[i]);
                if (i + 1 < genres.length)
                    sb.append(", ");
            }
        }
        sb.append("])");
        return sb.toString();
    }

}
