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
  <jsp:useBean id="appBean" class="org.apache.taglibs.rdc.struts.MortgageAppBean" scope="session" />
  <c:url var="submit" value="${pageContext.request.servletPath}" />

  <form>
	<c:if test="${empty dialogMap.login}">
      <block>
        <prompt>Welcome to the Automated Escrow application.</prompt>
      </block>
    </c:if>
    
    <rdc:group id="login" strategy="org.apache.taglibs.rdc.dm.SimpleDirectedDialog" submit="${submit}" >
      <rdc:digits id="memberNumber" minLength="2" maxLength="9"  config="/config/member-number.xml" 
        confirm="true" echo="true" />
      <rdc:digits id="mlsNumber" minLength="2" maxLength="9" config="/config/mls-number.xml" 
        confirm="true" echo="true" />
    </rdc:group>
      
    <c:if test="${!(empty mlsNumber)}">
    	<rdc:struts-submit submit="/login.do" context="${pageContext}" namelist="memberNumber mlsNumber" />
    </c:if>
  
  </form>

  <rdc:pop var="discard" stack="${rdcStack}"/>
  
</vxml>
<!--Example:End-->
