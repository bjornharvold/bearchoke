/*
 * Copyright (c) 2015. Bearchoke
 */
package com.bearchoke.platform.server.common.web.websocket;

import com.bearchoke.platform.domain.user.document.ActiveWebSocketUser;
import com.bearchoke.platform.domain.user.repositories.ActiveWebSocketUserRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Log4j2
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