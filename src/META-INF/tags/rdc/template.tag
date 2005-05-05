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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.0" %>

<%@ tag body-content="empty" %>
<%@ tag dynamic-attributes="dynamicAttrs" %>

<%@ attribute name="id" required="true" rtexprvalue="false" %>
<%@ attribute name="grammar" required="true" type="java.lang.Object" %>
<%@ attribute name="config" required="true" %>
<%@ attribute name="data" required="false" type="java.lang.Object" %>
<%@ attribute name="bean" required="false" %>
<%@ attribute name="fsmFragment" required="false" %>
<%@ attribute name="constraints" required="false" type="java.lang.Object" %>
<%@ attribute name="initial" required="false" %>
<%@ attribute name="confirm" required="false" %>
<%@ attribute name="echo" required="false" %>
<%@ attribute name="submit" required="false" %>
<%@ attribute name="minConfidence" required="false" %>
<%@ attribute name="numNBest" required="false" %>
<%@ attribute name="maxNoInput" required="false" %>
<%@ attribute name="maxNoMatch" required="false" %>
<%@ variable name-from-attribute="id" alias="retVal" scope="AT_END"%>
-->

<rdc:peek var="stateMap" stack="${requestScope.rdcStack}"/>
<jsp:useBean id="constants" class="org.apache.taglibs.rdc.core.Constants" />

<c:choose>
  <c:when test="${empty stateMap[id]}">
    <rdc:comment>This instance is being called for the first time in this session </rdc:comment>
    <jsp:useBean id="model" type="org.apache.taglibs.rdc.core.RDCTemplate"
     beanName="${(empty bean) ? 'org.apache.taglibs.rdc.core.RDCTemplate' : bean}" >
      <c:set target="${model}" property="state"
      value="${stateMap.initOnlyFlag == true ? constants.FSM_INITONLY : constants.FSM_INPUT}"/>
      <rdc:comment> initialize bean from our attributes</rdc:comment>
      <c:set target="${model}" property="id" value="${id}"/>
      <c:set target="${model}" property="grammar" value="${grammar}"/>
      <c:set target="${model}" property="data" value="${data}"/>
      <c:set target="${model}" property="bean" value="${bean}"/>
      <c:set target="${model}" property="fsmFragment" value="${fsmFragment}"/>
      <c:set target="${model}" property="constraints" value="${constraints}"/>
      <c:set target="${model}" property="initial" value="${initial}"/>
      <c:set target="${model}" property="confirm" value="${confirm}"/>
      <c:set target="${model}" property="echo" value="${echo}"/>
      <c:set target="${model}" property="dynamicAttributes" value="${dynamicAttrs}"/>
      <c:import varReader="xmlSource" url="${config}">
        <x:parse var="configuration" doc="${xmlSource}"/> 
        <c:set target ="${model}" property="configuration"
         value="${configuration}"/>	
      </c:import>
      <rdc:setup-results model="${model}" submit="${submit}" 
        minConfidence="${minConfidence}" numNBest="${numNBest}"
        maxNoInput="${maxNoInput}" maxNoMatch="${maxNoMatch}" />
    </jsp:useBean>
    <rdc:comment>cache away this instance for future requests in this session </rdc:comment>
    <c:set target="${stateMap}" property="${id}" value="${model}"/>
  </c:when>
  <c:otherwise>
    <rdc:comment> retrieve cached bean for this instance </rdc:comment>
    <c:set var="model" value="${stateMap[id]}"/>
  </c:otherwise>
</c:choose>

<rdc:extract-params target="${model}" parameters="${model.paramsMap}"/>

<c:choose>
  <c:when test="${(!empty model.fsmFragment) and (model.isValid == false)}">
    <%-- 
      PageContext.include(String, boolean) doesn't work [Tomcat 5.0.28]
      The 'and' operation in the above condition will be removed once that
      is resolved.
    --%>
    <rdc:include-fsm-fragment template="${model}" context="${pageContext}" />
  </c:when>
  <c:otherwise>
    <rdc:fsm-run model="${model}" />
  </c:otherwise>
</c:choose>

<c:if test="${model.state == constants.FSM_DONE}">
  <c:set var="retVal" value="${model.value}"/>
</c:if>
