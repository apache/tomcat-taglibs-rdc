<!--Example:Start-->

<!-- 
<%@ page language="java" contentType="application/vxml" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.0"%>
-->

<vxml version="2.0" xml:lang="en-US"  xmlns="http://www.w3.org/2001/vxml" >

<jsp:useBean id="h" class="org.apache.taglibs.rdc.HelloWorldBean"/>  
<c:set target="${h}" property="name" value="Bubbles"/>

<form>   
<rdc:hello name="${h.name}"/>    
</form>

</vxml>
<!--Example:End-->