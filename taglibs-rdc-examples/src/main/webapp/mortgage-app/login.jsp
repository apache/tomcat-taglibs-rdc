<!--Example:Start-->
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
<!--$Id$-->
<!--
<%@ page language="java" contentType="application/voicexml+xml" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.1"%>
-->
<jsp:useBean id="appBean" class="org.apache.taglibs.rdc.sampleapps.mortgage.MortgageAppBean"
 scope="session" />
<vxml version="2.0" xml:lang="en-US"  xmlns="http://www.w3.org/2001/vxml" >
  <jsp:useBean id="dialogMap" class="java.util.LinkedHashMap" scope="session"/>
  <rdc:task map="${dialogMap}">

    <c:if test="${empty dialogMap.login}">
      <block>
        <prompt>Welcome to the Automated Escrow application.</prompt>
      </block>
    </c:if>
    
    <rdc:struts-errors />

    <rdc:group id="login" strategy="org.apache.taglibs.rdc.dm.SimpleDirectedDialog" >
      <rdc:digits id="memberNumber" minLength="2" maxLength="9"
       config="/config/mortgage-app/member-number.xml" confirm="true" echo="true" />
      <rdc:digits id="mlsNumber" minLength="2" maxLength="9"
       config="/config/mortgage-app/mls-number.xml" confirm="true" echo="true" />
    </rdc:group>

  </rdc:task>
</vxml>
      
<c:if test="${!(empty mlsNumber)}">
  <rdc:struts-submit submit="/login.do" context="${pageContext}" namelist="memberNumber mlsNumber" />
</c:if>
<!--Example:End-->
