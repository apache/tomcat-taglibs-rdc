<%--$Id$--%>
<!--
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ tag body-content="empty" %>

<%@ attribute name="model" required="true" type="java.lang.Object" %>
<%@ attribute name="defaultConfig" required="true" %>
<%@ attribute name="config" required="true" %>
-->
<rdc:comment>
  model is the bean of the component being configured.
  default gives the jar entry for the default configuration.
  config holds the user-specified location if any
</rdc:comment>
<c:choose>
  <c:when test="${empty config}">
    <rdc:getDefaultConfig name="${defaultConfig}" model="${model}" />
  </c:when>
  <c:otherwise>
    <c:import varReader="xmlSource" url="${config}">
      <x:parse var="configuration" doc="${xmlSource}"/> 
      <c:set target ="${model}" property="configuration"
      value="${configuration}"/>	
    </c:import>
  </c:otherwise>
</c:choose>
