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

package com.bearchoke.platform.base.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.interceptor.MessageSelectingInterceptor;
import org.springframework.integration.channel.interceptor.WireTap;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.dispatcher.BroadcastingDispatcher;
import org.springframework.integration.endpoint.EventDrivenConsumer;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.selector.PayloadTypeSelector;
import org.springframework.integration.websocket.ServerWebSocketContainer;
import org.springframework.integration.websocket.inbound.WebSocketInboundChannelAdapter;
import org.springframework.integration.websocket.outbound.WebSocketOutboundMessageHandler;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.ChannelInterceptor;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 8/18/14
 * Time: 5:13 PM
 * Responsibility:
 */
@Configuration
@PropertySource("classpath:spring-integration.properties")
@EnableIntegration
public class SpringIntegrationConfig {

    @Inject
    private Environment environment;

    @Inject
    private ConnectionFactory connectionFactory;

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setQueue(environment.getProperty("rabbitmq.queue"));
        return template;
    }

    /**
     * Manager
     * @return
     */
    @Bean(initMethod = "initialize")
    public RabbitAdmin rabbitAdmin() {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.declareExchange(topicExchange());
        rabbitAdmin.declareQueue(queue());
        rabbitAdmin.declareBinding(
                BindingBuilder.bind(queue()).to(topicExchange()).with(environment.getProperty("exchange.routing.key"))
        );

        return rabbitAdmin;
    }

    /**
     * Queue to use to push messages onto
     * @return
     */
    @Bean
    public Queue queue() {
        Queue queue = new Queue(environment.getProperty("rabbitmq.queue"));

        return queue;
    }

    @Bean
    public TopicExchange topicExchange() {
        TopicExchange exchange = new TopicExchange(environment.getProperty("rabbitmq.exchange"), true, true);

        return exchange;
    }

    /**
     * SubscribableChannel for Axon CQRS to use
     * @return
     */
    @Bean(name = "webSocketInputChannel")
    public PublishSubscribeChannel webSocketInputChannel() {
        PublishSubscribeChannel channel = new PublishSubscribeChannel();
        List<ChannelInterceptor> list = new ArrayList<>(1);
        list.add(messageSelectingInterceptor());
        channel.setInterceptors(list);

//        channel.setDatatypes(Object.class); // we've defined it using the PayloadTypeSelector instead and injected it as an interceptor above
        return channel;
    }

    /**
     * Default endpoint
     * @return
     */
    @Bean(destroyMethod = "stop")
    public EventDrivenConsumer eventDrivenConsumer() {
        EventDrivenConsumer consumer = new EventDrivenConsumer(webSocketInputChannel(), loggingHandler());
        consumer.setAutoStartup(true);

        return consumer;
    }

    @Bean(destroyMethod = "destroy")
    public SimpleMessageListenerContainer simpleMessageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueues(queue());
        container.setMessageListener(loggingMessageListenerAdapter());
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);

        return container;
    }

    /**
     * Wraps our LoggingHandler in a MessageListenerAdapter to be used by a MessageListenerContainer
     * @return
     */
    @Bean(name = "loggingMessageListenerAdapter")
    public MessageListenerAdapter loggingMessageListenerAdapter() {
        MessageListenerAdapter adapter = new MessageListenerAdapter(loggingHandler());

        return adapter;
    }

    /**
     * This message handler is just used for logging purposes
     * @return
     */
    @Bean(name = "loggingHandler")
    public LoggingHandler loggingHandler() {
        LoggingHandler loggingHandler = new LoggingHandler("DEBUG");

        return loggingHandler;
    }

    @Bean
    public BroadcastingDispatcher broadcastingDispatcher() {
        BroadcastingDispatcher dispatcher = new BroadcastingDispatcher();
        dispatcher.addHandler(loggingHandler());

        return dispatcher;
    }

    /**
     * Interceptor intercepts messages on channels before the message go to the designated channel endpoint
     *
     preSend is invoked before a message is sent and returns the message that will be
     sent to the channel when the method returns. If the method returns null, nothing
     is sent. This allows the implementation to control what gets sent to the
     channel, effectively filtering the messages.

     postSend is invoked after an attempt to send the message has been made. It
     indicates whether the attempt was successful through the boolean flag it passes
     as an argument. This allows the implementation to monitor the message flow
     and learn which messages are sent and which ones fail.

     preReceive applies only if the channel is pollable. It’s invoked when a component
     calls receive() on the channel, but before a Message is actually read from
     that channel. It allows implementers to decide whether the channel can return
     a message to the caller.

     postReceive, like preReceive, applies only to pollable channels. It’s invoked
     after a message is read from a channel but before it’s returned to the component
     that called receive(). If it returns null, then no message is received. This allows
     the implementer to control what, if anything, is actually received by the poller.

     * @return
     */
    @Bean(name = "wireTap")
    public ChannelInterceptor wireTap() {
        WireTap interceptor = new WireTap(webSocketInputChannel());

        return interceptor;
    }

    /**
     * This channel can be used to monitor messages from our main channel without interrupting it.
     * @return
     */
    @Bean(name = "monitoringChannel")
    public PublishSubscribeChannel monitoringChannel() {
        PublishSubscribeChannel channel = new PublishSubscribeChannel();
        List<ChannelInterceptor> list = new ArrayList<>(1);
        list.add(wireTap());
        channel.setInterceptors(list);

        return channel;
    }

    /**
     * A MessageSelector filters out messages that are not valid on the specific MessageChannel
     * @return
     */
    @Bean
    public PayloadTypeSelector payloadTypeSelector() {
        // accepts everything - we prolly want to narrow that down when we know which object is coming through
        PayloadTypeSelector selector = new PayloadTypeSelector(Object.class);

        return selector;
    }

    /**
     * Wraps our payload type selector as a channel interceptor
     * @return
     */
    @Bean(name = "messageSelectingInterceptor")
    public ChannelInterceptor messageSelectingInterceptor() {
        MessageSelectingInterceptor interceptor = new MessageSelectingInterceptor(payloadTypeSelector());

        return interceptor;
    }

    @Bean
    public ServerWebSocketContainer serverWebSocketContainer() {
        return new ServerWebSocketContainer("/ws").withSockJs();
    }

    @Bean
    public MessageProducer webSocketInboundChannelAdapter() {
        WebSocketInboundChannelAdapter webSocketInboundChannelAdapter =
                new WebSocketInboundChannelAdapter(serverWebSocketContainer());
        webSocketInboundChannelAdapter.setOutputChannel(webSocketInputChannel());
        return webSocketInboundChannelAdapter;
    }

//    @Bean
//    @Transformer(inputChannel = "webSocketInputChannel", outputChannel = "webSocketOutputChannel")
//    public ExpressionEvaluatingTransformer transformer() {
//        return new ExpressionEvaluatingTransformer(PARSER.parseExpression("'Hello ' + payload"));
//    }

    @Bean
    @ServiceActivator(inputChannel = "webSocketOutputChannel")
    public MessageHandler webSocketOutboundMessageHandler() {
        return new WebSocketOutboundMessageHandler(serverWebSocketContainer());
    }
}
