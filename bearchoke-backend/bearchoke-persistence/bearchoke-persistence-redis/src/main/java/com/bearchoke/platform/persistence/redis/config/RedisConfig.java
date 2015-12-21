/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.persistence.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.ExpiringSession;

import javax.inject.Inject;

/**
 * Created by Bjorn Harvold
 * Date: 8/28/14
 * Time: 11:41 PM
 * Responsibility:
 */
@Configuration
public class RedisConfig {

    @Inject
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * SpringSession config
     * @return
     * @throws Exception
     */
    @Bean
    public RedisTemplate<String,ExpiringSession> redisTemplate() throws Exception {
        RedisTemplate<String, ExpiringSession> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

}
