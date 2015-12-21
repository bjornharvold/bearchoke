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

import com.bearchoke.platform.api.user.command.CreateFacebookUserCommand;
import com.bearchoke.platform.api.user.identifier.UserIdentifier;
import com.bearchoke.platform.api.user.dto.FacebookUserDto;
import com.bearchoke.platform.base.PlatformConstants;
import com.bearchoke.platform.server.common.ApplicationMediaType;
import com.bearchoke.platform.server.common.security.ApiAuthenticationSuccessHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.axonframework.commandhandling.StructuralCommandValidationFailedException;
import org.axonframework.commandhandling.callbacks.FutureCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
@Slf4j
public class FacebookController {
    private final CommandBus commandBus;

    @Qualifier("userDetailsService")
    private final UserDetailsService userDetailsService;

    private final ApiAuthenticationSuccessHandler apiAuthenticationSuccessHandler;

    @Autowired
    public FacebookController(CommandBus commandBus,
                              UserDetailsService userDetailsService,
                              ApiAuthenticationSuccessHandler apiAuthenticationSuccessHandler) {
        this.commandBus = commandBus;
        this.userDetailsService = userDetailsService;
        this.apiAuthenticationSuccessHandler = apiAuthenticationSuccessHandler;
    }

    /**
     * Creates / Updates a facebook user details with the user object locally
     *
     * @param user user
     * @return
     */
    @RequestMapping(value = "/api/facebook", method = {RequestMethod.POST}, consumes = ApplicationMediaType.APPLICATION_BEARCHOKE_V1_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void updateFacebookUser(@RequestBody FacebookUserDto user, HttpServletRequest request, HttpServletResponse response) {
        // attach default role to user
        user.setRoles(new String[]{PlatformConstants.DEFAULT_USER_ROLE});

        if (log.isDebugEnabled()) {
            log.debug(user.toString());
        }

        FutureCallback<UserIdentifier> callback = new FutureCallback<>();

        // reset the password here so we can authenticate later
        // this means we will set a random password every time we have a facebook user try to log in
        user.setPassword(RandomStringUtils.randomAlphabetic(15));

        try {
            commandBus.dispatch(new GenericCommandMessage<>(
                            new CreateFacebookUserCommand(new UserIdentifier(user.getId()), user)), callback
            );
        } catch (StructuralCommandValidationFailedException e) {
            log.error(e.getMessage(), e);
        }

        UserIdentifier userIdentifier;

        try {
            userIdentifier = callback.get();

            if (userIdentifier != null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

                if (userDetails != null) {
                    try {
                        apiAuthenticationSuccessHandler.onAuthenticationSuccess(request, response, new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                        throw new AuthenticationServiceException("Could not authenticate user", e);
                    } catch (ServletException e) {
                        log.error(e.getMessage(), e);
                        throw new AuthenticationServiceException("Could not authenticate user", e);
                    }
                }
                else {
                    if (log.isDebugEnabled()) {
                        log.debug("Could not find Facebook user with username: " + user.getEmail());
                    }
                }
            }

        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            log.error(e.getMessage(), e);
        }


    }


}
