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
  <%@ page language="java"contentType="text/html; charset=ISO-8859-1" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  <META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <title>Music Store</title>
</head>

<body>
<c:choose>
 <c:when test="${empty msAppBean}">
    <b>Music Store Outlet</b><br/><br/>
    The start URI is <font color="red">mainmenu.do</font><br/>
    and not mainmenu.jsp<br/><br/>
    Click <a href="mainmenu.do">here</a> to start.<br/><br/>
 </c:when>
 <c:otherwise>
  <%@ include file="header.jsp" %>
  Welcome to Music Store Outlet<br/>
  ${msAppBean.menuLinks.links}
 </c:otherwise>
</c:choose>
</body>
</html>
<!--Example:End-->
