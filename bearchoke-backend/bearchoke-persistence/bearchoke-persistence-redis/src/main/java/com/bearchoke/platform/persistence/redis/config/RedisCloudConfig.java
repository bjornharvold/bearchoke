/*
 * Copyright (c) 2015. Bearchoke
 */

package com.traveliko.platform.persistence.redis.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
@Profile("redis-cloud")
@Log4j2
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
