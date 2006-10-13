<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:jsp="http://java.sun.com/JSP/Page"
  version="1.0">
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
  <xsl:param name="name">tag-name</xsl:param>
  <xsl:param name="path">tag-location</xsl:param>
  <xsl:template match="/">
    <tag-file>
      <name><xsl:value-of select="$name"/></name>
      <path><xsl:value-of select="$path"/></path>
      <xsl:apply-templates select="jsp:directive.tag"/>
      <xsl:apply-templates select="//jsp:directive.tag"/>
      <xsl:apply-templates select="//jsp:directive.attribute"/>
    </tag-file>
    <xsl:text>
    </xsl:text>
  </xsl:template>
  <xsl:template match="jsp:directive.tag">
    <xsl:apply-templates select="@*"/>
    <xsl:text> </xsl:text>
  </xsl:template>
  <xsl:template match="jsp:directive.attribute">
    <attribute>
      <xsl:apply-templates select="@*"/>
      <xsl:text> </xsl:text>
    </attribute>
  </xsl:template>
  <xsl:template match="@*">
    <xsl:element name="{name()}">
      <xsl:value-of select="."/>
    </xsl:element>
  </xsl:template>
</xsl:stylesheet>
