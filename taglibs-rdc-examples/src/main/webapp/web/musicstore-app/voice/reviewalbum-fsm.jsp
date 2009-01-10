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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.0"%>
-->
<c:choose>
  <c:when test="${model.state == constants.FSM_VALIDATE and model.isValid == false 
   and model.errorCode == model.ERR_NEED_DETAILS}">
    <block>
      <rdc:expand>
        <rdc:get-configuration xml="${model.configuration}"
        locator="/config/validate/handler[@errorCode=${model.errorCode}]/prompt"/>
      </rdc:expand>
    </block>
    <c:set target="${model}" property="state" value="${constants.FSM_INPUT}"/>
    <rdc:fsm-input  model="${model}" state="custom[@name='describe-album']" />
    <c:set target="${model}" property="state" value="${constants.FSM_VALIDATE}"/>
  </c:when>

  <c:otherwise>
    <rdc:fsm-run  model="${model}"/>
  </c:otherwise>
</c:choose>
<!--Example:End-->
