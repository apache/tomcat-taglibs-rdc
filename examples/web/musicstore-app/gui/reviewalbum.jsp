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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
  <%@ page language="java" contentType="text/html; charset=ISO-8859-1" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
  <title>Music Store</title>
</head>
  
<body>
  <%@ include file="header.jsp" %>

  <c:if test="${!(empty msAppBean.currentAlbum.smallImage)}">
    <img height="<c:out value="${msAppBean.currentAlbum.smallImage.height}"/>" 
         src="<c:out value="${msAppBean.currentAlbum.smallImage.url}"/>" 
         width="<c:out value="${msAppBean.currentAlbum.smallImage.width}"/>" />
    <br/>
  </c:if>

  <b><c:out value="${msAppBean.currentAlbum.title}"/></b><br/>	
  <b>Artist:</b><c:out value="${msAppBean.currentAlbum.artist}"/><br/>
  <b>Label:</b><c:out value="${msAppBean.currentAlbum.label}"/><br/>	  						
  <b>List Price:</b>
  <fmt:formatNumber type="currency" 
    value="${msAppBean.currentAlbum.listPrice / 100}" pattern=".00"/><br/>
  
  <c:if test="${msAppBean.currentAlbum.offerSummary.totalNew > 0 }">
    <b>Our Price:</b>
    <fmt:formatNumber type="currency" 
      value="${msAppBean.currentAlbum.offerSummary.newPrice / 100}" pattern=".00"/><br/>
  </c:if>
  <b>Release Date:</b>
  <fmt:formatDate type="date" pattern="MMMMM dd yyyy" 
    value="${msAppBean.currentAlbum.releaseDate}" /><br/>
  <a href="<%=request.getContextPath()%>/showsimilar_gui.do">Customers also bought</a>
 
  <c:choose>
    <c:when test="${(msAppBean.currentAlbum.offerSummary.totalNew == 0)}">
    <P>This item is out of stock</P>
    </c:when>
    <c:when test="${(msAppBean.currentAlbum.offerSummary.totalNew > 0)}">
    <form method="post" action="addtocart_gui.do">
      <input type="submit" value="Add to Cart" name="addtocart">
    </form>
    </c:when>
  </c:choose></body>
</html>
<!--Example:End-->