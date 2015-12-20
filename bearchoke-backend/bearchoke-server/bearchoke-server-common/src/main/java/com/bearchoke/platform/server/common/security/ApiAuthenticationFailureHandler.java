/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.server.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ApiAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper om;

    @Autowired
    public ApiAuthenticationFailureHandler(ObjectMapper om) {
        this.om = om;
    }

    @Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        om.writeValue(response.getOutputStream(),
                new ApiError(
                        request.getRequestURI(),
                        HttpServletResponse.SC_UNAUTHORIZED,
                        exception.getLocalizedMessage()
                )
        );
	}

}
