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

import com.bearchoke.platform.api.user.RoleIdentifier;
import com.bearchoke.platform.api.user.UserCreatedEvent;
import com.bearchoke.platform.user.document.Role;
import com.bearchoke.platform.user.document.User;
import com.bearchoke.platform.user.repositories.RoleRepository;
import com.bearchoke.platform.user.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Bjorn Harvold
 * Date: 8/13/14
 * Time: 5:27 PM
 * Responsibility:
 */
@Slf4j
@Component
public class UserEventListener {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserEventListener(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @EventHandler
    public void handleCreateUser(UserCreatedEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Caught: " + event.getClass().getSimpleName());
        }

        User user = new User(event);
        if (event.getRoles() != null && event.getRoles().size() > 0) {
            for (RoleIdentifier roleName : event.getRoles()) {
                Role role = roleRepository.findByName(roleName.toString());

                if (role != null) {
                    user.addRole(role);
                }
            }
        }

        user = userRepository.save(user);

        if (log.isDebugEnabled()) {
            log.debug("Saved user object: " + user.toString());
        }
    }

}
