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
<%@ page import="org.apache.taglibs.rdc.core.Grammar" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.0"%>
-->
<c:choose>
 <c:when test="${empty msAppBean}">
  <vxml version="2.0" xml:lang="en-US"  xmlns="http://www.w3.org/2001/vxml" >
    <form><block><prompt>
    The start URI for the RDC Music Store application is mainmenu dot d o
    and not mainmenu dot j s p
    </prompt></block></form>
  </vxml>
 </c:when>
 <c:otherwise>
   <vxml version="2.0" xml:lang="en-US"  xmlns="http://www.w3.org/2001/vxml" >
    <%@ include file="header.jsp" %>
    <jsp:useBean id="dialogMap" class="java.util.LinkedHashMap" scope="session"/>
    <rdc:task map="${dialogMap}">

      <c:if test="${empty dialogMap.choice}">
        <block>
          <audio src="../../media/musicstore-app/welcome.wav"></audio>
          <prompt>Welcome to RDC based Music Store!</prompt>
          <prompt><break time="400ms"/>${proactiveHelp_hint}<break time="400ms"/></prompt>
        </block>
      </c:if>

      <rdc:template id="choice" grammar="${grammarList}" 
       config="/config/musicstore-app/amazon-menu.xml" 
       bean="org.apache.taglibs.rdc.sampleapps.musicstore.AmazonMenuTemplate" 
       fsmFragment="mainmenu-fsm.jsp" />

    </rdc:task>
    <%@ include file="footer.jsp" %>
   </vxml>

   <c:if test="${!(empty choice)}">
     <rdc:struts-submit submit="/listalbums_voice.do" context="${pageContext}" namelist="choice" />
   </c:if>
 </c:otherwise>
</c:choose>
<!--Example:End-->
