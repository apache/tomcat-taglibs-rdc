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
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.0" %>
<%@ tag body-content="empty" %>

<%@ attribute name="model" required="true" type="java.lang.Object" %>
-->

<rdc:comment>
  States in FSM:
  0) initOnly -    RDC initializes itself but emits no markup.
  1) input -       RDC generates prompt for input
  2) validate -    Validate collected input againsts supplied constraints
  3) confirmation- RDC generates confirmation prompts
  4) end -         RDC echoes results before exit
  5) done -		   RDC  done; signal this to the parent
  6) dormant - 	   RDC in a group, currently inactive
  
  Rahul: SM follows, one choose, one when per state transition
  Transition is defined as possible state change(s) the vxml browser 
  can see in any given round trip.
  
  State initOnly:
  One of two possible entry points for the SM. 
  Transition to state input. 
  
  State input:
  One of two possible entry points for the SM.
  Prompt for user for input (outsourced to fsm-input).
  Transition to state validate.
  
  State validate:
  1) If input is valid and author has requested confirmation from user,
  transition to confirmation state
  2) If input is valid and author has not requested confirmation from user,
  transition to end state
  3) If input is not valid, set prompt depending on error code received 
  from data model and transition back to input state to get 
  new input from user
  
  State confirmation:
  1) Prompt for user for confirmation of validated input (outsourced to
  fsm-confirm). Remain in state confirmation
  2) User validated input. Transition to state end
  3) User invalidated input. Transition back to input and finally 
  validate state.
  
  State end:
  1) Set retVal to value of date
  2) Echo value if author has requested.
  3) Set state to done. This is the only exit for the SM.
  
</rdc:comment>
<jsp:useBean id="constants" class="org.apache.taglibs.rdc.core.Constants" />

<c:choose>
  
  <c:when test="${model.state == constants.FSM_INITONLY || 
  		model.state == constants.FSM_DORMANT}">
    <%-- (initOnly -> initOnly) --%>
    <%-- (dormant -> dormant) --%>
  </c:when>
  
  <c:when test="${model.state == constants.FSM_INPUT}">
    <%-- (input -> validate) --%>
    <rdc:fsm-input model="${model}" />
    <c:set target="${model}" property="state" value="${constants.FSM_VALIDATE}"/>
  </c:when>

  <c:when test="${model.state == constants.FSM_VALIDATE and model.isValid == true 
    and model.skipSubmit == true}">
    <%-- (validate -> done) --%>
    <c:set target="${model}" property="state" value="${constants.FSM_DONE}"/>
  </c:when>
    
  <c:when test="${model.state == constants.FSM_VALIDATE and model.isValid == true 
    and model.confirm == true}">
    <%-- (validate -> confirmation) --%>
    <c:set target="${model}" property="state" value="${constants.FSM_CONFIRM}"/>
    <rdc:fsm-confirm  model="${model}" />
  </c:when>
  
  <c:when test="${model.state == constants.FSM_VALIDATE and model.isValid == true 
    and model.confirm == false}">
    <%-- (validate -> done) --%>
    <c:if test="${model.echo == true}">
      <c:set var="configuration" value="${model.configuration}"/>
      <block>
        <rdc:expand>
          <rdc:get-configuration xml="${model.configuration}"
          locator="/config/echo/prompt-list/prompt" />
        </rdc:expand>
      </block>
    </c:if>
    <c:set target="${model}" property="state" value="${constants.FSM_DONE}"/>
  </c:when>
  
  <c:when test="${model.state == constants.FSM_VALIDATE and model.isValid == false}">
    <%-- (validate -> input -> validate) --%>
    <block>
      <rdc:expand>
        <rdc:get-configuration xml="${model.configuration}"
        locator="/config/validate/handler[@errorCode=${model.errorCode}]/prompt"/>
      </rdc:expand>
    </block>
    <c:set target="${model}" property="state" value="${constants.FSM_INPUT}"/>
    <rdc:fsm-input  model="${model}" />
    <c:set target="${model}" property="state" value="${constants.FSM_VALIDATE}"/>
  </c:when>
  
  <c:when test="${model.state == constants.FSM_CONFIRM and model.confirmed == true}">
    <%-- (confirmation -> done) --%>
    <c:if test="${model.echo == true}">
      <c:set var="configuration" value="${model.configuration}"/>
      <block>
        <rdc:expand>
          <rdc:get-configuration xml="${model.configuration}"
          locator="/config/echo/prompt-list/prompt" />
        </rdc:expand>
      </block>
    </c:if>
    <c:set target="${model}" property="state" value="${constants.FSM_DONE}"/>
  </c:when>
  
  <c:when test="${model.state == constants.FSM_CONFIRM and model.confirmed == false}">
    <%-- (confirmation -> input -> validate) --%> 
    <block>
      <rdc:expand>
        <rdc:get-configuration xml ="${model.configuration}"
        locator="/config/confirm/reject/prompt"/>
      </rdc:expand>
    </block>	
    <c:set target="${model}" property="state" value="${constants.FSM_INPUT}"/>
    <c:set target="${model}" property="isValid" value="false"/>
    <c:set target="${model}" property="confirmed" value="false"/>
    <rdc:fsm-input  model="${model}" />
    <c:set target="${model}" property="state" value="${constants.FSM_VALIDATE}"/>
  </c:when>
  
</c:choose>
