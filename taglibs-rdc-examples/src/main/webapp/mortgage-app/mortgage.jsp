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
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.0"%>
-->
<vxml version="2.0" xml:lang="en-US"  xmlns="http://www.w3.org/2001/vxml" >
  <jsp:useBean id="dialogMap" class="java.util.LinkedHashMap" scope="session"/>
  <rdc:task map="${dialogMap}">

     <c:if test="${!(empty mlsPropertyValue)}">
       <block>
         <prompt>The value of the property with M. L. S. number 
         <say-as interpret-as="vxml:digits">${mlsNumber}</say-as> is 
         <say-as interpret-as="vxml:currency">USD${mlsPropertyValue}</say-as>
         <break time="300ms"/>Now let us set up the mortgage details.</prompt>
       </block>
     </c:if>
  
     <rdc:mortgage id="mortgage" config="/config/mortgage-cfg.xml" />

  </rdc:task>
</vxml>
   	 
<c:if test="${!(empty mortgage)}">
  <rdc:struts-submit submit="/mortgage.do" context="${pageContext}" namelist="mortgage" />
</c:if>
<!--Example:End-->
