/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.persistence.rabbitmq.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("cloud")
@PropertySource(value = "classpath:rabbit-cloud.properties")
@Log4j2
public class RabbitMQCloudConfig extends AbstractCloudConfig {

    @Bean
    public ConnectionFactory rabbitConnectionFactory() {
        if (log.isInfoEnabled()) {
            log.info("Attempting to retrieve RabbitMQ instance from Cloud Foundry...");
        }

        ConnectionFactory factory = connectionFactory().rabbitConnectionFactory();

        if (factory != null) {
            if (log.isInfoEnabled()) {
                log.info("Retrieved RabbitMQ service successfully: " + factory.toString());
            }
        } else {
            if (log.isErrorEnabled()) {
                log.error("Could not find RabbitMQ service");
            }
        }

        return factory;
    }

}
