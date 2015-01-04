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
