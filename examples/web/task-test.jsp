<!--Example:Start-->
<!--$Id$-->
<!--
<%@ page language="java" contentType="application/vxml" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.0"%>
-->
<jsp:useBean id="taskmap" class="java.util.LinkedHashMap" scope="session"/>
<rdc:task map="${taskmap}">
  <rdc:date id="start" minDate="01012004" maxDate="01012005"
  confirm="true" echo="true"/>
</rdc:task>
<!--Example:End-->
