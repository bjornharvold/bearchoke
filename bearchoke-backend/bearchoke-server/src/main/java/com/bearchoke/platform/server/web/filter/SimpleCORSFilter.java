/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bearchoke.platform.server.web.filter;

/**
 * Created by Bjorn Harvold
 * Date: 1/5/14
 * Time: 3:36 PM
 * Responsibility:
 */
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Data
public class SimpleCORSFilter extends OncePerRequestFilter {

    private static final String ORIGIN_FILTER_PATTERN = "[^0-9a-zA-Z$-_.+!*'()$&+,/:;=?@]";

    public static final String IE_CONTENT_TYPE = "text/plain";
    public static final String IE_ENCODING = "UTF-8";
    private String corsAllowMethods;
    private String corsAllowHeaders;
    private String corsAllowCredentials;
    private String corsExposeHeaders;
    private String corsMaxAge;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Get the origin of the request... all origins will be allowed
        String origin = request.getHeader("Origin");

        // Use Host header if Origin is blank
        if (StringUtils.isBlank(origin)) {
            origin = request.getHeader("Host");
        }

        // If origin, set allow
        if (StringUtils.isNotBlank(origin)) {
            // Url encode origin to avoid header splitting cross origin response hacks
            origin = origin.replaceAll(ORIGIN_FILTER_PATTERN, "");
            response.setHeader("Access-Control-Allow-Origin", origin);
        }

        if (corsAllowMethods != null) {
            response.setHeader("Access-Control-Allow-Methods", corsAllowMethods);
        }

        if (corsAllowHeaders != null) {
            response.setHeader("Access-Control-Allow-Headers", corsAllowHeaders);
        }

        if (corsAllowCredentials != null) {
            response.setHeader("Access-Control-Allow-Credentials", corsAllowCredentials);
        }

        if (corsExposeHeaders != null) {
            response.setHeader("Access-Control-Expose-Headers", corsExposeHeaders);
        }

        if (corsMaxAge != null) {
            response.setHeader("Access-Control-Max-Age", corsMaxAge);
        }

        // IE 8 & 9 Crossdomain request doesn't pass content-type correctly
        if ("POST".equals(request.getMethod()) && request.getContentType() == null) {
            request = new IEXDomainRequest(request);
        }

        // to bypass options checks
        if (request.getMethod().equals("OPTIONS")) {
            if (log.isTraceEnabled()) {
                log.trace("OPTIONS called");
            }
            response.getWriter().print("OK");
            response.getWriter().flush();

            return;
        }

        // Pass on to the other filters
        // Pass on to the other filters
        filterChain.doFilter(request, response);
    }

    // Inner class helper
    private static class IEXDomainRequest extends HttpServletRequestWrapper {
        public IEXDomainRequest(HttpServletRequest request)
                throws IOException {
            super(request);
        }

        @Override
        public String getCharacterEncoding() {
            return IE_ENCODING;
        }

        @Override
        public String getContentType() {
            return IE_CONTENT_TYPE;
        }
    }

}
