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

package com.bearchoke.platform.server.frontend.security;

import com.bearchoke.platform.domain.user.security.PreAuthenticatedTokenCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
public class ApiAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	private final PreAuthenticatedTokenCacheService preAuthenticatedTokenCacheService;

	@Autowired
	public ApiAuthenticationSuccessHandler(PreAuthenticatedTokenCacheService preAuthenticatedTokenCacheService) {
		this.preAuthenticatedTokenCacheService = preAuthenticatedTokenCacheService;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

		response.setStatus(HttpServletResponse.SC_OK);

        UserDetails user = (UserDetails) authentication.getPrincipal();
		String xAuthToken = UUID.randomUUID().toString();

		preAuthenticatedTokenCacheService.putInCache(xAuthToken, user);

        // set the result in the request header
		response.setHeader(ServerConstants.X_AUTH_TOKEN, xAuthToken);
	}
	
}
