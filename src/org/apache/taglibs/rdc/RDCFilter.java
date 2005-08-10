/*
 *    
 *   Copyright 2004 The Apache Software Foundation.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */
package org.apache.taglibs.rdc;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Locale;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.taglibs.rdc.core.Constants;
/**
 * Servlet filter to tweak RDC output. Filter does the following:
 * 1) Improves aesthetics of VoiceXML output (filter out empty lines
 *         and empty comments)
 * 2) Performs platform adaptations
 * 
 * @author Rahul Akolkar
 * @author Thomas Ling
 */
public class RDCFilter implements Filter {

    //// Filter Init Params
    // The init param name for the platform
    private static final String INIT_PARAM_PLATFORM = "platform";
    // The init param name for the platform
    private static final String INIT_PARAM_VERSION = "version";
    // The init param name for the debug
    private static final String INIT_PARAM_DEBUG = "debug";
    // The init param name for the language (i18n)
    private static final String INIT_PARAM_LANGUAGE = "language";
    // The init param name for the country (i18n)
    private static final String INIT_PARAM_COUNTRY = "country";
    // The init param name for the variant (i18n)
    private static final String INIT_PARAM_VARIANT = "variant";
    
    //// Patterns
    private static final String PATTERN_XML_COMMENT = "<!--(\\s|.)*?-->";
    private static final String PATTERN_BLANK_LINES = "((\\s)*\\n){2,}";

    //// String constants
    private static final String STR_EMPTY = "";
    private static final String STR_NEWLINE = "\n";
    private static final String STR_CONTENT_TYPE_HTML = "text/html";

    // Save the config so we can extract the platform information
    // from the init parameter
    private FilterConfig filterConfig;

    // The init param value for the platform
    private String platform;

    // The init param value for the version
    private String version;

    // The init param value for the debug
    private boolean bDebug;
    
    // The String representation of the Locale based on init param values
    private String locale;
    
    // The Locale based on init param values
    private Locale rdcLocale;
    
    // Whether locale information needs to be pushed to 
    // org.apache.taglibs.rdc.core.Constants
    private boolean pushLocale = true;

    /**
     * Save FilterConfig so we can extract the init parameters later
     * 
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        this.platform = filterConfig.getInitParameter(INIT_PARAM_PLATFORM);
        this.version = filterConfig.getInitParameter(INIT_PARAM_VERSION);
        String debug = filterConfig.getInitParameter(INIT_PARAM_DEBUG);

        if (debug != null) {
            this.bDebug = debug.equalsIgnoreCase("true");
        } else {
            this.bDebug = false;
        }
        
        String language = filterConfig.getInitParameter(INIT_PARAM_LANGUAGE);
        String country = filterConfig.getInitParameter(INIT_PARAM_COUNTRY);
        String variant = filterConfig.getInitParameter(INIT_PARAM_VARIANT);
        initLocale(language, country, variant);


        // save a handle for later, standard practice
        this.filterConfig = filterConfig;
    }

    /**
     * Filter out empty lines and empty comments, perform platform adaptations
     * 
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     *  javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        
        if (!(request instanceof HttpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }
        checkLocale();

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpRes = (HttpServletResponse) response;

        PrintWriter out = httpRes.getWriter();
        CharResponseWrapper wrapper = new CharResponseWrapper(httpRes);
        chain.doFilter(httpReq, wrapper);

        if (bDebug) {
            String reqUrl = httpReq.getRequestURL().toString();
            String queryString = httpReq.getQueryString();

            if (queryString != null) {
                reqUrl += "?" + queryString;
            }
            filterConfig.getServletContext().log("URL: " + reqUrl);
        }

        // Its possible this is a RDC based JSP if:
        // 1) The content type is not set, or
        // 2) The content type is non-HTML
        if ( wrapper.getContentType() == null || 
            !wrapper.getContentType().equals(STR_CONTENT_TYPE_HTML) ) {
            // 1) Improve aesthetics of VoiceXML output
            String trimmedResponse = trimResponse(wrapper.toString());
            // 2) Perform platform adaptations
            String adaptedResponse = performPlatformAdaptations(trimmedResponse);

            // Now that looks just fine
            httpRes.setContentLength(adaptedResponse.length());
            out.write(adaptedResponse);
        } else {
            // What a waste ;-)
            out.write(wrapper.toString());
        }

        // We're done
        out.close();
    }

    /**
     * Cleanup
     * 
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
        this.filterConfig = null;
    }

    /**
     * Trim the response.
     * 
     * @param String responseString - The response to trim
     */
    private String trimResponse(String responseString) {
        // Filter out the empty comments
        String trimmedResponse = 
            responseString.replaceAll(PATTERN_XML_COMMENT, STR_EMPTY);

        // Trim any empty lines
        trimmedResponse = 
            trimmedResponse.replaceAll(PATTERN_BLANK_LINES, STR_NEWLINE);

        return trimmedResponse;
    }

    /**
     * Perform platform adaptations.
     * 
     * @param String line - The line in the response
     */
    private String performPlatformAdaptations(String vxmlMarkup) {
        if (platform == null) {
            return vxmlMarkup;
        }
        // Platform adaptations go here    
        return vxmlMarkup;
    }
    
    /**
     * Get Locale for given language, country and variant.
     * 
     * @param String language - The language of the Locale
     * @param String country - The country of the Locale
     * @param String variant - The variant of the Locale
     */
    private void initLocale(String language, String country, String variant) {
        boolean gotLanguage = !RDCUtils.isStringEmpty(language);
        boolean gotCountry = !RDCUtils.isStringEmpty(country);
        boolean gotVariant = !RDCUtils.isStringEmpty(variant);
        if (!gotLanguage) {
            return;
        } else {
            if (gotCountry) {
                if (gotVariant) {
                    rdcLocale = new Locale(language, country, variant);
                } else {
                    rdcLocale = new Locale(language, country);
                }
            } else {
                rdcLocale = new Locale(language);
            }
        }
        if (rdcLocale != null) {
            StringBuffer buf = new StringBuffer(language);
            if (!RDCUtils.isStringEmpty(country)) {
                buf.append('-').append(country);
                if (!RDCUtils.isStringEmpty(variant)) {
                    buf.append('-').append(variant);
                }
            }
            locale = buf.toString();
        }
    }
    
    private void checkLocale() {
        if (pushLocale) {
            if (rdcLocale != null) {
                Constants.initI18NResources(locale, rdcLocale);
            }
            pushLocale = false;
        }
    }

    /**
     * Response wrapper so we can examine the RDC output
     */
    private static class CharResponseWrapper extends HttpServletResponseWrapper {

        private Writer output;

        public String toString() {
            return output.toString();
        }

        public CharResponseWrapper(HttpServletResponse response) {
            super(response);
            output = new CharArrayWriter();
        }

        public PrintWriter getWriter() {
            return new PrintWriter(output);
        }
    }
}