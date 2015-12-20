/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.persistence.rabbitmq.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.inject.Inject;

/**
 * Created by Bjorn Harvold
 * Date: 8/18/14
 * Time: 5:13 PM
 * Responsibility:
 */
@Configuration
@Profile("rabbit-local")
@PropertySource(value = "classpath:rabbit-local.properties")
public class RabbitMQLocalConfig {

    @Inject
    private Environment environment;

    /**
     * Bean is our direct connection to RabbitMQ
     * @return CachingConnectionFactory
     */
    @Bean(destroyMethod = "destroy")
    public ConnectionFactory rabbitConnectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory(
                environment.getProperty("rabbitmq.host"),
                environment.getProperty("rabbitmq.port", Integer.class)
        );
        factory.setUsername(environment.getProperty("rabbitmq.username"));
        factory.setPassword(environment.getProperty("rabbitmq.password"));

        return factory;
    }

}
