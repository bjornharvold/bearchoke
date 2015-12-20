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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * {@link org.springframework.security.web.AuthenticationEntryPoint} that rejects all requests with an unauthorized error message.
 * 
 * @author Philip W. Sorst <philip@sorst.net>
 */
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper om;

    public UnauthorizedEntryPoint(ObjectMapper objectMapper) {
        this.om = objectMapper;
    }

    @Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException
	{
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        om.writeValue(response.getOutputStream(),
                new ApiError(
                        request.getRequestURI(),
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "Unauthorized: Authentication token was either missing or invalid."
                )
        );
	}
}