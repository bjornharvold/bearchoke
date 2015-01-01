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

package com.bearchoke.platform.mongo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;

import javax.inject.Inject;

/**
 * Created by Bjorn Harvold
 * Date: 1/9/14
 * Time: 11:55 PM
 * Responsibility:
 */
@Configuration
@Profile("mongodb-cloud")
@PropertySource(value = "classpath:mongodb-cloud.properties")
@Slf4j
public class MongoCloudConfig extends AbstractCloudConfig {

    @Inject
    private Environment environment;

    @Bean(name = "mongoDbFactory")
    public MongoDbFactory mongoDbFactory() {
        log.info("Retrieving mongoDbFactory for application. Service ID: " + environment.getProperty("cf.bearchoke.mongodb.serviceid"));
        return connectionFactory().mongoDbFactory(environment.getProperty("cf.bearchoke.mongodb.serviceid"));
    }

    @Bean(name = "axonMongoDbFactory")
    public MongoDbFactory axonMongoDbFactory() {
        log.info("Retrieving mongoDbFactory for axon. Service ID: " + environment.getProperty("cf.bearchoke.axon.serviceid"));
        return connectionFactory().mongoDbFactory(environment.getProperty("cf.bearchoke.axon.serviceid"));
    }

}
