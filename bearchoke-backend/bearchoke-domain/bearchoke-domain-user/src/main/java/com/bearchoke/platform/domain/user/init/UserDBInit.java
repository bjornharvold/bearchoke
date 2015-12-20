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

import com.bearchoke.platform.api.user.CreateRoleCommand;
import com.bearchoke.platform.api.user.CreateUserCommand;
import com.bearchoke.platform.api.user.RoleIdentifier;
import com.bearchoke.platform.api.user.UserIdentifier;
import com.bearchoke.platform.base.PlatformConstants;
import com.bearchoke.platform.base.init.DBInit;
import com.bearchoke.platform.domain.user.document.Role;
import com.bearchoke.platform.domain.user.document.User;
import com.bearchoke.platform.domain.user.repositories.RoleRepository;
import com.bearchoke.platform.domain.user.repositories.UserRepository;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.axonframework.eventstore.mongo.MongoEventStore;
import org.axonframework.saga.repository.mongo.MongoTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 *
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Component
public class UserDBInit implements DBInit {
    private final static Logger logger = LoggerFactory.getLogger(UserDBInit.class);

    private final org.axonframework.eventstore.mongo.MongoTemplate systemAxonMongo;
    private final MongoEventStore eventStore;
    private final org.springframework.data.mongodb.core.MongoTemplate mongoTemplate;
    private final MongoTemplate systemAxonSagaMongo;
    private final CommandBus commandBus;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserDBInit(CommandBus commandBus,
                      UserRepository userRepository,
                      org.axonframework.eventstore.mongo.MongoTemplate systemMongo,
                      MongoEventStore eventStore,
                      org.springframework.data.mongodb.core.MongoTemplate mongoTemplate,
                      MongoTemplate systemAxonSagaMongo, RoleRepository roleRepository) {
        this.commandBus = commandBus;
        this.userRepository = userRepository;
        this.systemAxonMongo = systemMongo;
        this.eventStore = eventStore;
        this.mongoTemplate = mongoTemplate;
        this.systemAxonSagaMongo = systemAxonSagaMongo;
        this.roleRepository = roleRepository;
    }

    @Override
    public void createItemsIfNoUsersExist() {
        if (!mongoTemplate.collectionExists(User.class)) {
            createItems();
            logger.info("The database has been created and refreshed with some data.");
        }
    }

    private void initializeDB() {
        // clear all of Axon's collections
        systemAxonMongo.domainEventCollection().drop();
        systemAxonMongo.snapshotEventCollection().drop();
        systemAxonSagaMongo.sagaCollection().drop();

        // clear our own collections
        mongoTemplate.dropCollection(User.class);
        mongoTemplate.dropCollection(Role.class);
    }

    private void additionalDBSteps() {
        eventStore.ensureIndexes();
    }

    @Override
    public void createItems() {
        initializeDB();

        RoleIdentifier userRole = createRole(PlatformConstants.DEFAULT_USER_ROLE, Arrays.asList("RIGHT_READ_USER"));
        RoleIdentifier adminRole = createRole(PlatformConstants.DEFAULT_ADMIN_ROLE, Arrays.asList("RIGHT_ADMIN"));

        UserIdentifier user = createUser("harry@mitchell.com", "harrymitchell", "Harry", "Mitchell", "HarryMitchell5!", Arrays.asList(PlatformConstants.DEFAULT_USER_ROLE));
        UserIdentifier admin = createUser("admin@admin.com", "admin", "Admin", "Admin", "AdminAdmin%1", Arrays.asList(PlatformConstants.DEFAULT_ADMIN_ROLE));

        additionalDBSteps();
    }

    private RoleIdentifier createRole(String name, List<String> rights) {
        Role role = roleRepository.findByName(name);

        if (role != null) {
            roleRepository.delete(role);
        }

        RoleIdentifier roleId = new RoleIdentifier(name);
        CreateRoleCommand command = new CreateRoleCommand(roleId, name, rights);
        commandBus.dispatch(new GenericCommandMessage<>(command));

        return roleId;
    }

    UserIdentifier createUser(String email, String username, String firstName, String lastName, String password, List<String> roles) {
        User user = userRepository.findUserByUsername(username);

        if (user != null) {
            userRepository.delete(user);
        }

        UserIdentifier userId = new UserIdentifier();
        CreateUserCommand command = new CreateUserCommand(userId, email, username, firstName, lastName, password, roles);
        commandBus.dispatch(new GenericCommandMessage<>(command));

        return userId;
    }
}
