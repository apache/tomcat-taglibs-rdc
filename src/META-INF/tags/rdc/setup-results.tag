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
<%--$Id$--%>
<!--
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag body-content="empty" %>

<%@ attribute name="model" required="true" type="java.lang.Object" %>
<%@ attribute name="submit" required="true" %>
<%@ attribute name="numNBest" required="false" %>
<%@ attribute name="minConfidence" required="false" %>
<%@ attribute name="maxNoInput" required="false" %>
<%@ attribute name="maxNoMatch" required="false" %>
-->
<rdc:comment>
  Initialized submit and the N-Best attributes
</rdc:comment>
<c:choose>
  <c:when test="${empty submit}">
   <c:url var="self" value="${pageContext.request.servletPath}"/>
   <c:set target="${model}" property="submit" value="${self}"/>
 </c:when>
 <c:otherwise>
   <c:set target="${model}" property="submit" value="${submit}"/>
 </c:otherwise>
</c:choose>
<c:if test="${!empty minConfidence}">
   <c:set target="${model}" property="minConfidence" value="${minConfidence}"/>
</c:if>
<c:if test="${!empty numNBest}">
   <c:set target="${model}" property="numNBest" value="${numNBest}"/>
</c:if>
<c:if test="${!empty maxNoInput}">
   <c:set target="${model}" property="maxNoInput" value="${maxNoInput}"/>
</c:if>
<c:if test="${!empty maxNoMatch}">
   <c:set target="${model}" property="maxNoMatch" value="${maxNoMatch}"/>
</c:if>
