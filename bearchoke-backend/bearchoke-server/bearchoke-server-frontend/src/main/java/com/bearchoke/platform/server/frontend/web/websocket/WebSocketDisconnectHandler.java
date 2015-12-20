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

import com.bearchoke.platform.domain.user.document.ActiveWebSocketUser;
import com.bearchoke.platform.domain.user.repositories.ActiveWebSocketUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
public class WebSocketDisconnectHandler<S> implements ApplicationListener<SessionDisconnectEvent> {
    private ActiveWebSocketUserRepository repository;
    private SimpMessageSendingOperations messagingTemplate;

    public WebSocketDisconnectHandler(SimpMessageSendingOperations messagingTemplate, ActiveWebSocketUserRepository repository) {
        super();
        this.messagingTemplate = messagingTemplate;
        this.repository = repository;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Caught Web Socket disconnect event");
        }

        String id = event.getSessionId();
        if (StringUtils.isBlank(id)) {
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("Current web socket session id: " + id);
        }

        ActiveWebSocketUser user = repository.findUserBySessionId(id);

        if (user == null) {
            return;
        }

        repository.delete(user.getId());

//        messagingTemplate.convertAndSend("/topic/friends/signout", Arrays.asList(user.getUsername()));
    }
}