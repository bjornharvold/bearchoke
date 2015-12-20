/*
 * Copyright (c) 2015. Traveliko
 */

package com.bearchoke.platform.persistence.mongo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.xstream.XStream;
import com.bearchoke.platform.persistence.mongo.axon.AxonMongoTemplate;
import com.bearchoke.platform.persistence.mongo.axon.AxonSagaMongoTemplate;
import org.axonframework.commandhandling.AsynchronousCommandBus;
import org.axonframework.commandhandling.CommandDispatchInterceptor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.CommandGatewayFactoryBean;
import org.axonframework.commandhandling.gateway.IntervalRetryScheduler;
import org.axonframework.commandhandling.interceptors.BeanValidationInterceptor;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.eventsourcing.Snapshotter;
import org.axonframework.eventsourcing.SpringAggregateSnapshotter;
import org.axonframework.eventstore.mongo.MongoEventStore;
import org.axonframework.repository.LockManager;
import org.axonframework.repository.PessimisticLockManager;
import org.axonframework.saga.SagaRepository;
import org.axonframework.saga.repository.mongo.MongoSagaRepository;
import org.axonframework.saga.spring.SpringResourceInjector;
import org.axonframework.serializer.xml.XStreamSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.MongoDbFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Bjorn Harvold
 * Date: 7/19/14
 * Time: 10:36 PM
 * Responsibility:
 */
@Configuration
public class AxonCQRSConfig {

    @Inject
    private ObjectMapper objectMapper;

    @Inject
    private XStream xStream;

    @Inject
    @Qualifier("taskExecutor")
    private TaskExecutor taskExecutor;

    @Inject
    @Qualifier("axonMongoDbFactory")
    private MongoDbFactory mongoDbFactory;

    @Bean(name = "commandBus", destroyMethod = "shutdown")
    public AsynchronousCommandBus commandBus() {
        AsynchronousCommandBus commandBus = new AsynchronousCommandBus();

        List<CommandDispatchInterceptor> interceptors = new ArrayList<>(1);
        interceptors.add(new BeanValidationInterceptor());
        commandBus.setDispatchInterceptors(interceptors);

        return commandBus;
    }

    @Bean(name = "commandGateway")
    public CommandGateway commandGateway() {
        CommandGateway commandGateway = null;
        CommandGatewayFactoryBean factory = new CommandGatewayFactoryBean();
        factory.setCommandBus(commandBus());

        // create a retry scheduler to re-run the command that failed with an exception
        // I put this in place to contend with concurrency exceptions [BH]
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        factory.setRetryScheduler(new IntervalRetryScheduler(ses, 500, 2));
//        factory.setGatewayInterface();


        try {
            factory.afterPropertiesSet();
            commandGateway = (CommandGateway) factory.getObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return commandGateway;
    }

    // In case we want something more robust
//    @Bean(name = "eventBus")
//    public SpringMessagingEventBus eventBus() {
//        SpringMessagingEventBus eventBus = new SpringMessagingEventBus();
//        eventBus.setChannel(defaultInputChannel);
//
//        return eventBus;
//    }

    @Bean(name = "eventBus")
    public EventBus eventBus() {
        EventBus eventBus = new SimpleEventBus();

        return eventBus;
    }

    @Bean(name = "lockManager")
    public LockManager lockManager() {
//        OptimisticLockManager lockManager = new OptimisticLockManager();
        PessimisticLockManager lockManager = new PessimisticLockManager(); // default Axon lock manager

        return lockManager;
    }

    @Bean(name = "axonMongoTemplate")
    public AxonMongoTemplate axonMongoTemplate() {
        return new AxonMongoTemplate(mongoDbFactory);
    }

    @Bean(name = "axonSagaMongoTemplate")
    public AxonSagaMongoTemplate mongoSagaTemplate() {
        return new AxonSagaMongoTemplate(mongoDbFactory);
    }

    @Bean(name = "eventStore")
    public MongoEventStore eventStore() {
//        JacksonSerializer jsonSerializer = new JacksonSerializer(objectMapper);
        XStreamSerializer xStreamSerializer = new XStreamSerializer(xStream);

        return new MongoEventStore(xStreamSerializer, axonMongoTemplate());
    }

    @Bean(name = "sagaRepository")
    public SagaRepository sagaRepository() {
        MongoSagaRepository sagaRepository = new MongoSagaRepository(mongoSagaTemplate());
        sagaRepository.setResourceInjector(new SpringResourceInjector());

        return sagaRepository;
    }

    @Bean(name = "snapshotter")
    public Snapshotter snapshotter() {
        SpringAggregateSnapshotter sas = new SpringAggregateSnapshotter();
        sas.setEventStore(eventStore());
        sas.setExecutor(taskExecutor);

        return sas;
    }

}
