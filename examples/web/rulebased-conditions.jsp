<!--Example:Start-->
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
<!--$Id$-->
<!--
<%@ page language="java" contentType="application/voicexml+xml" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.0"%>
-->
<vxml version="2.0" xml:lang="en-US"  xmlns="http://www.w3.org/2001/vxml" >
  <jsp:useBean id="dialogMap"  class="java.util.LinkedHashMap" scope="session"/>
  <rdc:task map="${dialogMap}">

    <%-- 
    Following bean created to illustrate the use of EL expressions in
    group navigation rules
    --%>
    <jsp:useBean id="myBean" class="java.util.HashMap" scope="request" >
      <c:set target="${myBean}" property="myProperty" value="time"/>
    </jsp:useBean>
   
    <rdc:group id="myGroup" strategy="org.apache.taglibs.rdc.dm.RuleBasedDirectedDialog"
     config="config/rule-based-dialog/conditional-execution.xml" >
      <rdc:time id="time" confirm="true" echo="true"/>
      <rdc:duration id="duration" confirm="true" echo="true"/>
      <rdc:date id="date" minDate="01012005" maxDate="01012008" confirm="true" echo="true"/>
      <rdc:select1 id="select" optionList="config/rule-based-dialog/conditional-exec-opt.xml" />
    </rdc:group>	

  </rdc:task>
</vxml>
<!--Example:End-->
