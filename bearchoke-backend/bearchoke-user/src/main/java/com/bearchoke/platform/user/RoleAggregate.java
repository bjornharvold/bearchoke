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

import com.bearchoke.platform.api.user.RoleCreatedEvent;
import com.bearchoke.platform.api.user.RoleIdentifier;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 12/16/14
 * Time: 9:16 PM
 * Responsibility:
 */
@Component
@Slf4j
public class RoleAggregate extends AbstractAnnotatedAggregateRoot {

    @AggregateIdentifier
    private RoleIdentifier id;
    private String name;
    private List<String> rights;

    public RoleAggregate() {
    }

    public RoleAggregate(RoleIdentifier id, String name, List<String> rights) {
        apply(new RoleCreatedEvent(
                id,
                name,
                rights
        ));
    }

    @EventHandler
    public void onRoleCreated(RoleCreatedEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Caught: " + event.getClass().getSimpleName());
        }

        this.id = event.getRoleId();
        this.name = event.getName();
        this.rights = event.getRights();
    }
}
