/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.server.common.security;

import com.bearchoke.platform.server.common.ServerConstants;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Log4j2
public class ApiRequestHeaderAuthenticationFilter extends RequestHeaderAuthenticationFilter {

	public ApiRequestHeaderAuthenticationFilter() {
		super.setPrincipalRequestHeader(ServerConstants.X_AUTH_TOKEN);
		super.setExceptionIfHeaderMissing(false);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (log.isTraceEnabled()) {
			log.trace("Authenticating for url: " + ((HttpServletRequest)request).getRequestURL().toString());
		}
		super.doFilter(request, response, chain);
	}
}
