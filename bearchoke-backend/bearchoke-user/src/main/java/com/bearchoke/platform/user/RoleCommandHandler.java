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

import com.bearchoke.platform.api.user.CreateRoleCommand;
import com.bearchoke.platform.api.user.RoleIdentifier;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class RoleCommandHandler {
    @Qualifier("roleAggregateRepository")
    private final Repository<RoleAggregate> roleAggregateRepository;

    @Autowired
    public RoleCommandHandler(Repository<RoleAggregate> roleAggregateRepository) {
        this.roleAggregateRepository = roleAggregateRepository;
    }

    @CommandHandler
    public RoleIdentifier handleCreateRoleAggregate(CreateRoleCommand command) {
        if (log.isDebugEnabled()) {
            log.debug("Handling: " + command.getClass().getSimpleName());
        }
        RoleIdentifier id = command.getRoleId();
        RoleAggregate u = new RoleAggregate(id, command.getName(), command.getRights());
        roleAggregateRepository.add(u);

        return id;
    }

}
