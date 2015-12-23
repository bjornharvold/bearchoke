/*
 * Copyright (c) 2015. Traveliko
 */

package com.bearchoke.platform.domain.user.init;

import com.bearchoke.platform.api.user.command.CreateRoleCommand;
import com.bearchoke.platform.api.user.identifier.RoleIdentifier;
import com.bearchoke.platform.base.init.DBInit;
import com.bearchoke.platform.domain.user.RoleConstants;
import com.bearchoke.platform.domain.user.document.Role;
import com.bearchoke.platform.domain.user.repositories.RoleRepository;
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
@Order(1)
public class RoleDBInit implements DBInit {

    private final MongoTemplate mongoTemplate;
    private final CommandBus commandBus;
    private final RoleRepository roleRepository;

    @Autowired
    public RoleDBInit(CommandBus commandBus,
                      MongoTemplate mongoTemplate,
                      RoleRepository roleRepository) {

        this.commandBus = commandBus;
        this.mongoTemplate = mongoTemplate;
        this.roleRepository = roleRepository;
    }

    @Override
    public boolean initEvenIfExist() {
        // overwrite everything
        if (mongoTemplate.collectionExists(Role.class)) {
            initializeDB();
        }

        return initIfNotExist();
    }

    @Override
    public boolean initIfNotExist() {
        boolean result = true;

        log.info("Creating new reference roles...");
        createRole(RoleConstants.ROLE_USER, Collections.singletonList(RoleConstants.RIGHT_USER));
        createRole(RoleConstants.ROLE_ADMIN, Collections.singletonList(RoleConstants.RIGHT_ADMIN));
        createRole(RoleConstants.ROLE_MANAGER, Collections.singletonList(RoleConstants.RIGHT_MANAGER));

        return result;
    }

    private void initializeDB() {
        log.info("Dropping Role collections from MongoDb");
        // clear our own collections
        mongoTemplate.dropCollection(Role.class);
    }

    private void createRole(String name, List<String> rights) {
        Role role = roleRepository.findByName(name);

        if (role == null) {
            log.info(String.format("Creating new role: %s", name));
            RoleIdentifier roleId = new RoleIdentifier(name);
            CreateRoleCommand command = new CreateRoleCommand(roleId, name, rights);
            commandBus.dispatch(new GenericCommandMessage<>(command));
        } else {
            log.info(String.format("Role: %s already exists. Will not modify.", name));
        }
    }
}
