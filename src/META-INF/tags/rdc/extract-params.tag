<%--$Id: extract-params.tag,v 1.1 2004/07/09 17:06:42 rahul Exp
$--%>
<!--
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag body-content="empty" %>

<%@ attribute name="target" required="true" type="java.lang.Object" %>
<%@ attribute name="parameters" required="true" type="java.util.Map" %>
-->
<%--
  parameters is a map that holds mapping between vxml submit params and 
  bean property in target
  key - vxml submit param
  value - bean property in target
--%>
  
<c:forEach items="${parameters}" var="current">
  <c:if test="${!(empty param[current.key])}">
    <c:set target="${target}" property="${current.value}" value="${param[current.key]}"/>
  </c:if>
</c:forEach>
