/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.server.common.security;

import com.bearchoke.platform.domain.user.security.PreAuthenticatedTokenCacheService;
import com.bearchoke.platform.server.common.ServerConstants;
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

        UserDetails principal = (UserDetails) authentication.getPrincipal();
		String xAuthToken = UUID.randomUUID().toString();

		preAuthenticatedTokenCacheService.putInCache(xAuthToken, principal);

        // set the result in the request header
		response.setHeader(ServerConstants.X_AUTH_TOKEN, xAuthToken);
	}
	
}
