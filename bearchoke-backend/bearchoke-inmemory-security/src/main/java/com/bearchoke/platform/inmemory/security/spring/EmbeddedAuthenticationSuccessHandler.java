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

package com.bearchoke.platform.inmemory.security.spring;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EmbeddedAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final RememberMeServices rememberMeServices;

    public EmbeddedAuthenticationSuccessHandler(RememberMeServices rememberMeServices) {
        this.rememberMeServices = rememberMeServices;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // Track the login
        Object principal = authentication.getPrincipal();
        if (principal != null) {
            rememberMeServices.loginSuccess(request, response, authentication);
        }

        // Allow the Spring implementation to redirect to the configured URL
        super.onAuthenticationSuccess(request, response, authentication);
    }

    /*
    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        if (isAlwaysUseDefaultTargetUrl()) {
            return getDefaultTargetUrl();
        }

        // Check for the parameter and use that if available
        String targetUrl = null;

        if (getTargetUrlParameter() != null  ) {
            targetUrl = request.getParameter(getTargetUrlParameter());

            if (StringUtils.hasText(targetUrl)) {
                logger.debug("Found targetUrlParameter in request: " + targetUrl);

                return targetUrl;
            }
        }

        // Check the saved request
        targetUrl = extractOriginalUrl(request, response);

        if (!StringUtils.hasText(targetUrl)) {
            targetUrl = getDefaultTargetUrl();
            logger.debug("Using default Url: " + targetUrl);
        }

        return targetUrl;
    }

    private String extractOriginalUrl(HttpServletRequest request, HttpServletResponse response) {
        // else look to see if there is another saved request defined somewhere else
        SavedRequest saved = requestCache.getRequest(request, response);
        if (saved == null) {
            return null;
        }
        requestCache.removeRequest(request, response);
        return saved.getRedirectUrl();
    }
    */
}
