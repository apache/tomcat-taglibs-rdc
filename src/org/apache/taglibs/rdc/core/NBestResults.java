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
 *
 */
package org.apache.taglibs.rdc.core;

import java.net.URLDecoder;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * <p>Deserializes and stores n-best data received from the vxml browser.</p>
 * 
 * @author Sreeram Balakrishnan 
 * @author Rahul Akolkar
 */
public class NBestResults implements Serializable {
    
    // Arrays containing confidence, interpretation and utterances
    // for each of the n-best results
    ArrayList confidences, interpretations, utterances;

	/* Constructor */
    public NBestResults() {
        confidences = new ArrayList();
        interpretations = new ArrayList();
        utterances = new ArrayList();
    }

	/**
	 * Get the number of results stored
	 * 
	 * @return int the number of results stored
	 */
    public int getNumNBest()
    {
    	// At any point, all arraylists have same size
        return confidences.size();
    }
    
    /**
     * Get confidence for Nth result
     * 
     * @param n the index of the result
     * @return the float value of the confidence
     */
	public float getNthConfidence(int n) {
		if (n < confidences.size()) {
			return ((Float)confidences.get(n)).floatValue();
		}
		return 0.0F;
	}

	/**
	 * Get interpretation for Nth result, which is stored as a HashMap
	 * of attribute-value pairs
	 * 
	 * @param n the index of the result
	 * @return the interpretation
	 */
	public HashMap getNthInterpretation(int n) {
		if (n < interpretations.size()) {
			return (HashMap)interpretations.get(n);
		}
		return null;
	}

	/**
	 * Get utterance for Nth result
	 * 
	 * @param n the index of the result
	 * @return the utterance
	 */
	public String getNthUtterance(int n) {
		if (n < utterances.size()) {
			return (String)utterances.get(n);
		}
		return null;
	}
	
	/**
	 * Populate this object with the n-best results corresponding
	 * to this serialized string
	 * 
	 * @param serializedNBest the serialized n-best result string
	 * obtained from the vxml browser
	 * To understand this method, look at the nbest.js file in the
	 * .grammar directory
	 */
    public void setNBestResults(String serializedNBest) {
    	
    	String decodedNBest = null;
    	try {
    		decodedNBest = URLDecoder.decode(serializedNBest, "UTF-8");
    	} catch (UnsupportedEncodingException uee){
			String errMsg = "Deserialization of n-best tokenizer failed: " +
				"Cannot decode using UTF-8 encoding scheme";
			throw new IllegalArgumentException(errMsg);   		
    	}
    	if (decodedNBest == null) {
			String errMsg = "Deserialization of n-best tokenizer failed: " +
				"Cannot decode given encoded string: " + serializedNBest;
			throw new IllegalArgumentException(errMsg);    		
    	}
        StringTokenizer nBestTok = new StringTokenizer(decodedNBest, ";");
        if (nBestTok.countTokens() % 3 != 0) {
        	String errMsg = "Deserialization of n-best tokenizer failed: " +
        		"Illegal Argument \"" + serializedNBest + "\", number " +
        		"of tokens: " + nBestTok.countTokens();
        	throw new IllegalArgumentException(errMsg);
        }
        while (nBestTok.hasMoreTokens()) {        	
            confidences.add(new Float(nBestTok.nextToken()));
            utterances.add(nBestTok.nextToken());
            StringTokenizer interpTok = 
            	new StringTokenizer(nBestTok.nextToken(), ",");
            HashMap interpMap = new HashMap();
            while (interpTok.hasMoreTokens()) {
                StringTokenizer avst = 
                	new StringTokenizer(interpTok.nextToken(), "=");
                if (avst.countTokens() > 1) {
                	interpMap.put(avst.nextToken(),avst.nextToken());
                } else {
                	interpMap.put("",avst.nextToken());
                }
            }
            interpretations.add(interpMap);
        }
    }
}
