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

package com.bearchoke.platform.server.frontend.web.controller;

import com.bearchoke.platform.api.user.UserDetailsExtended;
import com.bearchoke.platform.domain.user.security.PreAuthenticatedTokenCacheService;
import com.bearchoke.platform.server.common.ApplicationMediaType;
import com.bearchoke.platform.server.common.ServerConstants;
import com.bearchoke.platform.api.user.dto.AuthenticationToken;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This controller generates the {@link AuthenticationToken} that must be present
 * in subsequent REST invocations.
 *
 * @author Gunnar Hillert
 * @since 1.0
 *
 */
@RestController
@Log4j2
public class AuthenticationController {

    private final PreAuthenticatedTokenCacheService preAuthenticatedTokenCacheService;

    @Autowired
    public AuthenticationController(PreAuthenticatedTokenCacheService preAuthenticatedTokenCacheService) {
        this.preAuthenticatedTokenCacheService = preAuthenticatedTokenCacheService;
    }

    /**
     * Retrieves the pre authenticated user
     * @param authentication
     * @return
     */
    @RequestMapping(value = "/api/secured/user", method = { RequestMethod.GET }, produces = ApplicationMediaType.APPLICATION_BEARCHOKE_V1_JSON_VALUE)
	public AuthenticationToken getUser(Authentication authentication) {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        Collection<GrantedAuthority> credentials = (Collection<GrantedAuthority>) authentication.getAuthorities();

        final Map<String, Boolean> roles = new HashMap<>();

        for (GrantedAuthority authority : credentials) {
            roles.put(authority.getAuthority(), true);
        }

        AuthenticationToken at;

        if (principal instanceof UserDetailsExtended) {
            UserDetailsExtended ude = (UserDetailsExtended) principal;
            at = new AuthenticationToken(ude.getUsername(), ude.getName(), ude.getFirstName(), ude.getLastName(), ude.getProfilePictureUrl(), roles);
        } else {
            at = new AuthenticationToken(principal.getUsername(), roles);
        }

        return at;
	}

    /**
     * Removes the auth token from the session cache
     * @param authToken
     * @param response
     */
	@RequestMapping(value = "/api/logout", method = { RequestMethod.GET })
    @ResponseStatus(HttpStatus.OK)
	public void logoutUser(@RequestHeader(value = ServerConstants.X_AUTH_TOKEN, required = true) String authToken, HttpServletResponse response) {
        preAuthenticatedTokenCacheService.deleteExistingSessions(Collections.singletonList(authToken));
	}
}
