<!--Example:Start-->
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
<!--$Id$-->
<!--
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="rdc" uri="http://jakarta.apache.org/taglibs/rdc-1.0" %>
-->
<vxml version="2.0" xml:lang="en-US" xmlns="http://www.w3.org/2001/vxml">

<jsp:useBean id="rdcStack" class="java.util.Stack" scope="request"/>
<jsp:useBean id="dialogMap"  class="java.util.LinkedHashMap" scope="session"/>

<rdc:push stack="${rdcStack}" element="${dialogMap}"/>
  <form>

    	<rdc:select1 id="test" submit="${submit}"
       config="config/equipment-type/equipment-cfg.xml" 
	 optionList="config/equipment-type/equipment-opt.xml" confirm="true"
	 minConfidence="40.0F" numNBest="6" echo="true" />

  </form>
</vxml>
<rdc:pop var="discard" stack="${rdcStack}"/>
<!--Example:End-->
