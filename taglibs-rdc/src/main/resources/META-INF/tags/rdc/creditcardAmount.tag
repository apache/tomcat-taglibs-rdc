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
<!--
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.1" %>

<%@ tag body-content="empty" %>

<%@ attribute name="id" required="true" rtexprvalue="false" %>
<%@ attribute name="submit" required="false" %>
<%@ attribute name="config" required="false" %>
<%@ attribute name="initial" required="false" %>
<%@ attribute name="confirm" required="false" %>
<%@ attribute name="locale" required="false" %>
<%@ attribute name="echo" required="false" %>
<%@ attribute name="balance" required="false" %>
<%@ attribute name="minAmount" required="false" %>
<%@ attribute name="maxAmount" required="false" %>
<%@ attribute name="maxDenials" required="false" %>
<%@ attribute name="currencyCode" required="false" %>
<%@ attribute name="minConfidence" required="false" %>
<%@ attribute name="numNBest" required="false" %>
<%@ attribute name="maxNoInput" required="false" %>
<%@ attribute name="maxNoMatch" required="false" %>
<%@ attribute name="subdialog" required="false" %>
<%@ variable name-from-attribute="id" alias="retVal" scope="AT_END"%>
-->

<rdc:peek var="stateMap" stack="${requestScope.rdcStack}"/>
<jsp:useBean id="constants" class="org.apache.taglibs.rdc.core.Constants" />

<c:choose>
  <c:when test="${empty stateMap[id]}">
    <rdc:comment>This instance is being called for the first time in this session </rdc:comment>
    <jsp:useBean id="model"
      class="org.apache.taglibs.rdc.CreditCardAmount" >
      <c:set target="${model}" property="state"
       value="${stateMap.initOnlyFlag == true ? constants.FSM_INITONLY : constants.FSM_INPUT}"/>
      <rdc:comment> initialize bean from our attributes </rdc:comment>
      <c:set target="${model}" property="id" value="${id}"/>
      <c:set target="${model}" property="initial" value="${initial}"/>
      <c:set target="${model}" property="submit" value="${submit}"/>
      <c:set target="${model}" property="confirm" value="${confirm}"/>
      <c:set target="${model}" property="echo" value="${echo}"/>
      <c:set target="${model}" property="locale" value="${locale}"/>
      <c:set target="${model}" property="currencyCode" value="${currencyCode}"/>
      <c:set target="${model}" property="subdialog" value="${subdialog}"/>
      <rdc:set-grammar model="${model}" key="rdc.currency.voicegrammar.uri" />
      <rdc:get-resource bundle="${model.rdcResourceBundle}" var="fbGrammar" 
       key="rdc.creditcard.fullbalance.voicegrammar.uri" />
      <jsp:useBean id="full_amt_grammar"
       class="org.apache.taglibs.rdc.core.Grammar" >
          <c:set target="${full_amt_grammar}" property="grammar"
           value="${pageContext.request.contextPath}/${fbGrammar}"/>
      </jsp:useBean>      
      <c:set target="${model}" property="fullAmountGrammar" value="${full_amt_grammar}"/>
      <rdc:get-resource bundle="${model.rdcResourceBundle}" var="mpGrammar"
       key="rdc.creditcard.minpayment.voicegrammar.uri" />
      <jsp:useBean id="min_due_grammar"
       class="org.apache.taglibs.rdc.core.Grammar" >
          <c:set target="${min_due_grammar}" property="grammar"
           value="${pageContext.request.contextPath}/${mpGrammar}"/>
      </jsp:useBean>      
      <c:set target="${model}" property="minimumDueGrammar" value="${min_due_grammar}"/>
      <c:set target="${model}" property="balance" value="${balance}"/>
      <c:set target="${model}" property="minAmount" value="${minAmount}"/>
      <c:set target="${model}" property="maxAmount" value="${maxAmount}"/>
      <rdc:get-resource bundle="${model.rdcResourceBundle}" var="defaultConfig"
       key="rdc.creditcard.amount.defaultconfig.uri" />
      <rdc:configure model="${model}" config="${config}"
       defaultConfig="${defaultConfig}" />
      <rdc:setup-results model="${model}" submit="${submit}" 
        minConfidence="${minConfidence}" numNBest="${numNBest}"
        maxNoInput="${maxNoInput}" maxNoMatch="${maxNoMatch}" />
      <c:if test="${not empty maxDenials}">
        <c:set target="${model}" property="maxDenials" value="${maxDenials}"/>
      </c:if>
    </jsp:useBean>
    <rdc:comment>cache away this instance for future requests in this session </rdc:comment>
    <c:set target="${stateMap}" property="${id}" value="${model}"/>
  </c:when>
  <c:otherwise>
    <rdc:comment>retrieve cached bean for this instance</rdc:comment>
    <c:set var="model" value="${stateMap[id]}"/>
  </c:otherwise>
</c:choose>

<rdc:extract-params target="${model}" parameters="${model.paramsMap}"/>

<rdc:fsm-run  model="${model}" />
              
<c:if test="${model.state == constants.FSM_DONE}">
  <c:set var="retVal" value="${model.value}"/>
  <rdc:subdialog-return  model="${model}"/>
</c:if>
