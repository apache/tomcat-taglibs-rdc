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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.0"%>
-->
<vxml version="2.0" xml:lang="en-US"  xmlns="http://www.w3.org/2001/vxml" >

  <c:url var="submit" value="${pageContext.request.servletPath}" />
 
  <form>
    <c:choose>
      <c:when test="${empty param['mortgageOK']}">
        <block>
          <prompt>The monthly installment for your mortgage will be
          <say-as interpret-as="vxml:currency">USD${monthlyInstallment}</say-as></prompt>
        </block>
        
        <field name="mortgageOK" type="boolean">
          <prompt>Would you like to proceed?</prompt>
          <filled>
            <submit next="${submit}" namelist="mortgageOK"/>
          </filled>
        </field>
      </c:when>
      <c:when test="${param['mortgageOK'] == true}">
        <c:set var="mortgageOK" value="${param['mortgageOK']}" />
        <c:set var="memberNumber" value="${appBean.memberNumber}" />
        <c:set var="mlsPropertyValue" value="${appBean.propertyValue}" />
        <c:set var="transferAmount" value="${appBean.downPayment}" />
 	  <rdc:struts-submit submit="/mortgage-rate.do" context="${pageContext}" 
 	   namelist="mortgageOK memberNumber mlsPropertyValue transferAmount" />
	</c:when>
	<c:otherwise>
        <jsp:forward page="goodbye.jsp" />
	</c:otherwise>
    </c:choose>
  </form>

</vxml>
<!--Example:End-->
