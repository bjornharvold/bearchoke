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

import com.bearchoke.platform.api.todo.CreateToDoItemCommand;
import com.bearchoke.platform.api.todo.DeleteToDoItemCommand;
import com.bearchoke.platform.api.todo.MarkToDoItemAsCompleteCommand;
import com.bearchoke.platform.api.todo.ToDoIdentifier;
import com.bearchoke.platform.platform.base.SpringSecurityHelper;
import com.bearchoke.platform.api.todo.dto.ToDoDto;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

/**
 * Created by Bjorn Harvold
 * <p/>
 * Date: 1/5/14
 * <p/>
 * Time: 3:32 PM
 * <p/>
 * Responsibility:
 */

@Controller
@Slf4j
public class ToDoController extends AbstractAuthenticatedController {

    private final CommandBus commandBus;

    @Autowired
    public ToDoController(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

    /**
     * Create todo item
     * @param dto
     * @return
     */
    @MessageMapping("/user/todo")
    public void createTodo(ToDoDto dto, StompHeaderAccessor accessor) {
        super.authenticate(accessor);

        UserDetails user = SpringSecurityHelper.getUserDetails();

        if (user != null) {
            GenericCommandMessage gcm = null;

            switch (dto.getAction()) {
                case CREATE:
                    gcm = new GenericCommandMessage<>(
                            new CreateToDoItemCommand(
                                    new ToDoIdentifier(),
                                    dto.getDescription(),
                                    user.getUsername()
                            )
                    );
                    break;
                case COMPLETED:
                    gcm = new GenericCommandMessage<>(
                            new MarkToDoItemAsCompleteCommand(
                                    new ToDoIdentifier(
                                            dto.getId()),
                                    user.getUsername()
                            )
                    );
                    break;
                case DELETE:
                    gcm = new GenericCommandMessage<>(
                            new DeleteToDoItemCommand(
                                    new ToDoIdentifier(dto.getId()),
                                    user.getUsername()
                            )
                    );
                    break;
                default:
                    log.error("Action: " + dto.getAction() + " not supported");
            }

            if (gcm != null) {
                commandBus.dispatch(gcm);
            }
        } else {
            String error = "No user could be found to authenticate";
            log.error(error);
            throw new RuntimeException(error);
        }
    }
}

