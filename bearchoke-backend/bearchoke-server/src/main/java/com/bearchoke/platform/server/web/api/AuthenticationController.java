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

package com.bearchoke.platform.server.web.api;

import com.bearchoke.platform.server.ServerConstants;
import com.bearchoke.platform.server.security.ApiPreAuthenticatedTokenCacheService;
import com.bearchoke.platform.server.web.ApplicationMediaType;
import com.bearchoke.platform.server.web.controller.dto.AuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
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
@Slf4j
public class AuthenticationController {

    private final ApiPreAuthenticatedTokenCacheService apiPreAuthenticatedTokenCacheService;

    @Autowired
    public AuthenticationController(ApiPreAuthenticatedTokenCacheService apiPreAuthenticatedTokenCacheService) {
        this.apiPreAuthenticatedTokenCacheService = apiPreAuthenticatedTokenCacheService;
    }

    /**
     * Retrieves the pre authenticated user
     * @param authentication
     * @return
     */
    @RequestMapping(value = "/api/secured/user", method = { RequestMethod.GET }, produces = ApplicationMediaType.APPLICATION_BEARCHOKE_V1_JSON_VALUE)
	public AuthenticationToken getUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Collection<GrantedAuthority> credentials = (Collection<GrantedAuthority>) authentication.getAuthorities();

		final Map<String, Boolean> roles = new HashMap<>();

        for (GrantedAuthority authority : credentials) {
            roles.put(authority.getAuthority(), true);
        }

		return new AuthenticationToken(user.getUsername(), roles);
	}

    /**
     * Removes the auth token from the session cache
     * @param authToken
     * @param response
     */
	@RequestMapping(value = "/api/logout", method = { RequestMethod.GET })
    @ResponseStatus(HttpStatus.OK)
	public void logoutUser(@RequestHeader(value = ServerConstants.X_AUTH_TOKEN, required = true) String authToken, HttpServletResponse response) {
        apiPreAuthenticatedTokenCacheService.deleteExistingSessions(Collections.singletonList(authToken));
	}
}
