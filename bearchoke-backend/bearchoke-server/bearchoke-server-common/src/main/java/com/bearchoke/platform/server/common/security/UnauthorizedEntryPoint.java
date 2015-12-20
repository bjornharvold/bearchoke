/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.server.common.security;

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