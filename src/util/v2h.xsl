<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:vxml="http://www.w3.org/2001/vxml">
<!-- 
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
-->
  <xsl:output encoding="iso8859-15"
  method="xml"  indent="yes"/>
  <xsl:param name="convertor">http://localhost/cgi-bin/v2h.pl</xsl:param>
  <xsl:template match="vxml:vxml">
    <html>
      <head>
        <title>Exercising VoiceXML Web Applications </title>
      </head>
      <body>
        <xsl:apply-templates  select="vxml:form"/>
      </body>
    </html>
  </xsl:template>
  
  <xsl:template match="vxml:form">
    <form>
      <xsl:attribute name="action">
        <xsl:value-of select="vxml:field/vxml:submit/@action"/>
      </xsl:attribute>
      <xsl:apply-templates select=".//vxml:prompt"/>
      <xsl:apply-templates select=".//vxml:submit"/>
    </form>
  </xsl:template>
  <xsl:template match="vxml:prompt">
    <br/>
    <span><xsl:value-of  select="."/></span><br/>
  </xsl:template>
  <xsl:template match="vxml:submit">
    <p>Please provide values for 
      <xsl:value-of select="@namelist"/>
    </p>
  </xsl:template>
</xsl:stylesheet>
