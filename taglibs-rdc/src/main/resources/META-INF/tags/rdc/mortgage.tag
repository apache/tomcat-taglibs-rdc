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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.1" %>

<%@ tag body-content="empty" %>

<%@ attribute name="id" required="true" rtexprvalue="false" %>
<%@ attribute name="submit" required="false" %>
<%@ attribute name="config" required="false" %>
<%@ attribute name="initial" required="false" %>
<%@ attribute name="confirm" required="false" %>
<%@ attribute name="echo" required="false" %>
<%@ attribute name="locale" required="false" %>
<%@ attribute name="subdialog" required="false" %>
<%@ variable name-from-attribute="id" alias="retVal" scope="AT_END"%>

<jsp:useBean id="constants" class="org.apache.taglibs.rdc.core.Constants" />

<rdc:peek var="stateMap" stack="${requestScope.rdcStack}"/>
<c:choose>
  <c:when test="${empty stateMap[id]}">
    <rdc:comment>This instance is being called for the first time in this session </rdc:comment>
    <jsp:useBean id="model"
      class="org.apache.taglibs.rdc.Mortgage" >
      <c:set target="${model}" property="state"
      value="${stateMap.initOnlyFlag == true ? constants.FSM_INITONLY : constants.FSM_INPUT}"/>
      <rdc:comment> initialize bean from our attributes </rdc:comment>
      <c:set target="${model}" property="id" value="${id}"/>
      <c:set target="${model}" property="confirm" value="${confirm}"/>
      <c:set target="${model}" property="echo" value="${echo}"/>
      <c:set target="${model}" property="initial" value="${initial}"/>
      <c:set target="${model}" property="subdialog" value="${subdialog}"/>
      <rdc:setup-results model="${model}" submit="${submit}" />
      <rdc:set-config-composite model="${model}" context="${pageContext}" config="${config}" />
    </jsp:useBean>
    <rdc:comment> cache away this instance for future requests in this session </rdc:comment>
    <c:set target="${stateMap}" property="${id}" value="${model}"/>
  </c:when>
  <c:otherwise>
    <rdc:comment> retrieve cached bean for this instance </rdc:comment>
    <c:set var="model" value="${stateMap[id]}"/>
  </c:otherwise>
</c:choose>

<rdc:comment>Push on stack, composite's localMap for next level of recursion </rdc:comment>
<rdc:push stack="${rdcStack}" element="${model.localMap}"/>

<c:choose>
	<c:when test="${model.state == constants.FSM_INITONLY || model.state == constants.FSM_DORMANT}">
   	 <%-- (initOnly -> initOnly) --%>
   	 <%-- (dormant -> dormant) --%>
	</c:when>
  
	<c:when test="${model.state == constants.FSM_INPUT}">
	  <rdc:group id="mortgage" strategy="org.apache.taglibs.rdc.dm.SimpleDirectedDialog"
	   submit="${model.submit}" >
	   
	    <rdc:mortgageType id="mortgageType" minTerm="10Yfixed" maxTerm="50Yfixed"
	     initial="20Yfixed" confirm="${model.confirm}" echo="${model.echo}" 
	     minConfidence="0.4F" numNBest="5" 
         locale="${model.locale}" config="${model.configMap.mortgageType}" />
         
	    <rdc:percent id="percent" minPercent="5" maxPercent="55" initial="10"
	     confirm="${model.confirm}" echo="${model.echo}" minConfidence="0.4F" numNBest="5" 
         locale="${model.locale}" config="${model.configMap.percent}" />
         
  	  </rdc:group>		
	</c:when>
</c:choose>

<rdc:comment>If Group is done</rdc:comment>
<c:if test="${model.localMap.mortgage.state == constants.GRP_STATE_DONE}">
	
    <rdc:comment> Populate the data model of component using HashMap returned by Group </rdc:comment>
    <jsp:useBean id="data" class="org.apache.taglibs.rdc.MortgageData" >
	<c:set target="${data}" property="mortgageType"	value="${mortgage.mortgageType}"/>
	<c:set target="${data}" property="percent" value="${mortgage.percent}"/>
    </jsp:useBean>
	
    <c:set target="${model}" property="value" value="${data}"/>
	
    <c:if test="${model.isValid == true}">
       	<c:set target="${model}" property="state" value="${constants.FSM_DONE}"/>
        <c:set var="retVal" value="${model.value}"/>
        <rdc:subdialog-return model="${model}"/>
    </c:if>

</c:if>

<rdc:pop var="discard" stack="${rdcStack}"/>
<!--Example:End-->
