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

package org.apache.taglibs.rdc.sampleapps.musicstore;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Enumeration;
import java.util.ArrayList;
import java.text.MessageFormat;
import org.apache.taglibs.rdc.sampleapps.musicstore.ws.MusicAlbum;

/**
 * Implements a simple HTML Menu Generator JavaBean
 * @author Thomas Ling
 *
 */
public class HTMLMenuLinks {

	static public String MAINMENU_CATEGORY = "mainmenu.category.";
	static public String MAINMENU_GENRE    = "mainmenu.genre.";
	static public String DEFAULT_GENRE     = "301668";
    static public String MUSICSTORE_PATH   = "/musicstore-app";
	
	protected ResourceBundle menuRes = null;
	private String baseURL = null;
	private String links = "";
	private ArrayList categoryKeys = new ArrayList();
	private ArrayList genreKeys = new ArrayList();
	
	 
	public HTMLMenuLinks(String aURL) {
        menuRes = ResourceBundle.getBundle(
            "org.apache.taglibs.rdc.sampleapps.musicstore.resources.MusicStoreMenu", Locale.US);

        baseURL = aURL;
		Enumeration keys = menuRes.getKeys();
		
		while (keys.hasMoreElements()) {
			String akey = (String)keys.nextElement();
			String id = akey.substring(akey.lastIndexOf('.') + 1);
			
			if (akey.startsWith(MAINMENU_CATEGORY)) {
				categoryKeys.add(id);
			} else if (akey.startsWith(MAINMENU_GENRE)) {
				genreKeys.add(id);
			}
		}
	}

	/**
	 * Sets the value of the hints property.
	 * @param aHints the new value of the hints property
	 */
	public void generateGenreLinks() {
		links ="";
		
		for (int i = 0; i < genreKeys.size(); i++) {
			String key = (String)genreKeys.get(i);
			String linkURL = baseURL;
			String text    = menuRes.getString(MAINMENU_GENRE + key);
			
			linkURL += MUSICSTORE_PATH + "/mainmenu_gui.do?genre=" + key;
			links += buildLinkNL(linkURL, text);
		}
	}
	
	public void generateCategoryLinks(String genre, boolean bBrowse) {
		links = "";
		
		for (int i = 0; i < categoryKeys.size(); i++) {
		    String key = (String)categoryKeys.get(i);
			String linkURL = baseURL;
			String text    = menuRes.getString(MAINMENU_CATEGORY + key);
			
			if (key.equals("0")) {
				if (bBrowse) {
					linkURL += MUSICSTORE_PATH + "/mainmenu_gui.do?browseMusic=1";
					links += buildLinkNL(linkURL, text);
				}
			} else {
				linkURL += "/listalbums_gui.do?category=" + key + "&genre=" + genre;
				links += buildLinkNL(linkURL, text);
			}
		}
	}

	public void generateAlbumLinks(MusicAlbum[] albums) {
		links = "";
		
		for (int i = 0; i < albums.length; i++) {
			MusicAlbum album = albums[i]; 
			String linkURL = baseURL + "/reviewalbum_gui.do?asin=" + album.getASIN();
			String text = album.getTitle() + ", " + album.getArtist();
			links += buildLinkNL(linkURL, text);
		}
	}
	
	public String getLinks() {
		return links;
	}
	
	public String getHomeLink() {
		String linkURL = baseURL + MUSICSTORE_PATH + "/mainmenu_gui.do"; 
		return buildLink(linkURL, "Home");
	}

	public String getCartLink() {
		String linkURL = baseURL + "/viewcart_gui.do"; 
		return buildLink(linkURL, "View Cart");
	}
	public String getHelpLink() {
		String linkURL = baseURL + "/help_gui.do"; 
		return buildLink(linkURL, "About");
	}
	
	private String buildLinkNL(String link, String text) {
		return buildLink(link, text) + "<br/>";
	}
	
	private String buildLink(String link, String text) {
		MessageFormat format = 
			new MessageFormat("<a href=\"{0}\">{1}</a>\n");
		Object[] args = {link, text};
		return format.format(args);
	}
		
}
