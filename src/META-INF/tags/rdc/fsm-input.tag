<%--$Id$--%>
<!--
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.0"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ tag body-content="empty" %>

<%@ attribute name="model" required="true" type="java.lang.Object" %>
-->
<field name="${model.id}Input">
  <rdc:get-configuration xml="${model.configuration}"
  locator="/config/input/prompt-list/prompt"/> 
  <c:forEach items="${model.grammars}" var="current">
    <c:choose>
      <c:when test="${fn:startsWith(current, '<grammar')}">
        <c:out value="${current}" escapeXml="false" />
      </c:when>
      <c:otherwise>
        <grammar xml:lang="en-US" src="${current}" 
 		<c:if test="${fn:contains(current, '-dtmf.')}">
 			mode="dtmf"
 		</c:if>
        />
      </c:otherwise>
    </c:choose>
  </c:forEach>
  <property name="maxnbest" value="${model.numNBest}"/>
  <rdc:get-configuration xml="${model.configuration}"
  locator="/config/input/property-list/property"/> 	
  <catch event= "repeat" >
    <reprompt/>
  </catch>
  <c:if test="${model.className == 'org.apache.taglibs.rdc.SelectOne'}">
    <rdc:get-configuration xml="${model.options}" locator="/list/option"/>
  </c:if>
  <rdc:get-configuration xml="${model.configuration}"
  locator="/config/input/noinput-list/noinput"/>
  <rdc:get-configuration xml="${model.configuration}"
  locator="/config/input/nomatch-list/nomatch"/>
  <rdc:get-configuration xml="${model.configuration}"
  locator="/config/input/help-list/help"/>		
  <filled>
  <c:if test="${!model.skipSubmit}">
    <script src="${pageContext.request.contextPath}/.grammar/nbest.js"/>
    <var name="${model.id}ResultNBest" expr="serializeNBest()"/>
    <submit next="${model.submit}" namelist="${model.id}ResultNBest"/>
  </c:if>
  </filled>
</field>
