<!--Example:Start-->
<%--
  Copyright 2004 The Apache Software Foundation.
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
--%> 
<!--$Id$-->
<!--
<%@ page language="java" contentType="application/vxml" %>
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.0"%>
-->
<vxml version="2.0" xml:lang="en-US"  xmlns="http://www.w3.org/2001/vxml" >
  <jsp:useBean id="dialogMap" class="java.util.LinkedHashMap" scope="session"/>
  <rdc:task map="${dialogMap}">

    <rdc:group id="allAtoms" strategy="org.apache.taglibs.rdc.dm.SimpleDirectedDialog">
 
      <rdc:date id="date" minDate="01012005" maxDate="01012008" initial="xx01xxxx"
       confirm="true" echo="true" minConfidence="0.4F" numNBest="5" />
    
      <rdc:time id="time" minTime="xxxxxx" maxTime="xx60xx"
       confirm="true" echo="true"  minConfidence="0.4F" numNBest="3" />
      
      <rdc:alpha id="alpha" minLength="2" maxLength="8" numNBest="5" echo="true"/>
       
      <rdc:alphanum id="alphanum" minLength="2" maxLength="7" pattern="[0-9]*"
       confirm="true" echo="true"/>
       
      <rdc:select1 id="test" optionList="config/cardtype-opt.xml"
       confirm="true" minConfidence="0.4F" numNBest="4" initial="hsbc" />

      <rdc:select1 id="typeOfEquipment" config="config/equipment-type/equipment-cfg.xml"
       optionList="config/equipment-type/equipment-opt.xml" confirm="true"
       minConfidence="0.4F" numNBest="6" echo="true" />

      <rdc:usMajorCity id="ourCity" confirm="true" echo="true"
       minConfidence="0.4F" numNBest="4" />	
        
      <rdc:usState id="ourState" confirm="true" echo="true"
       minConfidence="0.4F" numNBest="5"/>
       
      <rdc:zipCode id="ourZipCode" length="5" confirm="true" echo="true" 
       minConfidence="0.4F" numNBest="5"/>
        
      <rdc:country id="country" confirm="true" echo="true"
       minConfidence="0.4F" numNBest="5" initial = "Argentina" />
      
      <rdc:ssn id="myssn" confirm="true" echo="true" minConfidence="0.4F" numNBest="5"/>
       
      <rdc:creditcardType id="payingcard" numNBest="3" confirm="true" echo="true" initial = "visa" />
  
      <rdc:select1 id="payment" config="config/payment-method/payment-method-cfg.xml" 
  	 optionList="config/payment-method/payment-method-opt.xml" confirm="true"
  	 minConfidence="0.4F" numNBest="6" echo="true" />
      
      <rdc:currency id="currency" confirm="true" minValue="50.20"
       maxValue="1200.30" currencyCode="USD" echo="true" minConfidence="0.4F" numNBest="5" />

      <rdc:duration id="duration" minDuration="P00Y02M00D" maxDuration="P00Y09M00D"
       confirm="true" echo="true" minConfidence="0.4F" numNBest="5" initial="P00Y03M00D" />

	<rdc:isbn id="isbn" confirm="true" echo="true"/>
  	
    </rdc:group>

  </rdc:task>
</vxml>
<!--Example:End-->
