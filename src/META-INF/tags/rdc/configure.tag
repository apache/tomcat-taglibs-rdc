<%--
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
--%>
<%--$Id$--%>
<!--
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
    <rdc:get-default-config name="${defaultConfig}" model="${model}" />
  </c:when>
  <c:when test="${fn:startsWith(config, 'META-INF/')}">
    <rdc:get-default-config name="${config}" model="${model}" />
  </c:when>
  <c:otherwise>
    <c:catch var="parse_failed">
      <c:import varReader="xmlSource" url="${config}">
        <x:parse var="configuration" doc="${xmlSource}"/> 
        <c:set target ="${model}" property="configuration"
         value="${configuration}"/>	
      </c:import>
    </c:catch>
    <c:if test="${not empty parse_failed}">
      <c:set var="error_message" 
       value="Could not parse configuration file at ${config} for R D C with I D ${model.id}" /> 
      <block>
        <prompt>An application error has occured. 
        ${error_message}</prompt>
        <exit expr="'${error_message}'"/>
      </block>
    </c:if>
  </c:otherwise>
</c:choose>
