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
<%@ page language="java" contentType="application/vxml" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.0"%>
-->
<vxml version="2.0" xml:lang="en-US"  xmlns="http://www.w3.org/2001/vxml" >
  <jsp:useBean id="dialogMap" class="java.util.LinkedHashMap" scope="session"/>
  <rdc:task map="${dialogMap}">

   <rdc:group id="scxmlGrp" strategy="org.apache.taglibs.rdc.dm.SCXMLDialog"
     config="${empty param.scxml ? 'config/scxml/scxml-test.xml' : param.scxml}">

      <rdc:time id="myTime" confirm="true" echo="true"/>

      <rdc:date id="myDate" confirm="true" echo="true"/>

      <rdc:select1 id="mySelect" maxNoInput="5" maxNoMatch="3"
       optionList="config/rule-based-dialog/conditional-exec-opt.xml" />
  	
    </rdc:group>

    <c:if test="${not empty scxmlGrp}">
      <block>
        <prompt>
          The choice selected was ${scxmlGrp.mySelect}
        </prompt>
      </block>
    </c:if>

  </rdc:task>
</vxml>
<!--Example:End-->
