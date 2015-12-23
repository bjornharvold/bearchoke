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

package com.bearchoke.platform.domain.user.init;

import com.bearchoke.platform.api.user.command.CreateUserCommand;
import com.bearchoke.platform.api.user.enums.Gender;
import com.bearchoke.platform.api.user.identifier.UserIdentifier;
import com.bearchoke.platform.base.PlatformConstants;
import com.bearchoke.platform.base.init.DBInit;
import com.bearchoke.platform.domain.user.RoleConstants;
import com.bearchoke.platform.domain.user.document.User;
import com.bearchoke.platform.domain.user.repositories.RoleRepository;
import com.bearchoke.platform.domain.user.repositories.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 *
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Component
@Log4j2
@Order(3)
public class UserDBInit implements DBInit {
    private final MongoTemplate mongoTemplate;
    private final CommandBus commandBus;
    private final UserRepository userRepository;

    private boolean usersInserted = false;

    @Autowired
    public UserDBInit(CommandBus commandBus,
                      UserRepository userRepository,
                      MongoTemplate mongoTemplate) {
        this.commandBus = commandBus;
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public boolean initEvenIfExist() {
        // overwrite everything
        if (mongoTemplate.collectionExists(User.class)) {
            initializeDB();
        }

        return initIfNotExist();
    }

    @Override
    public boolean initIfNotExist() {
        log.info("Creating reference users...");

        createUser("harry", "harry@mitchell.com", "harrymitchell", "Harry", "Mitchell", "/img/profile/god.png", Gender.Male, "HarryMitchell5!", Collections.singletonList(PlatformConstants.DEFAULT_USER_ROLE));
        createUser("superman", "admin@admin.com", "admin", "Admin", "Admin", "/img/profile/god.png", Gender.Male, "AdminAdmin%1", Collections.singletonList(PlatformConstants.DEFAULT_ADMIN_ROLE));

        return usersInserted;
    }

    private void initializeDB() {
        log.info("Dropping User collections from MongoDb");
        // clear our own collections
        mongoTemplate.dropCollection(User.class);
    }

    private void createUser(String userIdentifier, String email, String username, String firstName, String lastName, String profilePictureUrl, Gender gender, String password, List<String> roles) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            usersInserted = true;

            UserIdentifier userId = new UserIdentifier(userIdentifier);
            CreateUserCommand command = new CreateUserCommand(userId, email, username, firstName, lastName, profilePictureUrl, gender, password, roles);
            commandBus.dispatch(new GenericCommandMessage<>(command));

            log.info("Created system user: " + username);
        }


    }
}
