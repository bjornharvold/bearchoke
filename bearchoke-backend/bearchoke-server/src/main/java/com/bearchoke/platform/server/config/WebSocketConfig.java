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

package com.bearchoke.platform.server.config;

import com.bearchoke.platform.platform.base.security.SpringSecurityHelper;
import com.bearchoke.platform.server.ServerConstants;
import com.bearchoke.platform.server.jackson.CustomObjectMapper;
import com.bearchoke.platform.server.security.ApiPreAuthenticatedTokenCacheService;
import com.bearchoke.platform.server.web.ApplicationMediaType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.ContentTypeResolver;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.MimeType;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.inject.Inject;
import java.security.Principal;
import java.util.ArrayList;
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
@ComponentScan("com.bearchoke.platform.server.websocket")
@Slf4j
public class WebSocketConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Inject
    private CustomObjectMapper objectMapper;

    @Inject
    private ApiPreAuthenticatedTokenCacheService apiPreAuthenticatedTokenCacheService;

    @Inject
    @Qualifier("preAuthenticationManager")
    private AuthenticationManager preAuthenticationManager;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setHandshakeHandler(new SecureHandshakeHandler(preAuthenticationManager))
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
    protected void configure(MessageSecurityMetadataSourceRegistry registry) {
        registry
                .destinationMatchers("/user/queue/errors").permitAll()
                .destinationMatchers("/user/*").hasRole("USER")
                .destinationMatchers("/app/user/*").hasRole("USER")
                .destinationMatchers("/*").permitAll();
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
//        config.enableSimpleBroker("/queue/", "/topic/");
        config.enableStompBrokerRelay("/queue/", "/topic/");

        // only if we want to use . instead of / for path separator e.g. /app/user.chat
//        config.setPathMatcher(new AntPathMatcher("."));
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
                    Authentication authentication = preAuthenticationManager.authenticate(new PreAuthenticatedAuthenticationToken(authToken, "N/A"));

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
