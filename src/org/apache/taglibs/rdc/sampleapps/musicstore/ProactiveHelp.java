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

import java.util.ResourceBundle;
import java.util.Map;
import java.util.Random;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Implements a simple proactive help (user hints) java bean
 * 
 * @author Jaroslav Gergic
 */
public class ProactiveHelp {
  
  /**
   * The probability the proactive help component will trigger &lt;0..100&gt; 
   * inclusive. 
   * The default value is 30%
   */
  protected int threshold = 30;
  
  /**
   * Adjust probabilities based on play count for each particular hint.
   */
  protected boolean usageWeighted = false;
  protected ResourceBundle hints = null;
  private Random generator = new Random ();
  private java.util.ArrayList allkeys = new ArrayList ();
  private int stats[] = null;
  
  /**
   */
  public ProactiveHelp() {
   
  }
  
  /**
   * Access method for the threshold property.
   * 
   * @return   the current value of the threshold property
   */
  public int getThreshold() {
    return threshold;   
  }
  
  /**
   * Sets the value of the threshold property.
   * 
   * @param aThreshold the new value of the threshold property
   */
  public void setThreshold(int aThreshold) {
    if(0 <= threshold && threshold <= 100) {
      threshold = aThreshold;
    } else {
      throw new IllegalArgumentException("Threshold must be in the interval <1..100> inclusive. Actual Value: " + aThreshold);
    }   
  }
  
  /**
   * Determines if the usageWeighted property is true.
   * 
   * @return   <code>true<code> if the usageWeighted property is true
   */
  public boolean getUsageWeighted() {
    return usageWeighted;   
  }
  
  /**
   * Sets the value of the usageWeighted property.
   * 
   * @param aUsageWeighted the new value of the usageWeighted property
   */
  public void setUsageWeighted(boolean aUsageWeighted) {
    usageWeighted = aUsageWeighted;   
  }
  
  /**
   * @return boolean
   */
  public boolean strike() {
    switch(threshold) {
    case 0:
      return false;
    case 100:
      return true;
    default:
      int guess = generator.nextInt(100);
      if(guess >= threshold)
	return false;
      else
	return true;
    }   
  }
  
  /**
   * @return java.lang.String
   */
  public String nextHint() {
    int sz = allkeys.size();
    int hintIdx = generator.nextInt(sz);
    if(usageWeighted) {
      int leftNB;
      if(hintIdx == 0)
	leftNB = sz - 1;
      else
	leftNB = hintIdx - 1;
      int rightNB = (hintIdx+1)%sz;
      //check if hintIdx is over-used
      if(stats[hintIdx] >= (stats[leftNB] + stats[rightNB])/2) {
	//equalization
	hintIdx = (stats[leftNB] > stats[rightNB]) ? rightNB : leftNB;
      }
    }
    stats[hintIdx]++;
    return hints.getString((String)allkeys.get(hintIdx));   
  }
  
  /**
   * @return java.util.Map
   */
  public Map getStats() {
    HashMap sts = new HashMap();
    int sz = allkeys.size();
    for(int i=0; i<sz; i++) {
      Object key = allkeys.get(i);
      int count = stats[i];
      sts.put(key, new Integer(count));
    }
    return sts;   
  }
  
  /**
   * Access method for the hints property.
   * @return   the current value of the hints property
   */
  public ResourceBundle getHints() {
    return hints;   
  }
  
  /**
   * Sets the value of the hints property.
   * @param aHints the new value of the hints property
   */
  public void setHints(ResourceBundle aHints) {
    if(aHints == null) {
      throw new IllegalArgumentException("Hints property can not be null!");
    } else {
      hints = aHints;
      allkeys.clear();
      stats = null;
      Enumeration keys = hints.getKeys();
      while(keys.hasMoreElements()) {
	Object akey = keys.nextElement();
	allkeys.add(akey);
      }
      stats = new int[allkeys.size()];
      for(int i=0; i<stats.length; i++) {
	stats[i] = 0;
      }
    }   
  }
  
  /**
   * @param sts a map of (hintKey, playCount) where playCount can be
   * either Integer or String
   */
  public void setStats(Map sts) {
    if(allkeys.size() == 0) {
      throw new IllegalStateException("Hints is an empty collection, can not apply usage statistics!");
    } else {
      for(int i=0; i<allkeys.size(); i++) {
	String akey = (String)allkeys.get(i);
	//we should handle both Integer and String
	Object count = sts.get(akey);
	if(count == null) {
	  continue;
	} else if(count instanceof Integer) {
	  stats[i] = ((Integer)count).intValue();
	} else if(count instanceof String) {
	  stats[i] = Integer.parseInt((String)count);
	} else {
	  throw new IllegalArgumentException(count.getClass().getName() +
					 " can not serve as usage count");
	}
      }
    }   
  }
}
