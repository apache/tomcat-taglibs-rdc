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
  <%@ page language="java"contentType="text/html; charset=ISO-8859-1"%>
  <META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <title>About the Music Store</title>
</head>
<body>
  <%@ include file="header.jsp" %>
  <table width="300">
    <tr><td>
    The Music Store is a <b>multi-channel</b> application.<br/>
    It demonstrates how the <b>RDC tag library</b> can be leveraged to
    create <b>speech</b> applications which share substantial portions
    of the higher layers of the application framework with the
    <b>GUI</b> channel.<br/><br/>
    The GUI channel is designed for <b>small devices</b>, hence uses
    minimal bandwidth and real estate.<br/><br/>
    The set of GUI and Voice JSPs produce the appropriate
    presentation layer for each channel, reusing the higher
    layers (in this case, Struts artifacts and the Amazon Web 
    Services calls).<br/>
    <ul>
      <li>
      The voice channel is here:<br/>
      ${pageContext.request.contextPath}/musicstore-app/voice/mainmenu.do
      </li>
      <li>
      The GUI channel is here:<br/>
      ${pageContext.request.contextPath}/musicstore-app/gui/mainmenu.do
      </li>
    </ul>
    View README for information about getting your own AWS
    subscription ID and try out the Music Store!
    </td></tr>
  </table>
</body>
</html:>
<!--Example:End-->