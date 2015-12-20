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
package com.bearchoke.platform.server.frontend.web.websocket;

import java.security.Principal;
import java.util.Calendar;

import com.bearchoke.platform.domain.user.document.ActiveWebSocketUser;
import com.bearchoke.platform.domain.user.repositories.ActiveWebSocketUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.socket.messaging.SessionConnectEvent;

@Slf4j
public class WebSocketConnectHandler<S> implements ApplicationListener<SessionConnectEvent> {
    private ActiveWebSocketUserRepository repository;
    private SimpMessageSendingOperations messagingTemplate;

    public WebSocketConnectHandler(SimpMessageSendingOperations messagingTemplate, ActiveWebSocketUserRepository repository) {
        super();
        this.messagingTemplate = messagingTemplate;
        this.repository = repository;
    }

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Caught Web Socket connect event");
        }

        MessageHeaders headers = event.getMessage().getHeaders();
        Principal user = SimpMessageHeaderAccessor.getUser(headers);

        if(user == null) {
            return;
        }

        String id = SimpMessageHeaderAccessor.getSessionId(headers);

        if (log.isDebugEnabled()) {
            log.debug("Current web socket session id: " + id);
        }

        if (StringUtils.isNotBlank(id)) {
            repository.save(new ActiveWebSocketUser(id, user.getName(), Calendar.getInstance()));
        }

//        messagingTemplate.convertAndSend("/topic/friends/signin", Arrays.asList(user.getName()));
    }
}