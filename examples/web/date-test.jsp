<!--Example:Start-->
<!--$Id$-->

<!--
<%@ page language="java" contentType="application/vxml" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.0"%>
-->

<vxml version="2.0" xml:lang="en-US"  xmlns="http://www.w3.org/2001/vxml" >

  <jsp:useBean id="rdcStack" class="java.util.Stack"
               scope="request"/>
  
  <jsp:useBean id="dialogMap"  class="java.util.LinkedHashMap"
               scope="session"/>

  <rdc:push stack="${rdcStack}" element="${dialogMap}"/>

  <form>    
    <rdc:date id="date" minDate="01012004" maxDate="01012005" initial="xx01xxxx"
      confirm="true" echo="true" minConfidence="40.0F" numNBest="5" />
  </form>

</vxml>
<rdc:pop var="discard" stack="${rdcStack}"/>
<!--Example:End-->
