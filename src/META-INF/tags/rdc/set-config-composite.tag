<%--$Id$--%>
<!--
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag body-content="empty" %>

<%@ attribute name="model" required="true" type="java.lang.Object" %>
<%@ attribute name="context" required="true" type="javax.servlet.jsp.JspContext" %>
<%@ attribute name="config" required="true" %>
-->
<rdc:comment>
  Make sure you set context before config
</rdc:comment>
<c:if test="${!(empty context) and !(empty config)}">
   <c:set target ="${model}" property="context" value="${context}"/>
   <c:set target ="${model}" property="config" value="${config}"/>
</c:if>
