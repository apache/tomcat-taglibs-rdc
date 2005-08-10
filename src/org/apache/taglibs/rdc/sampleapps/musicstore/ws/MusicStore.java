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
package org.apache.taglibs.rdc.sampleapps.musicstore.ws;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Class representing an Amazon Web Services music store.
 * Contains necessary AWS queries needed by the music store application.
 * 
 * @author Jaroslav Gergic
 * @author Rahul Akolkar
 * @author Thomas Ling
 */
public class MusicStore {

    protected static final String MERCHANT_ID = "Featured";
    protected static final String XMLNS_NS = "http://www.w3.org/2000/xmlns/";
    protected static final String RESPONSE_GRP = "ItemAttributes,Images,SalesRank,OfferSummary";

    protected String subscriptionId;
    
    private static Log log = LogFactory.getLog(MusicStore.class);
    private static final boolean info = false;

    /**
     * @param subscriptionId Amazon ECS subscription ID
     */
    public MusicStore(String subscriptionId) {
        if (subscriptionId == null || subscriptionId.trim().length() == 0) {
            String errDescription = "RDC MusicStore instantiated without an " +
                "Amazon web services subscription ID. Read application " +
                "README file.";
            log.error(errDescription);
            throw new IllegalArgumentException(errDescription);
        }
        this.subscriptionId = subscriptionId;   
    }

    /**
     * Retrurns an array of music genres (styles) listed on Amazon
     * 
     * @param parent parent music style
     * @return int[]
     * @throws IOException
     * @throws java.io.IOException
     */
    public int[] getGenres(int parent) throws IOException {
        int results[] = null;
        StringBuffer sb = new StringBuffer();
        sb.append("http://webservices.amazon.com/onca/xml");
        sb.append("?Service=AWSECommerceService");
        sb.append("&SubscriptionId=").append(subscriptionId);
        sb.append("&Operation=BrowseNodeLookup");
        sb.append("&BrowseNodeId=").append(parent);
        String qs = sb.toString();
        if (info) { log.info(qs); }
        InputSource is = new InputSource(qs);
        DocumentBuilder parser = getSimpleDOMBuilder();
        if (parser != null) {
            try {
                Document doc = parser.parse(is);
                Element root = (Element) doc.getDocumentElement();
                //Amazon NS changes quite frequently, let's get the latest one
                String ecs_ns = root.getAttribute("xmlns");
                log.debug("xmlns = " + ecs_ns);
                //Let's define a prefix required by XPath
                root.setAttributeNS(XMLNS_NS, "xmlns:ecs", ecs_ns);
                String xpath = "/ecs:BrowseNodeLookupResponse/ecs:BrowseNodes/ecs:BrowseNode/ecs:Children/ecs:BrowseNode";
                NodeList lst = XPathAPI.selectNodeList(root, xpath, root);
                int sz = lst.getLength();
                results = new int[sz];
                for (int i = 0; i < sz; i++) {
                    Element bnode = (Element) (lst.item(i));
                    NodeList nms = bnode.getElementsByTagNameNS(ecs_ns, "Name");
                    NodeList ids = bnode.getElementsByTagNameNS(ecs_ns,
                            "BrowseNodeId");
                    String name = xGetVal(nms.item(0));
                    String ndid = xGetVal(ids.item(0));
                    results[i] = Integer.parseInt(ndid);
                }
            } catch (Exception e) {
                log.error("error parsing result set", e);
                throw new IOException("error parsing result set");
            }
        }
        return results;
    }

    /**
     * @param genre
     * @return org.apache.taglibs.rdc.sampleapps.musicstore.ws.MusicAlbum[]
     */
    public MusicAlbum[] getTopSellers(int genre) throws IOException {
        StringBuffer sb = new StringBuffer();
        sb.append("http://webservices.amazon.com/onca/xml");
        sb.append("?Service=AWSECommerceService");
        sb.append("&SubscriptionId=").append(subscriptionId);
        sb.append("&MerchantId=").append(MERCHANT_ID);
        sb.append("&Operation=").append("ItemSearch");
        sb.append("&BrowseNode=").append(genre);
        sb.append("&SearchIndex=").append("Music");
        sb.append("&Condition=").append("New");
        sb.append("&ResponseGroup=").append(RESPONSE_GRP);
        sb.append("&Sort=").append("salesrank"); //Top Sellers
        String qs = sb.toString();
        if (info) { log.info(qs); }
        return doAlbumQuery(qs, genre);
    }

