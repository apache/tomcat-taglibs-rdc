<!--Example:Start-->
<!--$Id$-->

<% response.setHeader("Cache-Control", "no-cache"); %>
<%@ page language="java" contentType="application/vxml" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.0"%>

<vxml version="2.0" xml:lang="en-US"  xmlns="http://www.w3.org/2001/vxml" >

  <jsp:useBean id="rdcStack" class="java.util.Stack" scope="request"/>
  <jsp:useBean id="dialogMap"  class="java.util.LinkedHashMap" scope="session"/>
  <rdc:push stack="${rdcStack}" element="${dialogMap}"/>
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
      <c:otherwise>
        <c:set var="mortgageOK" value="${param['mortgageOK']}" />
        <c:set var="memberNumber" value="${sessionScope.appBean.memberNumber}" />
        <c:set var="mlsPropertyValue" value="${sessionScope.appBean.propertyValue}" />
        <c:set var="transferAmount" value="${sessionScope.appBean.downPayment}" />
        <c:set var="submitURI" value="/demo/transaction-confirm.jsp"/>
 	    <rdc:struts-submit submit="/mortgage-rate.do" context="${pageContext}" 
 	     namelist="mortgageOK memberNumber mlsPropertyValue transferAmount submitURI" />
	  </c:otherwise>
    </c:choose>

  </form>
  
  <rdc:pop var="discard" stack="${rdcStack}"/>
</vxml>
<!--Example:End-->
