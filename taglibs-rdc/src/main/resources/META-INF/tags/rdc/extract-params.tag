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
<%--$Id: extract-params.tag,v 1.1 2004/07/09 17:06:42 rahul Exp
$--%>
<!--
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag body-content="empty" %>

<%@ attribute name="target" required="true" type="java.lang.Object" %>
<%@ attribute name="parameters" required="true" type="java.util.Map" %>
-->
<jsp:useBean id="constants" class="org.apache.taglibs.rdc.core.Constants" />
<%--
  parameters is a map that holds mapping between vxml submit params and 
  bean property in target
  key - vxml submit param
  value - bean property in target
--%>
<c:if test="${target.state != constants.FSM_DONE}"> <%-- Don't be greedy --%>
  <c:forEach items="${parameters}" var="current">
    <c:if test="${!(empty param[current.key])}">
      <c:set target="${target}" property="${current.value}" value="${param[current.key]}"/>
    </c:if>
  </c:forEach>
</c:if>
