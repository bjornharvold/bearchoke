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

package com.bearchoke.platform.server.frontend.web.filter;

import com.bearchoke.platform.server.common.ApplicationMediaType;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
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
@Slf4j
public class JsonHttpRequestFilter implements Filter {
    private static final String JSON_CONTENT_TYPE = "application/(.*)json(.*)";

    public static void main(String[] args) {
        String s = ApplicationMediaType.APPLICATION_BEARCHOKE_V1_JSON_VALUE + "; charset=UTF-8";
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

