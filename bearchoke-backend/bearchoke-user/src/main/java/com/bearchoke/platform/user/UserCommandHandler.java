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

package com.bearchoke.platform.user;

import com.bearchoke.platform.api.user.RegisterUserCommand;
import com.bearchoke.platform.api.user.UserIdentifier;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by Bjorn Harvold
 * Date: 12/8/14
 * Time: 7:21 PM
 * Responsibility:
 */
@Component
public class UserCommandHandler {
    @Qualifier("userRepository")
    private final Repository<UserAggregate> repository;

    @Autowired
    public UserCommandHandler(Repository<UserAggregate> repository) {
        this.repository = repository;
    }

    @CommandHandler
    public UserIdentifier handleRegisterUserAggregate(RegisterUserCommand command) {
        UserIdentifier id = command.getUserId();
        UserAggregate u = new UserAggregate(id, command.getUsername(), command.getPassword(), command.getEmail(), command.getFirstName(), command.getLastName());
        repository.add(u);

        return id;
    }
}
