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
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.0"%>
-->
<vxml version="2.0" xml:lang="en-US"  xmlns="http://www.w3.org/2001/vxml" >
  <%@ include file="header.jsp" %>
  <jsp:useBean id="dialogMap" class="java.util.LinkedHashMap" scope="session"/>
  <rdc:task map="${dialogMap}">

    <rdc:template id="review" data="${msAppBean.currentAlbum}" 
     grammar="../../grammar/musicstore-app/actions.grxml" 
     config="/config/musicstore-app/review-album.xml" 
     bean="org.apache.taglibs.rdc.sampleapps.musicstore.ReviewAlbumTemplate"
     fsmFragment="reviewalbum-fsm.jsp" />

  </rdc:task>
  <%@ include file="footer.jsp" %>
</vxml>

<c:if test="${!(empty review)}">
  <c:if test="${review == 'addtocart'}" >
    <jsp:forward page="/addtocart.do" />
  </c:if>
  <c:if test="${review == 'showsimilar'}" >
    <jsp:forward page="/showsimilar.do" />
  </c:if>
</c:if>
<!--Example:End-->
