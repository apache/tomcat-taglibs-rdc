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
<%@ page language="java" contentType="application/vxml" %>
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.0"%>
-->
<vxml version="2.0" xml:lang="en-US"  xmlns="http://www.w3.org/2001/vxml" >
 <jsp:useBean id="dialogMap"  class="java.util.LinkedHashMap" scope="session"/>
 <rdc:task map="${dialogMap}">

  <rdc:group id="parentGrp" strategy="org.apache.taglibs.rdc.dm.RuleBasedDirectedDialog"
   config="config/rule-based-dialog/parent.xml" >

    <rdc:group id="childGrp2" strategy="org.apache.taglibs.rdc.dm.RuleBasedDirectedDialog"
     config="config/rule-based-dialog/child2.xml" >
      <rdc:date id="date2" minDate="01012005" maxDate="01012015" confirm="true" echo="true"/>
    </rdc:group>
    
    <rdc:group id="childGrp1" strategy="org.apache.taglibs.rdc.dm.RuleBasedDirectedDialog"
     config="config/rule-based-dialog/child1.xml" >
      <rdc:date id="date1" minDate="01012005" maxDate="01012010" confirm="true" echo="true"/>
      <rdc:select1 id="select" optionList="config/rule-based-dialog/trip-type-opt.xml" />
    </rdc:group>

  </rdc:group>

 </rdc:task>
</vxml>
<!--Example:End-->
