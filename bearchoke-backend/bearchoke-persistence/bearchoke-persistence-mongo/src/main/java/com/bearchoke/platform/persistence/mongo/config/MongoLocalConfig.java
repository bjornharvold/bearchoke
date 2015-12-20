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

package com.bearchoke.platform.persistence.mongo.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import javax.inject.Inject;
import java.net.UnknownHostException;

/**
 * Created by Bjorn Harvold
 * Date: 1/9/14
 * Time: 11:55 PM
 * Responsibility:
 */
@Configuration
@Profile("mongodb-local")
@PropertySource(value = "classpath:mongodb-local.properties")
public class MongoLocalConfig {

    @Inject
    private Environment environment;

    @Bean
    public Mongo mongo() throws UnknownHostException {
        // location of db
        ServerAddress sa = new ServerAddress(
                environment.getProperty("mongodb.host"),
                environment.getProperty("mongodb.port", Integer.class)
        );

        // set optional default parameters here
        MongoClientOptions.Builder builder = MongoClientOptions.builder();

        // none yet

        MongoClientOptions options = builder.build();

        return new MongoClient(sa, options);
    }

    @Bean(name = "mongoDbFactory")
    public SimpleMongoDbFactory mongoDbFactory() throws Exception {
        return new SimpleMongoDbFactory(mongo(), environment.getProperty("mongodb.database"));
    }

    @Bean(name = "axonMongoDbFactory")
    public SimpleMongoDbFactory axonMongoDbFactory() throws Exception {
        return mongoDbFactory();
    }
}
