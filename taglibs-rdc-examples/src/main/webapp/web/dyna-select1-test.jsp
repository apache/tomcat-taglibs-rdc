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
<!--$Id$-->
<!--
<%@ page language="java" contentType="application/voicexml+xml" %>
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.0" %>
-->
<%--
  FOLLOWING SCRIPTLET IS FOR ILLUSTRATION ONLY.
  The Options object will be created elsewhere (struts layer etc.)
--%>
<%
  org.apache.taglibs.rdc.SelectOne.Options opts = 
        new org.apache.taglibs.rdc.SelectOne.Options();
  // Add (value, utterance) pairs.
  // Only non-null and non-whitespace values will cause value attribute
  // to be rendered.
  opts.add(null, "one");
  opts.add("", "two");
  opts.add("3", "three");
  opts.add("   ", "four");
  request.setAttribute("opts", opts);
%>

<vxml version="2.0" xml:lang="en-US" xmlns="http://www.w3.org/2001/vxml">
  <jsp:useBean id="dialogMap" class="java.util.LinkedHashMap" scope="session"/>
  <rdc:task map="${dialogMap}" >

    <rdc:select1 id="dynamiclist" confirm="true" optionList="${opts}" />

  </rdc:task>
</vxml>
<!--Example:End-->
