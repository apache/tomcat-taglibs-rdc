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
<%--$Id$--%>
<!--
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.0"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ tag body-content="empty" %>

<%@ attribute name="model" required="true" type="java.lang.Object" %>
<%@ attribute name="state" type="java.lang.String" %>
-->
<c:set var="stateNode" value="${(empty state) ? 'input' : state}" />
<rdc:expand>
  <rdc:get-configuration xml="${model.configuration}"
   locator="/config/${stateNode}/events/link"/>
  <rdc:get-configuration xml="${model.configuration}"
   locator="/config/${stateNode}/events/catch"/>
</rdc:expand>
<field name="${model.id}Input">
  <rdc:expand>
    <rdc:get-configuration xml="${model.configuration}"
     locator="/config/${stateNode}/prompt-list/prompt"/> 
  </rdc:expand>
  <c:forEach items="${model.grammars}" var="currentGrammar">
    <c:choose>
      <c:when test="${currentGrammar.isInline == true}">
        <c:out value="${currentGrammar.grammar}" escapeXml="false" />
      </c:when>
      <c:otherwise>
        <grammar xml:lang="en-US" src="${currentGrammar.grammar}" 
                 mode="${(currentGrammar.isDTMF == true) ? 'dtmf' : 'voice'}" />
      </c:otherwise>
    </c:choose>
  </c:forEach>
  <property name="maxnbest" value="${model.numNBest}"/>
  <property name="confidencelevel" value="${model.minConfidence}"/>
  <rdc:get-configuration xml="${model.configuration}"
   locator="/config/${stateNode}/property-list/property"/> 
  <catch event= "repeat" >
    <reprompt/>
  </catch>
  <c:if test="${model.className == 'org.apache.taglibs.rdc.SelectOne'}">
    <c:if test="${model.optionsClass == 'org.w3c.dom.Document'}">
      <rdc:get-configuration xml="${model.options}" locator="/list/option"/>
    </c:if>
    <c:if test="${model.optionsClass == 'org.apache.taglibs.rdc.SelectOne$Options'}">
      ${model.options}
    </c:if>
  </c:if>
  <c:if test="${model.maxNoInput > 0}">
    <noinput count="${model.maxNoInput}">
      <var name="${model.id}ResultNBest" expr="'MAX_NOINPUT'"/>
      <submit next="${model.submit}" method="post" namelist="${model.id}ResultNBest"/>
    </noinput>  
  </c:if>
  <rdc:get-configuration xml="${model.configuration}"
   locator="/config/${stateNode}/noinput-list/noinput"/>
  <c:if test="${model.maxNoMatch > 0}">
    <nomatch count="${model.maxNoMatch}">
      <var name="${model.id}ResultNBest" expr="'MAX_NOMATCH'"/>
      <submit next="${model.submit}" method="post" namelist="${model.id}ResultNBest"/>
    </nomatch>  
  </c:if>
  <rdc:get-configuration xml="${model.configuration}"
   locator="/config/${stateNode}/nomatch-list/nomatch"/>
  <rdc:get-configuration xml="${model.configuration}"
   locator="/config/${stateNode}/help-list/help"/>		
  <filled>
  <c:if test="${!model.skipSubmit}">
    <script src="${pageContext.request.contextPath}/.grammar/nbest.js"/>
    <var name="${model.id}ResultNBest" expr="serializeNBest()"/>
    <submit next="${model.submit}" method="post" namelist="${model.id}ResultNBest"/>
  </c:if>
  </filled>
</field>
