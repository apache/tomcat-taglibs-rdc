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
<%--$Id$--%>
<!--
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.0" %>
<%@ attribute name="map" required="true" type="java.util.Map" %>
<%@ tag body-content="scriptless" %>
-->
<vxml version="2.0"
  xml:lang="en-US"
  xmlns="http://www.w3.org/2001/vxml" >
  <rdc:comment>ToDo: figure out a way to gensym name map so we
  can have more than one task per session</rdc:comment>
  <jsp:useBean id="rdcStack" class="java.util.Stack" scope="request"/>
  <rdc:push stack="${rdcStack}" element="${map}"/>
  <form>    
    <jsp:doBody/>
  </form>
  <rdc:pop var="discard" stack="${rdcStack}"/>
</vxml>
