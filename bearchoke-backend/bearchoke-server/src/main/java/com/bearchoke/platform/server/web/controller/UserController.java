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

package com.bearchoke.platform.server.web.controller;

import com.bearchoke.platform.api.user.RegisterUserCommand;
import com.bearchoke.platform.api.user.UserIdentifier;
import com.bearchoke.platform.api.user.dto.RegisterUserDto;
import com.bearchoke.platform.api.user.dto.UniqueResult;
import com.bearchoke.platform.base.PlatformConstants;
import com.bearchoke.platform.server.web.ApplicationMediaType;
import com.bearchoke.platform.user.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserController {
    private final CommandBus commandBus;
    private final UserRepository userRepository;

    @Autowired
    public UserController(CommandBus commandBus,
                          UserRepository userRepository) {
        this.commandBus = commandBus;
        this.userRepository = userRepository;
    }

    /**
     * Checks to see if email already exists in the system
     *
     * @param email email
     * @return
     */
    @RequestMapping(value = "/api/user/uniqueemail", method = {RequestMethod.GET}, produces = ApplicationMediaType.APPLICATION_BEARCHOKE_V1_JSON_VALUE)
    public UniqueResult isEmailUnique(@RequestParam(value = "key", required = true) String email) {
        return new UniqueResult(userRepository.isEmailUnique(email));
    }

    /**
     * Checks to see if username already exists in the system
     *
     * @param username username
     * @return
     */
    @RequestMapping(value = "/api/user/uniqueusername", method = {RequestMethod.GET}, produces = ApplicationMediaType.APPLICATION_BEARCHOKE_V1_JSON_VALUE)
    public UniqueResult isUsernameUnique(@RequestParam(value = "key", required = true) String username) {
        return new UniqueResult(userRepository.isUsernameUnique(username));
    }

    /**
     * Register a user with the system
     *
     * @param user user
     * @return
     */
    @RequestMapping(value = "/api/user/register", method = {RequestMethod.POST}, produces = ApplicationMediaType.APPLICATION_BEARCHOKE_V1_JSON_VALUE, consumes = ApplicationMediaType.APPLICATION_BEARCHOKE_V1_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody RegisterUserDto user) {
        // attach default role to user
        user.setRoles(new String[]{PlatformConstants.DEFAULT_USER_ROLE});

        if (log.isDebugEnabled()) {
            log.debug(user.toString());
        }

        commandBus.dispatch(new GenericCommandMessage<>(
                        new RegisterUserCommand(new UserIdentifier(), user))
        );
    }
}
