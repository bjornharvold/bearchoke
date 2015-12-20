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

package com.bearchoke.platform.domain.user.handler;

import com.bearchoke.platform.api.user.command.AuthenticateUserCommand;
import com.bearchoke.platform.api.user.command.CreateFacebookUserCommand;
import com.bearchoke.platform.api.user.command.CreateUserCommand;
import com.bearchoke.platform.api.user.command.RegisterUserCommand;
import com.bearchoke.platform.api.user.UserAccount;
import com.bearchoke.platform.api.user.identifier.UserIdentifier;
import com.bearchoke.platform.base.PlatformConstants;
import com.bearchoke.platform.domain.user.UserConstants;
import com.bearchoke.platform.domain.user.aggregate.UserAggregate;
import com.bearchoke.platform.domain.user.document.User;
import com.bearchoke.platform.domain.user.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by Bjorn Harvold
 * Date: 12/8/14
 * Time: 7:21 PM
 * Responsibility:
 */
@Component
@Slf4j
public class UserCommandHandler {
    @Qualifier("userAggregateRepository")
    private final Repository<UserAggregate> userAggregateRepository;

    @Qualifier("userRepository")
    private final UserRepository userRepository;

    @Autowired
    public UserCommandHandler(Repository<UserAggregate> userAggregateRepository, UserRepository userRepository) {
        this.userAggregateRepository = userAggregateRepository;
        this.userRepository = userRepository;
    }

    @CommandHandler
    public UserIdentifier handleRegisterUserAggregate(RegisterUserCommand command) {
        if (log.isDebugEnabled()) {
            log.debug("Handling: " + command.getClass().getSimpleName());
        }

        UserIdentifier id = command.getUserId();
        UserAggregate u = new UserAggregate(
                id,
                UserConstants.SITE_SOURCE,
                command.getUsername(),
                command.getPassword(),
                command.getEmail(),
                command.getFirstName(),
                command.getLastName(),
                Arrays.asList(PlatformConstants.DEFAULT_USER_ROLE)
        );

        // persist user aggregate
        userAggregateRepository.add(u);

        return id;
    }

    @CommandHandler
    public UserIdentifier handleCreateUserAggregate(CreateUserCommand command) {
        if (log.isDebugEnabled()) {
            log.debug("Handling: " + command.getClass().getSimpleName());
        }

        UserIdentifier id = command.getUserId();
        UserAggregate u = new UserAggregate(
                id,
                UserConstants.SITE_SOURCE,
                command.getUsername(),
                command.getPassword(),
                command.getEmail(),
                command.getFirstName(),
                command.getLastName(),
                command.getRoles()
        );

        // persist user aggregate
        userAggregateRepository.add(u);

        return id;
    }

    @CommandHandler
    public UserIdentifier handleCreateFacebookUserAggregate(CreateFacebookUserCommand command) {
        UserIdentifier id = null;
        UserAggregate u = null;

        if (log.isDebugEnabled()) {
            log.debug("Handling: " + command.getClass().getSimpleName());
        }

        // first see if we can retrieve an existing user
        User user = userRepository.findUserByUserIdentifier(command.getUserId().toString());

        if (user == null) {
            // user does not yet exist - go ahead and create it

            id = command.getUserId();

            u = new UserAggregate(
                    id,
                    UserConstants.FACEBOOK_SOURCE,
                    command.getEmail(),
                    command.getPassword(),
                    command.getEmail(),
                    command.getFirstName(),
                    command.getLastName(),
                    Arrays.asList(PlatformConstants.DEFAULT_USER_ROLE));
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Updating Facebook user......");
                log.debug("Old user values: " + user.getUsername() + ", " + user.getEmail() + ", " + user.getFirstName() + ", " + user.getLastName());
                log.debug("New user values: " + command.getEmail() + ", " + command.getEmail() + ", " + command.getFirstName() + ", " + command.getLastName());
            }
            // just update the values
            UserAggregate ua = onUser(user.getUserIdentifier());
            u = new UserAggregate(
                    ua.getId(),
                    UserConstants.FACEBOOK_SOURCE,
                    command.getEmail(),
                    command.getPassword(),
                    command.getEmail(),
                    command.getFirstName(),
                    command.getLastName(),
                    Arrays.asList(PlatformConstants.DEFAULT_USER_ROLE));

            id = ua.getId();
        }

        // persist / update user aggregate
        userAggregateRepository.add(u);

        return id;
    }

    @CommandHandler
    public UserAccount handleAuthenticateUser(AuthenticateUserCommand command) {
        if (log.isDebugEnabled()) {
            log.debug("Handling: " + command.getClass().getSimpleName());
        }
        User user = userRepository.findUserByUsername(command.getUsername());

        if (user == null) {
            return null;
        }

        boolean success = onUser(user.getUserIdentifier()).authenticate(command.getPassword());

        if (log.isDebugEnabled()) {
            log.debug("Authentication successful: " + success);
        }

        return success ? user : null;
    }

    private UserAggregate onUser(String userId) {
        UserAggregate ua = userAggregateRepository.load(new UserIdentifier(userId));

        if (log.isDebugEnabled()) {
            log.debug("Found user aggregate: " + ua);
        }

        return ua;
    }
}
