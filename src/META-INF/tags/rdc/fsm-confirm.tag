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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.0" %>
<%@ tag body-content="empty" %>

<%@ attribute name="model" required="true" type="java.lang.Object" %>
-->
<field name="${model.id}Confirm" type="boolean">
  <rdc:get-configuration xml="${model.configuration}"
  locator="/config/confirm/property-list/property"/>
  <rdc:expand>
    <rdc:get-configuration xml="${model.configuration}"
    locator="/config/confirm/prompt-list/prompt" />
  </rdc:expand>
  <catch event= "repeat" >
    <reprompt/>
  </catch>
  <rdc:expand>
    <rdc:get-configuration xml="${model.configuration}"
    locator="/config/confirm/noinput-list/noinput"/>
    <rdc:get-configuration xml="${model.configuration}"
    locator="/config/confirm/nomatch-list/nomatch"/>
    <rdc:get-configuration xml="${model.configuration}"
    locator="/config/confirm/help-list/help"/>
  </rdc:expand>
  <filled>
    <submit next="${model.submit}" method="post" namelist="${model.id}Confirm"/>
  </filled>
</field>
