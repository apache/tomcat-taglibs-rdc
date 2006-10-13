<?xml version="1.0" encoding="utf-8"?>
<!-- 
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
-->
<!--$Id$-->
<!--  DTD module for RDC component configuration. This XML structure is used to configure dialog components. --> 
  <!--===== Root Element ===== -->
<!--
    Element  config: Configure dialog components
--> 
<!ELEMENT config (input,confirm,validate,echo, custom*)>
<!ATTLIST config
  xmlns CDATA #FIXED ''>

<!ELEMENT input (property-list,prompt-list,help-list,noinput-list, nomatch-list)>
<!ATTLIST input
  xmlns CDATA #FIXED ''>

<!ELEMENT confirm
(property-list,prompt-list,help-list,noinput-list, nomatch-list, reject)>
<!ATTLIST confirm
  xmlns CDATA #FIXED ''>

<!ELEMENT validate (handler)+>
<!ATTLIST validate
  xmlns CDATA #FIXED ''>

<!ELEMENT echo (property-list,prompt-list)>
<!ATTLIST echo
  xmlns CDATA #FIXED ''>

<!ELEMENT custom (property-list,prompt-list,help-list,noinput-list, nomatch-list)>
<!--         role       Identify this  custom section's purpose. Used
                        as a selector.                           -->
<!ATTLIST custom
  xmlns CDATA #FIXED ''
  role  CDATA #REQUIRED >



<!ELEMENT handler (%event.handler.content;)*>
<!ATTLIST handler
  xmlns CDATA #FIXED ''
  errorCode CDATA #REQUIRED>

<!ELEMENT reject (%event.handler.content;)*>
<!ATTLIST reject
  xmlns CDATA #FIXED ''>

<!ELEMENT property-list (property)+>
<!ATTLIST property-list
  xmlns CDATA #FIXED ''>

<!ELEMENT prompt-list (prompt+)>
                       
<!ATTLIST prompt-list
  xmlns CDATA #FIXED ''>

<!ELEMENT help-list (help)+>
<!ATTLIST help-list
  xmlns CDATA #FIXED ''>
  
<!ELEMENT noinput-list (noinput)+>
<!ATTLIST noinput-list
  xmlns CDATA #FIXED ''>

<!ELEMENT nomatch-list (nomatch)+>
<!ATTLIST nomatch-list
  xmlns CDATA #FIXED ''>

