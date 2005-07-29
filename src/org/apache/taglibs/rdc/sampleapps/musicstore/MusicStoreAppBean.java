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

import org.apache.taglibs.rdc.SelectOne;
import org.apache.taglibs.rdc.sampleapps.musicstore.ws.Cart;
import org.apache.taglibs.rdc.sampleapps.musicstore.ws.MusicAlbum;
import org.apache.taglibs.rdc.sampleapps.musicstore.ws.MusicStore;
/**
 * The application bean for the sample music store application
 *
 * @author Rahul Akolkar
 * @author Thomas Ling
 *
 */
public class MusicStoreAppBean {

	/**
	 * The key used to save application state in the HttpSession
	 */
	public static final String SESSION_KEY = "msAppBean";
	public String getSESSION_KEY() { return SESSION_KEY; }

	/**
	 * The genre and category options selected by the user from the
	 * main menu
	 */
	private AmazonMenuTemplate.AmazonMenuResult menuResult;

	/**
	 * The list of albums that are the result set of the user's current
	 * query
	 */
	private MusicAlbum[] albums;
	
	/**
	 * The options that will be consumed by the &lt;rdc:select1%gt; to play
	 * out the result set of the user's current query
	 */
	private SelectOne.Options options;

	/**
	 * The readable value corresponding to the user's current query which
	 * can be played out in a VXML prompt
	 */	
	private String choice;
	
	/**
	 * The individual music album that is user has currently selected for
	 * further operations
	 */
	private MusicAlbum currentAlbum;
	
	/**
	 * The amazon web services shopping cart object for this HttpSession
	 */
	private Cart cart;
	
	/**
	 * Store error prompt, which will be used by the error page  
	 */
	private String errorDescription;
	
	/**
	 * The proactive help bean for this HttpSession, which will prompt user
	 * with helpful hints using some &quot;degree of annoyance%quot; criteria. 
	 */
	private ProactiveHelp proactiveHelp;
	
	/**
	 * The MusicStore class instance for this HttpSession. 
	 */
	private MusicStore musicStore;
	
	/**
	 * The final prompt that informs the user how to retrieve the shopping cart
	 * that was assembled in this session. Coincides with the 'checkout' phase. 
	 */
	private String checkoutPrompt;

	/**
	 * The HTMLMenuLinks class instance for this HttpSession.
	 */
	private HTMLMenuLinks menuLinks;

	/**
	 * Store channel information of the application, The application could 
	 * either be a GUI application, a VOICE application or a VOICE Debug 
	 * applicaton which allows you to output the VXML to a Visual Browser. 
	 */
	private String channel;
	
	/**
	 * GUI channel
	 */
	public static final String GUI_APP   = "msGUI";
	/**
	 * Voice channel
	 */
	public static final String VOICE_APP = "msVOICE";
	/**
	 * Voice Debug channel
	 */
	public static final String VOICE_DBG = "msVOICEDBG";

    
	/**
	 * MusicStoreAppBean Constructor
	 */
	public MusicStoreAppBean(String subscriptionId) {
		this.menuResult = null;
		this.albums = null;
		this.options = null;
		this.choice = null;
		this.currentAlbum = null;
		this.cart = null;
		this.errorDescription = null;
		this.proactiveHelp = null;
		this.musicStore = new MusicStore(subscriptionId);
		this.checkoutPrompt = null;
		this.channel = null;
		this.menuLinks = null;
	}

	/**
	 * Get the user's main menu selection
	 * 
	 * @return the AmazonMenuTemplate.AmazonMenuResult object
	 * corresponding to the main menu selection
	 */
	public AmazonMenuTemplate.AmazonMenuResult getMenuResult() {
		return menuResult;
	}

	/**
	 * Get the array of albums that are the result set of the user's current
	 * query
	 * 
	 * @return The array of albums that are the result set of the user's current
	 * query
	 */
	public MusicAlbum[] getAlbums() {
		return albums;
	}

	/**
	 * Get the readable query string 
	 * 
	 * @return choice the readable query string
	 */
	public String getChoice() {
		return choice;
	}

