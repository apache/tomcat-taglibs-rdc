<!--Example:Start-->
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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
  <%@ page language="java" contentType="text/html; charset=ISO-8859-1" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
  <TITLE>Music Store</TITLE>
</head>

<body>
  <%@ include file="header.jsp" %>

  <c:if test="${(empty msAppBean.cart)}">
    <P>Your cart is empty </P>
  </c:if>

  <c:if test="${!(empty msAppBean.cart)}">
  <table>
    <tr>
      <td align="left" width="200"><b>Item Title</b></td>
	  <td align="left" width="25"><b>Price</b></td>
    </tr>
    <c:forEach var="item" items="${msAppBean.cart. cartItems}">
      <c:set var="total" value="${total + item.listPrice}" scope="page"/>
      <tr> 
        <td align="left" bgcolor="#ffffff"><c:out value="${item.title}" /></td>
        <td align="left" bgcolor="#ffffff">
          <fmt:formatNumber type="currency" 
             value="${item.listPrice / 100}" pattern=".00"/>
        </td>
      </tr>
    </c:forEach>
    <tr>
      <td align="left" width="200">Total</td>
	  <td align="left" width="25">
	    <fmt:formatNumber type="currency" 
          value="${total / 100}" pattern=".00"/></td>
    </tr> 
  </table>
  </c:if>

  <a href="${pageContext.request.contextPath}/checkout_gui.do?action=shopping">Continue Shopping</a>
  <form method="post" action="checkout_gui.do">
    <input type="hidden" name="action" value="checkout" />
    <input type="Submit" value="Checkout" name="checkout">
  </form>

</body>
</html>
<!--Example:End-->