    /**
     * @param genre
     * @return org.apache.taglibs.rdc.sampleapps.musicstore.ws.MusicAlbum[]
     */
    public MusicAlbum[] getNewReleases(int genre) throws IOException {
        StringBuffer sb = new StringBuffer();
        sb.append("http://webservices.amazon.com/onca/xml");
        sb.append("?Service=AWSECommerceService");
        sb.append("&SubscriptionId=").append(subscriptionId);
        sb.append("&MerchantId=").append(MERCHANT_ID);
        sb.append("&Operation=").append("ItemSearch");
        sb.append("&BrowseNode=").append(genre);
        sb.append("&SearchIndex=").append("Music");
        sb.append("&Condition=").append("New");
        sb.append("&ResponseGroup=").append(RESPONSE_GRP);
        sb.append("&Sort=").append("-orig-rel-date"); //Reveresed Release Date
        String qs = sb.toString();
        if (info) { log.info(qs); }
        return doAlbumQuery(qs, genre);
    }

    /**
     * @param genre
     * @return org.apache.taglibs.rdc.sampleapps.musicstore.ws.MusicAlbum[]
     */
    public MusicAlbum[] getFeaturedItems(int genre) throws IOException {
        StringBuffer sb = new StringBuffer();
        sb.append("http://webservices.amazon.com/onca/xml");
        sb.append("?Service=AWSECommerceService");
        sb.append("&SubscriptionId=").append(subscriptionId);
        sb.append("&MerchantId=").append(MERCHANT_ID);
        sb.append("&Operation=").append("ItemSearch");
        sb.append("&BrowseNode=").append(genre);
        sb.append("&SearchIndex=").append("Music");
        sb.append("&Condition=").append("New");
        sb.append("&ResponseGroup=").append(RESPONSE_GRP);
        sb.append("&Sort=").append("psrank"); //Featured Items
        String qs = sb.toString();
        if (info) { log.info(qs); }
        return doAlbumQuery(qs, genre);
    }

    /**
     * @param album
     * @return org.apache.taglibs.rdc.sampleapps.musicstore.ws.MusicAlbum[]
     */
    public MusicAlbum[] getSimilarItems(MusicAlbum album) throws IOException {
        StringBuffer sb = new StringBuffer();
        sb.append("http://webservices.amazon.com/onca/xml");
        sb.append("?Service=AWSECommerceService");
        sb.append("&SubscriptionId=").append(subscriptionId);
        sb.append("&MerchantId=").append(MERCHANT_ID);
        sb.append("&Operation=").append("SimilarityLookup");
        sb.append("&ItemId=").append(album.getASIN());
        sb.append("&SimilarityType=").append("Intersection");
        sb.append("&ResponseGroup=").append(RESPONSE_GRP);
        String qs = sb.toString();
        if (info) { log.info(qs); }
        MusicAlbum results[] = null;
        InputSource is = new InputSource(qs);
        DocumentBuilder parser = getSimpleDOMBuilder();
        if (parser != null) {
            try {
                Document doc = parser.parse(is);
                Element root = (Element) doc.getDocumentElement();
                //Amazon NS changes quite frequently, let's get the latest one
                String ecs_ns = root.getAttribute("xmlns");
                log.debug("xmlns = " + ecs_ns);
                //Let's define a prefix required by XPath
                root.setAttributeNS(XMLNS_NS, "xmlns:ecs", ecs_ns);
                //all items
                String xpath = "/ecs:SimilarityLookupResponse/ecs:Items/ecs:Item";
                NodeList items = XPathAPI.selectNodeList(root, xpath, root);
                int sz = items.getLength();
                results = new MusicAlbum[sz];
                for (int i = 0; i < sz; i++) {
                    results[i] = parseAlbum(root, items.item(i), album
                            .getGenres()[0]);
                }
            } catch (Exception e) {
                log.error("error parsing result set", e);
                throw new IOException("error parsing result set");
            }
        }
        return results;
    }

