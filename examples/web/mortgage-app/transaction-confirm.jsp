<!--Example:Start-->
<!--$Id$-->

<% response.setHeader("Cache-Control", "no-cache"); %>
<%@ page language="java" contentType="application/vxml" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.0"%>

<vxml version="2.0" xml:lang="en-US"  xmlns="http://www.w3.org/2001/vxml">

  <jsp:useBean id="rdcStack" class="java.util.Stack" scope="request"/>
  <jsp:useBean id="dialogMap"  class="java.util.LinkedHashMap" scope="session"/>
  <rdc:push stack="${rdcStack}" element="${dialogMap}"/>
  <c:url var="submit" value="${pageContext.request.servletPath}"/>
 
  <form>

	<c:choose>
	
      <c:when test="${param['confirmation'] == true}">
        <block>
          <prompt>Your transaction number is ${sessionScope.appBean.transactionNum}
          <break time="500ms"/> Goodbye.</prompt>
        </block>
      </c:when>

      <c:when test="${param['confirmation'] == false}">
        <block>
          <prompt>Thank you for calling. Goodbye.</prompt>
        </block>
      </c:when>

      <c:otherwise>
        <c:if test="${empty dialogMap.notificationOption}" >
          <block>
      	    <prompt>A deposit in the amount of <say-as interpret-as="vxml:currency">
      	    USD${sessionScope.appBean.downPayment}</say-as> 
                has been made to the escrow account.</prompt>
          </block>
        </c:if>
      
        <rdc:select1 id="notificationOption" minConfidence="40.0F" numNBest="4" initial="email"
         optionList="/config/notification-type/notification-type-opt.xml" 
         config="/config/notification-type/notification-type-cfg.xml" />

        <c:if test="${!(empty notificationOption)}">
          <block><prompt>Will do that. And I will send the Realtors an email of this.</prompt></block>
	      <block>
		    <prompt>Your transaction number is ${sessionScope.appBean.transactionNum}</prompt>
          </block>
	      <field name = "confirmation" type="boolean">
            <prompt><break time="300ms"/>Would you like me to repeat the transaction number?</prompt>
            <filled>
	          <submit next="${submit}" namelist="confirmation"/>
	        </filled>
          </field>
        </c:if>
      </c:otherwise>

    </c:choose>
	 
  </form>
  <rdc:pop var="temp" stack="${rdcStack}"/>
  
</vxml>
<!--Example:End-->

