<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="html" indent="yes"/>
  <xsl:template match="/">
    <html>
      <head>
        <title>Justin Good's Blog RSS Feed</title>
      </head>
      <body>
        <h1>Justin Good's Blog RSS Feed</h1>
        <ul>
          <xsl:for-each select="rss/channel/item">
            <li>
              <a href="{link}"><xsl:value-of select="title"/></a>
              <p><xsl:value-of select="description"/></p>
              <small><xsl:value-of select="pubDate"/></small>
            </li>
          </xsl:for-each>
        </ul>
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>
