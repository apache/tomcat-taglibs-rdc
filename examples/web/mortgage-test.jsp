<!--Example:Start-->
<!--$Id$-->

<% response.setHeader("Cache-Control", "no-cache"); %>

<!--
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.0"%>
-->


<vxml version="2.0" xml:lang="en-US"  xmlns="http://www.w3.org/2001/vxml" >
  <jsp:useBean id="rdcStack" class="java.util.Stack"
               scope="request"/> 
  <jsp:useBean id="dialogMap"  class="java.util.LinkedHashMap"
               scope="session"/>
  <rdc:push stack="${rdcStack}" element="${dialogMap}"/>

  <form>
   	 <rdc:mortgage id="mortgage" config="config/mortgage-cfg.xml" />
  </form>
  <rdc:pop var="discard" stack="${rdcStack}"/>
</vxml>
<!--Example:End-->
