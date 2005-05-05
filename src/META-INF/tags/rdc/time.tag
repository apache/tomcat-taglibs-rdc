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

<%@ attribute name="id" required="true" rtexprvalue="false" %>
<%@ attribute name="submit" required="false" %>
<%@ attribute name="config" required="false" %>
<%@ attribute name="initial" required="false" %>
<%@ attribute name="confirm" required="false" %>
<%@ attribute name="echo" required="false" %>
<%@ attribute name="minTime" required="false" %>
<%@ attribute name="maxTime" required="false" %>
<%@ attribute name="minConfidence" required="false" %>
<%@ attribute name="numNBest" required="false" %>
<%@ attribute name="maxNoInput" required="false" %>
<%@ attribute name="maxNoMatch" required="false" %>
<%@ variable name-from-attribute="id" alias="retVal" scope="AT_END"%>
-->

<rdc:comment>
  Description: Collect, disambiguate, validate and confirm a time.
  Attributes:
  id: unique identifier for the atom
  submit: the submit location
  config: the configuration file location 
  initial: initial value
  confirm: boolean value indicating whether the input should be confirmed
  echo: boolean value indicating whether to echo back the result on completion
  minTime: minimum acceptable time
  maxTime: maximum acceptable time

  </rdc:comment>

<rdc:peek var="stateMap" stack="${requestScope.rdcStack}"/>
<jsp:useBean id="constants" class="org.apache.taglibs.rdc.core.Constants" />

<rdc:comment>
  Bean model holds the private data model of this component.
  It is created when the component is called the first time,
  and is found in subsequent requests  in stateMap[id].
  stateMap is in session scope and holds (directly or indirectly)
  the transitive closure
  of all components.
</rdc:comment>

<c:choose>
  <c:when test="${empty stateMap[id]}">
    <rdc:comment>This instance is being called for the first time in 
    this session</rdc:comment>
    <jsp:useBean id="model"
      class="org.apache.taglibs.rdc.Time" >
      <c:set target="${model}" property="state"
      value="${stateMap.initOnlyFlag == true ? constants.FSM_INITONLY : constants.FSM_INPUT}"/>
      <rdc:comment>initialize bean from our attributes</rdc:comment>
      <c:set target="${model}" property="id" value="${id}"/>
      <c:set target="${model}" property="confirm" value="${confirm}"/>
      <c:set target ="${model}" property="maxTime" value="${maxTime}"/>
      <c:set target ="${model}" property="minTime" value="${minTime}"/>
      <c:set target ="${model}" property="initial" value="${initial}"/>
      <c:set target ="${model}" property="submit" value="${submit}"/>
      <c:set target ="${model}" property="echo" value="${echo}"/>
      <jsp:useBean id="voice_grammar"
       class="org.apache.taglibs.rdc.core.Grammar" >
          <c:set target="${voice_grammar}" property="grammar"
           value="${pageContext.request.contextPath}/.grammar/time.grxml"/>
      </jsp:useBean>
      <c:set target="${model}" property="grammar" value="${voice_grammar}"/>
      <rdc:configure model="${model}" config="${config}" 
        defaultConfig="META-INF/tags/rdc/config/time.xml" />
      <rdc:setup-results model="${model}" submit="${submit}" 
        minConfidence="${minConfidence}" numNBest="${numNBest}"
        maxNoInput="${maxNoInput}" maxNoMatch="${maxNoMatch}" />
    </jsp:useBean>
    <rdc:comment>cache away this instance for future requests in this 
    session</rdc:comment>
    <c:set target="${stateMap}" property="${id}" value="${model}"/>
  </c:when>
  <c:otherwise>
    <rdc:comment>retrieve cached bean for this instance</rdc:comment>
    <c:set var="model" value="${stateMap[id]}"/>
  </c:otherwise>
</c:choose>

<rdc:extract-params target="${model}" parameters="${model.paramsMap}"/>

<c:choose>
  <c:when test="${model.state == constants.FSM_VALIDATE and model.isAmbiguous == true}">
    <field name="${model.id}Input">
      <rdc:expand>
        <rdc:get-configuration xml="${model.configuration}"
          locator="/config/validate/handler[@errorCode='disambiguate']/prompt" />
      </rdc:expand>
      <prompt><enumerate /></prompt>
      <c:forEach items="${model.ambiguousValues}" var="options">
        <option value="${options.key}"> ${options.value} </option>
      </c:forEach>
      <filled>
        <c:if test="${!model.skipSubmit}">
          <script src="${pageContext.request.contextPath}/.grammar/nbest.js"/>
          <var name="${model.id}ResultNBest" expr="serializeNBest()"/>
          <submit next="${model.submit}" method="post" namelist="${model.id}ResultNBest"/>
        </c:if>
      </filled>
    </field>
  </c:when>
  <c:otherwise>
    <rdc:fsm-run  model="${model}"/>
  </c:otherwise>
</c:choose>
              
<c:if test="${model.state == constants.FSM_DONE}">
  <c:set var="retVal" value="${model.value}"/>
</c:if>
