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

package com.bearchoke.platform.domain.todo.listener;

import com.bearchoke.platform.api.todo.event.ToDoItemCompletedEvent;
import com.bearchoke.platform.api.todo.event.ToDoItemCreatedEvent;
import com.bearchoke.platform.api.todo.event.ToDoItemRemovedEvent;
import com.bearchoke.platform.api.todo.dto.ToDoDto;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by Bjorn Harvold
 * Date: 8/13/14
 * Time: 5:27 PM
 * Responsibility:
 */
@Slf4j
@Component
public class ToDoEventListener {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public ToDoEventListener(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @EventHandler
    public void handle(ToDoItemCreatedEvent event) {

        ToDoDto dto = new ToDoDto(event);
        if (log.isDebugEnabled()) {
            log.debug("Publishing to websocket channel: " + event);
            log.debug("Dto: " + dto);
        }

        // send event over web socket to user subscriber
        this.simpMessagingTemplate.convertAndSend("/topic/todos." + event.getUsername(), dto);
//        this.simpMessagingTemplate.convertAndSendToUser(event.getUsername(), "/topic/todos", event);
    }

    @EventHandler
    public void handle(ToDoItemCompletedEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Publishing to websocket channel: " + event);
        }

        // send event over web socket to user subscriber
        this.simpMessagingTemplate.convertAndSend("/topic/todos." + event.getUsername(), new ToDoDto(event));
//        this.simpMessagingTemplate.convertAndSendToUser(event.getUsername(), "/topic/todos", new ToDoDto(event));
    }

    @EventHandler
    public void handle(ToDoItemRemovedEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Publishing to websocket channel: " + event);
        }

        // send event over web socket to user subscriber
        this.simpMessagingTemplate.convertAndSend("/topic/todos." + event.getUsername(), new ToDoDto(event));
//        this.simpMessagingTemplate.convertAndSendToUser(event.getUsername(), "/topic/todos", new ToDoDto(event));
    }
}