	/**
	 * Get the result set of the user's query as &lt;rdc:select1&gt; options
	 * 
	 * @return options the SelectOne.Options object
	 */
	public SelectOne.Options getOptions() {
		return options;
	}

	/**
	 * Get the album the user is currently interacting with 
	 * 
	 * @return currentAlbum the MusicAlbum the user is currently 
	 * interacting with
	 */
	public MusicAlbum getCurrentAlbum() {
		return currentAlbum;
	}

	/**
	 * Get the shopping cart object for this session
	 * 
	 * @return cart the shopping cart object
	 */
	public Cart getCart() {
		return cart;
	}

	/**
	 * Get the error description string 
	 * 
	 * @return errorDescription the error description string
	 */
	public String getErrorDescription() {
		return errorDescription;
	}

	/**
	 * Get the proactive help bean
	 * 
	 * @return proactiveHelp the proactive help bean
	 */
	public ProactiveHelp getProactiveHelp() {
		return proactiveHelp;
	}
	
	/**
	 * Get the check out prompt 
	 * 
	 * @return checkoutPrompt the check out prompt
	 */
	public String getCheckoutPrompt() {
		return checkoutPrompt;
	}	
	  
	/**
	 * Get the music store object
	 * 
	 * @return MusicStore The music store object
	 */
	public MusicStore getMusicStore() {
	  return musicStore;
	}

	/**
	 * Get the menu links object
	 * 
	 * @return HTMLMenuLinks The menu links object
	 */
	public HTMLMenuLinks getMenuLinks() {
		return menuLinks;
	}

	/**
	 * Get the application channel
	 * 
	 * @return channel The channel key
	 */
	public String getChannel() {
		return channel;
	}

	/**
	 * Set the user's main menu selection data 
	 * 
	 * @param menuResult the AmazonMenuTemplate.AmazonMenuResult object
	 * corresponding to the main menu selection
	 */
	public void setMenuResult(AmazonMenuTemplate.AmazonMenuResult menuResult) {
		this.menuResult = menuResult;
	}
	
	/**
	 * Set the result set of the user's current query
	 * 
	 * @param albums The array of albums that are the result set of the 
	 * user's current query
	 */
	public void setAlbums(MusicAlbum[] albums) {
		this.albums = albums;
	}

	/**
	 * Set the readable query string 
	 * 
	 * @param choice the readable query string
	 */
	public void setChoice(String choice) {
		this.choice = choice;
	}

	/**
	 * Set the result set of the user's query as &lt;rdc:select1&gt; options
	 * 
	 * @param options the SelectOne.Options object
	 */
	public void setOptions(SelectOne.Options options) {
		this.options = options;
	}

	/**
	 * Set the album the user is currently interacting with 
	 * 
	 * @param currentAlbum the MusicAlbum the user is currently 
	 * interacting with
	 */
	public void setCurrentAlbum(MusicAlbum currentAlbum) {
		this.currentAlbum = currentAlbum;
	}

	/**
	 * Set the shopping cart object for this session
	 * 
	 * @param cart the shopping cart object
	 */
	public void setCart(Cart cart) {
		this.cart = cart;
	}

	/**
	 * Set the error description string 
	 * 
	 * @param errorDescription the error description string
	 */
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	/**
	 * Set the proactive help bean for this session
	 * 
	 * @param proactiveHelp the proactive help bean
	 */
	public void setProactiveHelp(ProactiveHelp proactiveHelp) {
		this.proactiveHelp = proactiveHelp;
	}
	
	/**
	 * Set the check out prompt 
	 * 
	 * @param checkoutPrompt the check out prompt
	 */
	public void setCheckoutPrompt(String checkoutPrompt) {
		this.checkoutPrompt = checkoutPrompt;
	}

	/**
	 * Set the menuLinks bean for Links generation
	 * 
	 * @param menuLinks The menuLinks to set.
	 */
	public void setMenuLinks(HTMLMenuLinks menuLinks) {
		this.menuLinks = menuLinks;
	}
    
	/**
	 * Set the channel of the application 
	 * 
	 * @param channel the application channel
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}

}
