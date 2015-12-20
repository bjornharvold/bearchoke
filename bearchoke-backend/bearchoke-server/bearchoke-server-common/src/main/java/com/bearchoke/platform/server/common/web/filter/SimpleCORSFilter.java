/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.server.common.web.filter;

/**
 * Created by Bjorn Harvold
 * Date: 1/5/14
 * Time: 3:36 PM
 * Responsibility:
 */

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Log4j2
@Data
@EqualsAndHashCode(callSuper = false)
@Component("simpleCORSFilter")
public class SimpleCORSFilter extends OncePerRequestFilter {

    @Inject
    private Environment environment;

    private static final String ORIGIN_FILTER_PATTERN = "[^0-9a-zA-Z$-_.+!*'()$&+,/:;=?@]";

    public static final String IE_CONTENT_TYPE = "text/plain";
    public static final String IE_ENCODING = "UTF-8";
    private List<String> allowedOrigins;
    private String corsAllowMethods;
    private String corsAllowHeaders;
    private String corsAllowCredentials;
    private String corsExposeHeaders;
    private String corsMaxAge;

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        corsAllowMethods = environment.getProperty("cors.allow.methods");
        corsAllowHeaders = environment.getProperty("cors.allow.headers");
        corsAllowCredentials = environment.getProperty("cors.allow.credentials");
        corsExposeHeaders = environment.getProperty("cors.expose.headers");
        corsMaxAge = environment.getProperty("cors.max.age");
        allowedOrigins = Arrays.asList(
                StringUtils.split(
                        StringUtils.deleteWhitespace(
                                environment.getProperty("allowed.origin")
                        ), ",")
        );
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        boolean allowed = true;

        if (log.isTraceEnabled()) {
            log.trace("CORS filter in effect. Adding required header values.");
        }

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
            final String finalOrigin = origin;

            if (allowedOrigins.stream().anyMatch(s -> s.equals(finalOrigin))) {
                if (log.isTraceEnabled()) {
                    log.trace("Allowing: " + origin);
                }
                response.setHeader("Access-Control-Allow-Origin", origin);
            } else {
                if (log.isTraceEnabled()) {
                    log.trace("Disallowing: " + origin);
                }
                allowed = false;
            }
        }

        if (allowed) {
            if (StringUtils.isNotBlank(corsAllowMethods)) {
                response.setHeader("Access-Control-Allow-Methods", corsAllowMethods);
            }

            if (StringUtils.isNotBlank(corsAllowHeaders)) {
                response.setHeader("Access-Control-Allow-Headers", corsAllowHeaders);
            }

            if (StringUtils.isNotBlank(corsAllowCredentials)) {
                response.setHeader("Access-Control-Allow-Credentials", corsAllowCredentials);
            }

            if (StringUtils.isNotBlank(corsExposeHeaders)) {
                response.setHeader("Access-Control-Expose-Headers", corsExposeHeaders);
            }

            if (StringUtils.isNotBlank(corsMaxAge)) {
                response.setHeader("Access-Control-Max-Age", corsMaxAge);
            }
        }

        // IE 8 & 9 Crossdomain request doesn't pass content-type correctly
        if ("POST".equals(request.getMethod()) && request.getContentType() == null) {
            request = new IEXDomainRequest(request);
        }

        // to bypass options checks
        if (allowed && request.getMethod().equals("OPTIONS")) {
            if (log.isTraceEnabled()) {
                log.trace("OPTIONS called");
            }
            response.getWriter().print("OK");
            response.getWriter().flush();
        } else {
            // Pass on to the other filters
            filterChain.doFilter(request, response);
        }

        if (log.isTraceEnabled()) {
            log.trace("CORS filter successful!");
        }
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
