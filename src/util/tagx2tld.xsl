<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:jsp="http://java.sun.com/JSP/Page"
  version="1.0">
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
