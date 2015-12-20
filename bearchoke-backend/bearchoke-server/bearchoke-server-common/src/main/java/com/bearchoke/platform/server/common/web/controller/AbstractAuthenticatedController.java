/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.server.common.web.controller;

import com.bearchoke.platform.domain.user.SpringSecurityHelper;
import com.bearchoke.platform.server.common.ServerConstants;
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
