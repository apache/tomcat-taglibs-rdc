<!--Example:Start-->
<!--$Id$-->

<!--
<%@ page language="java" contentType="application/vxml" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.0"%>
-->

<vxml version="2.0" xml:lang="en-US"  xmlns="http://www.w3.org/2001/vxml" >

  <jsp:useBean id="rdcStack" class="java.util.Stack"
               scope="request"/>
  
  <jsp:useBean id="dialogMap"  class="java.util.LinkedHashMap"
               scope="session"/>

  <rdc:push stack="${rdcStack}" element="${dialogMap}"/>

  <c:url var="submit" value="${pageContext.request.servletPath}" />

  <form>
    <rdc:group id="allAtoms" strategy="org.apache.taglibs.rdc.dm.SimpleDirectedDialog" submit="${submit}" >
 
      <rdc:date id="date" minDate="01012004" maxDate="01012005" initial="xx01xxxx"
       confirm="true" echo="true" minConfidence="40.0F" numNBest="5" />
    
      <rdc:time id="time" minTime="xxxxxx" maxTime="xx45xx"
       confirm="true" echo="true"  minConfidence="40.0F" numNBest="3" />
      
      <rdc:alpha id="alpha" minLength="2" maxLength="8" numNBest="5" echo="true"/>
       
      <rdc:alphanum id="alphanum" minLength="2" maxLength="7" pattern="[0-9]*"
       confirm="true" echo="true"/>
       
      <rdc:select1 id="test" optionList="config/cardtype-opt.xml"
       confirm="true" minConfidence="40.0F" numNBest="4" initial="hsbc" />

      <rdc:select1 id="typeOfEquipment" config="config/equipment-type/equipment-cfg.xml"
       optionList="config/equipment-type/equipment-opt.xml" confirm="true"
       minConfidence="40.0F" numNBest="6" echo="true" />

      <rdc:usMajorCity id="ourCity" confirm="true" echo="true"
       minConfidence="40.0F" numNBest="4" />	
        
      <rdc:usState id="ourState" confirm="true" echo="true"
       minConfidence="40.0F" numNBest="5"/>
       
      <rdc:zipCode id="ourZipCode" length="5" confirm="true" echo="true" 
       minConfidence="40.0F" numNBest="5"/>
        
      <rdc:country id="country" confirm="true" echo="true"
       minConfidence="40.0F" numNBest="5" initial = "Argentina" />
      
      <rdc:ssn id="myssn" confirm="true" echo="true" minConfidence="40.0F" numNBest="5"/>
       
      <rdc:creditcardType id="payingcard" numNBest="3" confirm="true" echo="true" initial = "visa" />
  
      <rdc:select1 id="payment" config="config/payment-method/payment-method-cfg.xml" 
  	 optionList="config/payment-method/payment-method-opt.xml" confirm="true"
  	 minConfidence="40.0F" numNBest="6" echo="true" />
      
      <rdc:currency id="currency" confirm="true" minValue="50.20"
       maxValue="1200.30" currencyCode="USD" echo="true" minConfidence="40.0F" numNBest="5" />

      <rdc:duration id="duration" minDuration="P00Y02M00D" maxDuration="P00Y09M00D"
       confirm="true" echo="true" minConfidence="40.0F" numNBest="5" initial="P00Y03M00D" />

	<rdc:isbn id="isbn" confirm="true" echo="true"/>
  	
    </rdc:group>
  </form>

</vxml>
<rdc:pop var="discard" stack="${rdcStack}"/>
<!--Example:End-->
