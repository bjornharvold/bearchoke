/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.server.common.web.filter;

import com.bearchoke.platform.server.common.ApplicationMediaType;
import lombok.extern.log4j.Log4j2;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Wraps a HttpServletRequest in a proxy object which converts the message body into JSON so
 * we can use HttpServletRequest#getParameter in methods that can't take advantage of
 * Jackson message conversion.
 *
 * Use this filter in conjunction with Spring Security's UsernamePasswordAuthenticationFilter
 * and/or TokenBasedRememberMeServices
 */
@Log4j2
public class JsonHttpRequestFilter implements Filter {
    private static final String JSON_CONTENT_TYPE = "application/(.*)json(.*)";

    public static void main(String[] args) {
        String s = ApplicationMediaType.APPLICATION_TRAVELIKO_V1_JSON_VALUE + "; charset=UTF-8";
        System.out.println(s.matches(JSON_CONTENT_TYPE));
    }

    /**
     * @inheritdoc
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("JsonHttpRequestFilter initialized");
    }

    /**
     * @inheritdoc
     * @param request
     * @param response
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if(request.getContentType().matches(JSON_CONTENT_TYPE) && request instanceof HttpServletRequest) {
            // the content type of the request was JSON - wrap the request in our JSON friendly request wrapper
            if (log.isDebugEnabled()) {
                log.debug("Content-Type is JSON");
            }
            request = new JsonHttpServletRequest((HttpServletRequest)request);
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Content-Type is not JSON: " + request.getContentType());
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * @inheritdoc
     */
    @Override
    public void destroy() {

    }
}

