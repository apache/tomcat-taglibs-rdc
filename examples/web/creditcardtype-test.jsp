<!--Example:Start-->
<?xml version="1.0"?>
<!--
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="mytag" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.0" %>
-->
<vxml version="2.0" xml:lang="en-US" xmlns="http://www.w3.org/2001/vxml">


<jsp:useBean id="rdcStack" class="java.util.Stack" scope="request"/>
<jsp:useBean id="dialogMap"  class="java.util.LinkedHashMap" scope="session"/>


<rdc:push stack="${rdcStack}" element="${dialogMap}"/>

  <c:set var="URI" value="${pageContext.request.requestURI}"/>
  <c:if test="${fn:startsWith(URI, pageContext.request.contextPath)}">
	<c:set var="URI"
		value="${fn:substringAfter(URI, pageContext.request.contextPath)}"/>
  </c:if>
  <c:url var="submit" value="${URI}"/>
 
  <form>
	 <rdc:creditcardType id="payingcard" submit="${submit}" numNBest="3" confirm="true" echo="true" initial = "visa" />
  </form>
</vxml>
<rdc:pop var="discard" stack="${rdcStack}"/>
<!--Example:End-->
