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
    <submit next="${model.submit}" namelist="${model.id}Confirm"/>
  </filled>
</field>
