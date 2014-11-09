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

package com.bearchoke.platform.platform.base.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
@Profile("redis-cloud")
@Slf4j
public class RedisCloudConfig extends AbstractCloudConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        if (log.isInfoEnabled()) {
            log.info("Attempting to retrieve Redis instance from Cloud Foundry...");
        }

        RedisConnectionFactory factory = connectionFactory().redisConnectionFactory();

        if (factory != null) {
            if (log.isInfoEnabled()) {
                log.info("Retrieved Redis service successfully: " + factory.toString());
            }
        } else {
            if (log.isErrorEnabled()) {
                log.error("Could not find Redis service");
            }
        }

        return factory;
    }

}
