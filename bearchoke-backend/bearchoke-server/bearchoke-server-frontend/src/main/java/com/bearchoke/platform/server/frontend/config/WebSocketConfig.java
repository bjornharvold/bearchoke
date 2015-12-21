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

package com.bearchoke.platform.server.frontend.config;

import com.bearchoke.platform.base.SpringSecurityHelper;
import com.bearchoke.platform.server.common.ServerConstants;
import com.bearchoke.platform.domain.user.repositories.ActiveWebSocketUserRepository;
import com.bearchoke.platform.server.common.web.websocket.WebSocketConnectHandler;
import com.bearchoke.platform.server.common.web.websocket.WebSocketDisconnectHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.session.ExpiringSession;
import org.springframework.session.web.socket.config.annotation.AbstractSessionWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.inject.Inject;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 8/25/14
 * Time: 1:12 AM
 * Responsibility:
 */
@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig<S extends ExpiringSession> extends AbstractSessionWebSocketMessageBrokerConfigurer<S> {

    @Inject
    private Environment environment;

    @Inject
    private ObjectMapper objectMapper;

    @Inject
    @Qualifier("preAuthAuthenticationManager")
    private AuthenticationManager preAuthAuthenticationManager;

    @Override
    public void configureStompEndpoints(StompEndpointRegistry registry) {
        log.info("WebSocket config: Allowing only origins: " + environment.getProperty("allowed.origin"));
        registry.addEndpoint("/ws").setAllowedOrigins(environment.getProperty("allowed.origin")).setHandshakeHandler(new SecureHandshakeHandler(preAuthAuthenticationManager))
                .withSockJS()
                .setStreamBytesLimit(512 * 1024)
                .setHttpMessageCacheSize(1000)
                .setDisconnectDelay(30 * 1000)
                .setInterceptors(new HttpSessionIdHandshakeInterceptor())
        ;
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setSendTimeLimit(15 * 1000).setSendBufferSizeLimit(512 * 1024);
        registration.setMessageSizeLimit(128 * 1024);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration channelRegistration) {
        channelRegistration.setInterceptors(sessionContextChannelInterceptorAdapter());
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration channelRegistration) {
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> converters) {
        MappingJackson2MessageConverter jacksonConverter = new MappingJackson2MessageConverter();
        jacksonConverter.setObjectMapper(objectMapper);
        converters.add(jacksonConverter);

        return true;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app");
        config.enableSimpleBroker("/queue/", "/topic/");

        // This uses too much data for CF AMPQ service
//        StompBrokerRelayRegistration stompBrokerRelayRegistration = config.enableStompBrokerRelay("/queue/", "/topic/");
//
//        stompBrokerRelayRegistration.setRelayHost(environment.getProperty("rabbitmq.host"));
//        stompBrokerRelayRegistration.setVirtualHost(environment.getProperty("rabbitmq.virtualhost"));
//        stompBrokerRelayRegistration.setClientLogin(environment.getProperty("rabbitmq.username"));
//        stompBrokerRelayRegistration.setSystemLogin(environment.getProperty("rabbitmq.username"));
//        stompBrokerRelayRegistration.setClientPasscode(environment.getProperty("rabbitmq.password"));
//        stompBrokerRelayRegistration.setSystemPasscode(environment.getProperty("rabbitmq.password"));

        // only if we want to use . instead of / for path separator e.g. /app/user.chat
//        config.setPathMatcher(new AntPathMatcher("."));
    }

    @Bean
    public WebSocketConnectHandler<S> webSocketConnectHandler(SimpMessageSendingOperations messagingTemplate, ActiveWebSocketUserRepository repository) {
        return new WebSocketConnectHandler<>(messagingTemplate, repository);
    }

    @Bean
    public WebSocketDisconnectHandler<S> webSocketDisconnectHandler(SimpMessageSendingOperations messagingTemplate, ActiveWebSocketUserRepository repository) {
        return new WebSocketDisconnectHandler<S>(messagingTemplate, repository);
    }

    /**
     * For serving up websockets in a Tomcat / GlassFish / WildFly environment
     *
     * @return
     */
    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }

    @Bean
    public ChannelInterceptorAdapter sessionContextChannelInterceptorAdapter() {
        return new ChannelInterceptorAdapter() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
                StompCommand command = accessor.getCommand();

                if (log.isDebugEnabled() && command != null) {
                    log.debug("StompCommand: " + command.toString());
                }

                String authToken = accessor.getFirstNativeHeader(ServerConstants.X_AUTH_TOKEN);

                if (log.isDebugEnabled() && StringUtils.isNotEmpty(authToken)) {
                    log.debug("Header auth token: " + authToken);
                }

                if (StringUtils.isNotBlank(authToken)) {

                    // set cached authenticated user back in the spring security context
                    Authentication authentication = preAuthAuthenticationManager.authenticate(new PreAuthenticatedAuthenticationToken(authToken, "N/A"));

                    if (log.isDebugEnabled()) {
                        log.debug("Adding Authentication to SecurityContext for WebSocket call: " + authentication);
                    }
                    SpringSecurityHelper.setAuthentication(authentication);

                }
                return super.preSend(message, channel);
            }
        };
    }

    static class HttpSessionIdHandshakeInterceptor implements HandshakeInterceptor {

        @Override
        public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
            if (request instanceof ServletServerHttpRequest) {

                HttpHeaders headers = request.getHeaders();

                if (headers.containsKey(ServerConstants.X_AUTH_TOKEN)) {
                    List<String> authToken = headers.get(ServerConstants.X_AUTH_TOKEN);

                    if (log.isDebugEnabled()) {
                        log.debug("Header auth token: " + authToken.get(0));
                    }

                    attributes.put(ServerConstants.X_AUTH_TOKEN, headers.get(ServerConstants.X_AUTH_TOKEN));
                }
            }
            return true;
        }

        public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Exception ex) {
        }


    }

    static class SecureHandshakeHandler extends DefaultHandshakeHandler {
        private final AuthenticationManager authenticationManager;

        public SecureHandshakeHandler(AuthenticationManager authenticationManager) {
            this.authenticationManager = authenticationManager;
        }

        @Override
        protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
            Principal result = null;
            String authToken = null;
            HttpHeaders headers = request.getHeaders();

            if (log.isDebugEnabled()) {
                log.debug("Determining user...");
            }

            if (headers.containsKey(ServerConstants.X_AUTH_TOKEN)) {
                authToken = headers.getFirst(ServerConstants.X_AUTH_TOKEN);
                authenticate(authToken);
            } else if (attributes.containsKey(ServerConstants.X_AUTH_TOKEN)) {
                authToken = (String) attributes.get(ServerConstants.X_AUTH_TOKEN);

                authenticate(authToken);
            } else {
                result = super.determineUser(request, wsHandler, attributes);
            }

            return result;
        }

        private void authenticate(String authToken) {
            if (log.isDebugEnabled() && StringUtils.isNotEmpty(authToken)) {
                log.debug("Header auth token: " + authToken);
            }

            if (StringUtils.isNotBlank(authToken)) {

                // set cached authenticated user back in the spring security context
                Authentication authentication = authenticationManager.authenticate(new PreAuthenticatedAuthenticationToken(authToken, "N/A"));

                if (log.isDebugEnabled()) {
                    log.debug("Adding Authentication to SecurityContext for WebSocket call: " + authentication);
                }
                SpringSecurityHelper.setAuthentication(authentication);

            }
        }
    }
}
