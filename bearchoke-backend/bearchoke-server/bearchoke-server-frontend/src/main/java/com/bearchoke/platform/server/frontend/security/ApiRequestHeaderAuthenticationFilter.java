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

import com.bearchoke.platform.server.common.ServerConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class ApiRequestHeaderAuthenticationFilter extends RequestHeaderAuthenticationFilter {

	public ApiRequestHeaderAuthenticationFilter() {
		super.setPrincipalRequestHeader(ServerConstants.X_AUTH_TOKEN);
		super.setExceptionIfHeaderMissing(false);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (log.isDebugEnabled()) {
			log.debug("Authenticating for url: " + ((HttpServletRequest)request).getRequestURL().toString());
		}
		super.doFilter(request, response, chain);
	}
}
