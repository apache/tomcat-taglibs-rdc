<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:vxml="http://www.w3.org/2001/vxml">
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
