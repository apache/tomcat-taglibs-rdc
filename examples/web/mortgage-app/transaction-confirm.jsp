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
<vxml version="2.0" xml:lang="en-US"  xmlns="http://www.w3.org/2001/vxml">

  <c:url var="submit" value="${pageContext.request.servletPath}"/>
 
  <form>

	<c:choose>
	
      <c:when test="${param['confirmation'] == true}">
        <block>
          <prompt>Your transaction number is ${appBean.transactionNum}
          <break time="500ms"/> Thank you for calling. Goodbye.</prompt>
        </block>
      </c:when>

      <c:when test="${param['confirmation'] == false}">
        <block>
          <prompt>Thank you for calling. Goodbye.</prompt>
        </block>
      </c:when>

      <c:otherwise>
      
        <jsp:useBean id="rdcStack" class="java.util.Stack" scope="request"/>
        <jsp:useBean id="dialogMap"  class="java.util.LinkedHashMap" scope="session"/>
        <rdc:push stack="${rdcStack}" element="${dialogMap}"/>

        <c:if test="${empty dialogMap.notificationOption}" >
          <block>
      	    <prompt>A deposit in the amount of <say-as interpret-as="vxml:currency">
      	    USD${appBean.downPayment}</say-as> 
                has been made to the escrow account.</prompt>
          </block>
        </c:if>

        <rdc:select1 id="notificationOption" minConfidence="0.4F" numNBest="4" initial="email"
         optionList="/config/notification-type/notification-type-opt.xml" 
         config="/config/notification-type/notification-type-cfg.xml" />

        <rdc:pop var="discard" stack="${rdcStack}"/>

        <c:if test="${!(empty notificationOption)}">
          <block><prompt>Will do that. And I will send the Realtors an email of this.</prompt></block>
	      <block>
		    <prompt>Your transaction number is ${appBean.transactionNum}</prompt>
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
  
</vxml>
<!--Example:End-->
