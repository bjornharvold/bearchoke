/*
 * Copyright (c) 2015. Bearchoke
 */

package com.traveliko.platform.persistence.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import javax.inject.Inject;

/**
 * Created by Bjorn Harvold
 * Date: 8/28/14
 * Time: 11:41 PM
 * Responsibility:
 */
@Configuration
@Profile("redis-local")
@PropertySource("classpath:redis-local.properties")
public class RedisLocalConfig {

    @Inject
    private Environment environment;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() throws Exception {
        JedisConnectionFactory jcf = new JedisConnectionFactory();
        jcf.setHostName(environment.getProperty("redis.hostname"));
        jcf.setPassword(environment.getProperty("redis.password"));
        jcf.setPort(environment.getProperty("redis.port", Integer.class));

        return jcf;
    }

}