    /**
     * @param album
     * @return org.apache.taglibs.rdc.sampleapps.musicstore.ws.MusicAlbum[]
     */
    public Cart createCart(MusicAlbum album, int quantity) throws IOException {
        StringBuffer sb = new StringBuffer();
        sb.append("http://webservices.amazon.com/onca/xml");
        sb.append("?Service=AWSECommerceService");
        sb.append("&SubscriptionId=").append(subscriptionId);
        sb.append("&Operation=").append("CartCreate");
        sb.append("&Item.1.ASIN=").append(album.getASIN());
        sb.append("&Item.1.Quantity=").append(quantity);
        String qs = sb.toString();
        if (info) { log.info(qs); }
        Cart cart = doCartOperation(qs, "CartCreate");
        return cart;
    }

    /**
     * @param album
     * @return org.apache.taglibs.rdc.sampleapps.musicstore.ws.MusicAlbum[]
     */
    public Cart addToCart(Cart cart, MusicAlbum album, int quantity)
            throws IOException {
        StringBuffer sb = new StringBuffer();
        sb.append("http://webservices.amazon.com/onca/xml");
        sb.append("?Service=AWSECommerceService");
        sb.append("&SubscriptionId=").append(subscriptionId);
        sb.append("&Operation=").append("CartAdd");
        sb.append("&CartId=").append(cart.getCartId());
        sb.append("&HMAC=").append(cart.getHMAC());
        sb.append("&Item.1.ASIN=").append(album.getASIN());
        sb.append("&Item.1.Quantity=").append(quantity);
        String qs = sb.toString();
        if (info) { log.info(qs); }
        Cart newCart = doCartOperation(qs, "CartAdd");
        return newCart;
    }

