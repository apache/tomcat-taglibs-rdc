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
