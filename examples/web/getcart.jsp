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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
-->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<TITLE>MusicStore App - Get your shopping cart!</TITLE>
</HEAD>

<BODY>
 <H2>MusicStore Application - Shopping Cart Retrieval</H2>
 <c:if test="${!empty param['cartid']}">
  <c:set var="id" value="msCart${param['cartid']}" />
  <c:choose>
    <c:when test="${!empty applicationScope[id]}">
      <c:set var="redirectURI" value="${applicationScope[id]}"
       scope="request" />
      <% response.sendRedirect((String)request.getAttribute("redirectURI")); %>
    </c:when>
    <c:otherwise>
      <B><FONT COLOR="red">
      Cart with ID ${param['cartid']} could not be found.
      </FONT></B>
    </c:otherwise>
  </c:choose>
 </c:if>
 <FORM method="post" action="getcart.jsp">
   <P>Please enter the cart ID:</P> 
   <INPUT name="cartid" type="text" maxlength="5"/><BR>
   <INPUT name="sumit" value="Retrieve Cart" type="submit" />
 </FORM>
 <P>
   How to use the amazon web services based MusicStore application:
   <OL>
     <LI>Obtain a amazon web services subscription ID. This was the URI 
     when this application was authored: 
     <a href="http://www.amazon.com/gp/aws/landing.html">
     www.amazon.com/gp/aws/landing.html</a>. Click on "Register for AWS"
     on the top left.</LI>
     <LI>Edit the web.xml found in the WEB-INF/ folder
     where this webapp is deployed such that the context-param with name
     'com.amazon.ecs.SubscriptionId' contains your ID as the param-value.
     </LI>
     <LI>Use the MusicStore application by visiting
     <a href="musicstore-app/voice/mainmenu.do">
     musicstore-app/voice/mainmenu.do</a>.</LI>
     <LI>When you check out, you will be given a cart ID. You can
     then use the form above to retrieve your amazon shopping cart.</LI>
   </OL>
 </P>
</BODY>
</html:html>
<!--Example:End-->