    /**
     * a helper method - parser factory
     * 
     * @return DocumentBuilder
     */
    protected static DocumentBuilder getSimpleDOMBuilder() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            dbf.setValidating(false);
            return dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            log.fatal("can not instanitate XML parser", e);
        }
        return null;
    }

    /**
     * retrieve a DOM node value as a string depending on Node type.
     * 
     * @param target a node to be retreived
     * @return node value as a string
     */
    protected static String xGetVal(Node target) {
        String result = "";
        if (target == null)
            return result;
        switch (target.getNodeType()) {
        case Node.ATTRIBUTE_NODE:
            result = target.getNodeValue();
            break;
        case Node.ELEMENT_NODE:
            if (target.hasChildNodes()) {
                Node chld = target.getFirstChild();
                StringBuffer buf = new StringBuffer();
                while (chld != null) {
                    if (chld.getNodeType() == Node.TEXT_NODE)
                        buf.append(((CharacterData) chld).getData());
                    chld = chld.getNextSibling();
                }
                result = buf.toString();
            }
            break;
        case Node.TEXT_NODE:
        case Node.CDATA_SECTION_NODE:
            result = ((CharacterData) target).getData();
            break;
        default: {
            String err = "Trying to get value of a strange Node type: ";
            err += target.getNodeType();
            throw new IllegalArgumentException(err);
        }
        }
        return result.trim();
    }

    protected static MusicAlbum[] doAlbumQuery(String request, int genre)
            throws IOException {
        MusicAlbum results[] = null;
        InputSource is = new InputSource(request);
        DocumentBuilder parser = getSimpleDOMBuilder();
        if (parser != null) {
            try {
                Document doc = parser.parse(is);
                Element root = (Element) doc.getDocumentElement();
                //Amazon NS changes quite frequently, let's get the latest one
                String ecs_ns = root.getAttribute("xmlns");
                log.debug("xmlns = " + ecs_ns);
                //Let's define a prefix required by XPath
                root.setAttributeNS(XMLNS_NS, "xmlns:ecs", ecs_ns);
                //all items
                String xpath = "/ecs:ItemSearchResponse/ecs:Items/ecs:Item";
                NodeList items = XPathAPI.selectNodeList(root, xpath, root);
                int sz = items.getLength();
                results = new MusicAlbum[sz];
                for (int i = 0; i < sz; i++) {
                    results[i] = parseAlbum(root, items.item(i), genre);
                }
            } catch (Exception e) {
                log.error("error parsing result set", e);
                throw new IOException("error parsing result set");
            }
        }
        return results;
    }

    protected static Cart doCartOperation(String request, String cartOp)
            throws IOException {
        Cart cart = null;
        InputSource is = new InputSource(request);
        DocumentBuilder parser = getSimpleDOMBuilder();
        if (parser != null) {
            try {
                Document doc = parser.parse(is);
                Element root = (Element) doc.getDocumentElement();
                //Amazon NS changes quite frequently, let's get the latest one
                String ecs_ns = root.getAttribute("xmlns");
                log.debug("xmlns = " + ecs_ns);
                //Let's define a prefix required by XPath
                root.setAttributeNS(XMLNS_NS, "xmlns:ecs", ecs_ns);
                String cartPath = "/ecs:" + cartOp + "Response/ecs:Cart";
                String xpath = cartPath + "/ecs:CartId";
                String cartId = xmlGet(root, xpath, root);
                xpath = cartPath + "/ecs:HMAC";
                String hMAC = xmlGet(root, xpath, root);
                xpath = cartPath + "/ecs:PurchaseURL";
                String purchaseURL = xmlGet(root, xpath, root);

                cart = new Cart(cartId, hMAC, purchaseURL);
                xpath = cartPath + "/ecs:CartItems/ecs:CartItem";
                NodeList items = XPathAPI.selectNodeList(root, xpath, root);
                int sz = items.getLength();
                CartItem cartItems[] = new CartItem[sz];
                for (int i = 0; i < sz; i++) {
                    cartItems[i] = parseCartItem(root, items.item(i));
                }
                cart.setCartItems(cartItems);
                if (info) { log.info(cart); }  
            } catch (Exception e) {
                log.error("error parsing result set", e);
                throw new IOException("error parsing result set");
            }
        }

        return cart;
    }

    protected static MusicAlbum parseAlbum(Element root, Node album, int genre) {
        String asin = xmlGet(album, "ecs:ASIN", root);
        String group = 
            xmlGet(album, "ecs:ItemAttributes/ecs:ProductGroup", root);
        String title = xmlGet(album, "ecs:ItemAttributes/ecs:Title", root);
        String artist = xmlGet(album, "ecs:ItemAttributes/ecs:Artist", root);
        
        int listPrice = 0;
        String sPrice = 
            xmlGet(album,"ecs:ItemAttributes/ecs:ListPrice/ecs:Amount", root);        
        try {
            listPrice = Integer.parseInt(sPrice);
        } catch (NumberFormatException numEx) {
            log.warn("error parsing list price " + sPrice);
        }
 
        int rank = 0;
        String sRank = xmlGet(album, "ecs:SalesRank", root);       
        try {
            Integer.parseInt(sRank);
        } catch (NumberFormatException numEx) {
            log.warn("error parsing rank " + sRank);
        }

        Date relDate = null;
        String sDate = "";
        try {
            sDate = xmlGet(album, "ecs:ItemAttributes/ecs:ReleaseDate", root);
            relDate = MusicAlbum.DATE_FORMAT.parse(sDate);
        } catch (ParseException e) {
            log.warn("error parsing date: " + sDate);
        }
        
        String label = xmlGet(album, "ecs:ItemAttributes/ecs:Label", root);
        int genres[] = new int[] { genre };
        
        Image smallImage = null;
        try {
          Node imageNode = 
              XPathAPI.selectSingleNode(album, "ecs:SmallImage", root);
          smallImage = parseImage(root, imageNode);
        } catch (TransformerException e) {
            log.warn("error evaluating XPath: ecs:SmallImage" +  e);
        }
 
        OfferSummary offerSummary = null;
        try {
          Node offerSummaryNode =
              XPathAPI.selectSingleNode(album, "ecs:OfferSummary", root);
          offerSummary = parseOfferSummary(root, offerSummaryNode); 
        } catch (TransformerException e) {
            log.warn("error evaluating XPath: ecs:OfferSummary" +  e);
        }
        
        MusicAlbum ma = 
            new MusicAlbum(asin, group, title, listPrice, artist,
                relDate, label, rank);
        ma.setGenres(genres);
        ma.setSmallImage(smallImage);
        ma.setOfferSummary(offerSummary);
        
        return ma;
    }

    protected static Image parseImage(Element root, Node imageNode) {
        Image image = null;
        try {
            String imageURL = xmlGet(imageNode, "ecs:URL", root);
            int height = 
                Integer.parseInt(xmlGet(imageNode, "ecs:Height", root));
            int width = 
                Integer.parseInt(xmlGet(imageNode, "ecs:Width", root));
            image = new Image(imageURL, height, width);
        } catch (Exception e) {
            log.warn("error parsing item's image");
        }
        
        return image;
    }
    
    protected static OfferSummary parseOfferSummary(Element root, Node offerSummary) {
        int totalNew = 0;
        String sTotalNew = xmlGet(offerSummary, "ecs:TotalNew", root);       
        try {
            totalNew = Integer.parseInt(sTotalNew);
        } catch (NumberFormatException numEx) {
            log.warn("error parsing total new offer " + sTotalNew);
        }
        
        int totalUsed = 0;
        String sTotalUsed = xmlGet(offerSummary, "ecs:TotalUsed", root);        
        try {
            totalUsed = Integer.parseInt(sTotalUsed);
        } catch (NumberFormatException numEx) {
            log.warn("error parsing total used offer " + sTotalUsed);
        }
        
        int totalCollect = 0;
        String sTotalCollect = xmlGet(offerSummary, "ecs:TotalCollectible", root);       
        try {
            totalCollect = Integer.parseInt(sTotalCollect);
        } catch (NumberFormatException numEx) {
            log.warn("error parsing total collectible offer " + sTotalCollect);
        }

        int newPrice = 0;
        if (totalNew > 0) {
            String sNewPrice = 
                xmlGet(offerSummary, "ecs:LowestNewPrice/ecs:Amount", root);
            try {
                newPrice = Integer.parseInt(sNewPrice);
            } catch (NumberFormatException numEx) {
                log.warn("error parsing lowest new price " + sNewPrice);
            }
        }

        int usedPrice = 0;
        if (totalUsed > 0) {
            String sUsedPrice = 
                xmlGet(offerSummary, "ecs:LowestUsedPrice/ecs:Amount", root);       
            try {
                usedPrice = Integer.parseInt(sUsedPrice);
            } catch (NumberFormatException numEx) {
                log.warn("error parsing lowest ued price " + sUsedPrice);
            }
        }
        
        int collectPrice = 0;
        if (totalCollect > 0) {
            String sCollectPrice = 
                xmlGet(offerSummary, "ecs:LowestCollectiblePrice/ecs:Amount", root);        
            try {
                collectPrice = Integer.parseInt(sCollectPrice);
            } catch (NumberFormatException numEx) {
                log.warn("error parsing lowest collectible price " + sCollectPrice);
            }
        }
                
        return new OfferSummary(newPrice, usedPrice, collectPrice, 
                                totalNew, totalUsed, totalCollect);
    }
    
    protected static CartItem parseCartItem(Element root, Node cartItem) {
        String cartItemId = xmlGet(cartItem, "ecs:CartItemId", root);
        String asin = xmlGet(cartItem, "ecs:ASIN", root);
        String merchantId = xmlGet(cartItem, "ecs:MerchantId", root);
        int quantity = Integer.parseInt(xmlGet(cartItem, "ecs:Quantity", root));
        String title = xmlGet(cartItem, "ecs:Title", root);
        String group = xmlGet(cartItem, "ecs:ProductGroup", root);
        int listPrice = Integer.parseInt(xmlGet(cartItem,
                "ecs:Price/ecs:Amount", root));

        CartItem item = new CartItem(cartItemId, asin, merchantId, quantity,
                title, group, listPrice);
        return item;
    }

    protected static final String xmlGet(Node node, String xpath, Node xmlnsNode) {
        String rslt = "";
        try {
            rslt = xGetVal(XPathAPI.selectSingleNode(node, xpath, xmlnsNode));
        } catch (TransformerException e) {
            log.warn("error evaluating XPath: " + xpath, e);
        }
        return rslt;
    }

}