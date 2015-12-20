/*
 * Copyright (c) 2015. Bearchoke
 */
package com.bearchoke.platform.server.common.web.websocket;

import com.bearchoke.platform.domain.user.document.ActiveWebSocketUser;
import com.bearchoke.platform.domain.user.repository.ActiveWebSocketUserRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.security.Principal;
import java.util.Calendar;

@Log4j2
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