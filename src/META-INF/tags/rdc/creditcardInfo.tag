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
<%@ attribute name="locale" required="false" %>
<%@ attribute name="subdialog" required="false" %>
<%@ attribute name="mode" required="false" %>
<%@ variable name-from-attribute="id" alias="retVal" scope="AT_END"%>

<jsp:useBean id="constants" class="org.apache.taglibs.rdc.core.Constants" />

<rdc:peek var="stateMap" stack="${requestScope.rdcStack}"/>
<c:choose>
  <c:when test="${empty stateMap[id]}">
    <rdc:comment>This instance is being called for the first time in this session </rdc:comment>
    <jsp:useBean id="model"
      class="org.apache.taglibs.rdc.CreditCardInfo" >
      <c:set target="${model}" property="state"
      value="${stateMap.initOnlyFlag == true ? constants.FSM_INITONLY : constants.FSM_INPUT}"/>
      <rdc:comment> initialize bean from our attributes </rdc:comment>
      <c:set target="${model}" property="id" value="${id}"/>
      <c:set target="${model}" property="confirm" value="${confirm}"/>
      <c:set target="${model}" property="echo" value="${echo}"/>
      <c:set target="${model}" property="locale" value="${locale}"/>
      <c:set target="${model}" property="initial" value="${initial}"/>
      <c:set target="${model}" property="mode" value="${mode}"/>
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
	  <rdc:group id="ccInfoGrp" strategy="org.apache.taglibs.rdc.dm.RuleBasedDirectedDialog"
       config="META-INF/tags/rdc/config/cardinfo-rules.xml" submit="${model.submit}">

	    <rdc:creditcardType id="ccType" confirm="true" echo="true" 
	     locale="${model.locale}" config="${model.configMap.creditcardType}" />

	    <rdc:creditcardNumber id="ccNumber" confirm="true" echo="true" 
	     locale="${model.locale}" config="${model.configMap.creditcardNumber}" />

	    <rdc:creditcardExpiry id="ccExpiry" confirm="true" echo="true" 
	     locale="${model.locale}" config="${model.configMap.creditcardExpiry}" />

	    <rdc:digits id="ccSecurityCode" minLength="3" maxLength="7"
         locale="${model.locale}" confirm="true" echo="true" 
         config="${not empty model.configMap.creditcardSecurityCode ?
         model.configMap.creditcardSecurityCode :
         'META-INF/tags/rdc/config/cardsecuritycode.xml' }" />

  	  </rdc:group>		
	</c:when>
</c:choose>

<rdc:comment>If Group is done</rdc:comment>
<c:if test="${model.localMap.ccInfoGrp.state == constants.GRP_STATE_DONE}">
	
    <rdc:comment> Populate the data model of component using HashMap returned by Group </rdc:comment>
    <jsp:useBean id="data" class="org.apache.taglibs.rdc.CreditCardData" >
	<c:set target="${data}" property="type" value="${ccInfoGrp.ccType}"/>
	<c:set target="${data}" property="number" value="${ccInfoGrp.ccNumber}"/>
	<c:set target="${data}" property="expiry" value="${ccInfoGrp.ccExpiry}"/>
	<c:set target="${data}" property="securityCode" value="${ccInfoGrp.ccSecurityCode}"/>
    </jsp:useBean>
	
    <c:set target="${model}" property="value" value="${data}"/>
	
    <c:if test="${model.isValid == true}">
   	    <c:set target="${model}" property="state" value ="${constants.FSM_DONE}"/>
        <c:choose>
            <c:when test="${not model.subdialog}">
                <c:set var="retVal" value="${model.value}"/>
            </c:when>
            <c:otherwise>
                <block>
                    <var name="${model.id}" expr="'${model.serializedValue}'"/>
                    <return namelist="${model.id}"/>
                </block>
            </c:otherwise>
        </c:choose>
    </c:if>

</c:if>

<rdc:pop var="discard" stack="${rdcStack}"/>
<!--Example:End-->
