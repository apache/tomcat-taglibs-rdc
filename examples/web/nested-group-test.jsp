<!--Example:Start-->
<!--$Id$-->

<!--
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.0"%>
-->

<vxml version="2.0" xml:lang="en-US"  xmlns="http://www.w3.org/2001/vxml" >

  <jsp:useBean id="rdcStack" class="java.util.Stack" scope="request"/>
  <jsp:useBean id="dialogMap"  class="java.util.LinkedHashMap" scope="session"/>

  <rdc:push stack="${rdcStack}" element="${dialogMap}"/>
  <c:set var="URI" value="${pageContext.request.requestURI}"/>
  <c:if test="${fn:startsWith(URI, pageContext.request.contextPath)}">
    <c:set var="URI" value="${fn:substringAfter(URI, pageContext.request.contextPath)}"/>
  </c:if>
  <c:url var="submit" value="${URI}"/>

  <form>    
    <rdc:group id="pickup" strategy="org.apache.taglibs.rdc.dm.SimpleDirectedDialog" submit="${submit}" >
      <rdc:group id="trip" strategy="org.apache.taglibs.rdc.dm.SimpleDirectedDialog" submit="${submit}" >
        <rdc:date id="outDate" minDate="01012004" maxDate="01012005" />
        <rdc:date id="inDate" minDate="01012004" maxDate="01012005" />
      </rdc:group>	
      <rdc:date id="pickupDate" minDate="01012004" maxDate="01012005" />
      <rdc:time id="pickupTime" minTime="xxxxxx" maxTime="xx30xx" />
      <rdc:duration id ="pickupDuration" minDuration="P04D" maxDuration="P09D" />
    </rdc:group>
  </form>

</vxml>
<rdc:pop var="discard" stack="${rdcStack}"/>
<!--Example:End-->
