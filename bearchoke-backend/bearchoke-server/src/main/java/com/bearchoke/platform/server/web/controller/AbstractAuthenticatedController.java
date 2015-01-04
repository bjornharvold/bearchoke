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

package com.bearchoke.platform.server.web.controller;

import com.bearchoke.platform.base.SpringSecurityHelper;
import com.bearchoke.platform.server.ServerConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * Created by Bjorn Harvold
 * Date: 10/15/14
 * Time: 7:33 PM
 * Responsibility:
 */
public abstract class AbstractAuthenticatedController {
    private final static Logger log = LoggerFactory.getLogger(AbstractAuthenticatedController.class);

    @Autowired
    @Qualifier("preAuthAuthenticationManager")
    public AuthenticationManager preAuthAuthenticationManager;

    protected void authenticate(StompHeaderAccessor accessor) {
        String authToken = accessor.getFirstNativeHeader(ServerConstants.X_AUTH_TOKEN);

        if (log.isDebugEnabled() && StringUtils.isNotEmpty(authToken)) {
            log.debug("Header auth token: " + authToken);
        }

        if (StringUtils.isNotBlank(authToken)) {

            // set cached authenticated user back in the spring security context
            Authentication authentication = preAuthAuthenticationManager.authenticate(new PreAuthenticatedAuthenticationToken(authToken, "N/A"));

            if (log.isDebugEnabled()) {
                log.debug("Adding Authentication to SecurityContext for WebSocket call: " + authentication);
            }

            SpringSecurityHelper.setAuthentication(authentication);

        }
    }
}
