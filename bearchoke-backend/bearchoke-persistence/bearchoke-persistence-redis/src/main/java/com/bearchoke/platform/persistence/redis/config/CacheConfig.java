/*
 * Copyright (c) 2015. Bearchoke
 */

package com.traveliko.platform.persistence.redis.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.ExpiringSession;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Bjorn Harvold
 * Date: 9/21/14
 * Time: 11:52 PM
 * Responsibility:
 */
@Configuration
@PropertySource("classpath:cache.properties")
@Log4j2
public class CacheConfig {

    @Inject
    private Environment environment;

    @Inject
    private RedisTemplate<String, ExpiringSession> redisTemplate;

    @Bean
    public CacheManager cacheManager() {
        String userTokenCache = environment.getProperty("user.session.cache.name");
        Long expiration = environment.getProperty("cache.default.timeout", Long.class);

        log.info("Creating an initial user token cache called: '" + userTokenCache + "' with expiration time: " + expiration + " seconds");

        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        Map<String, Long> expires = new ConcurrentHashMap<>(1);
        expires.put(userTokenCache, expiration);
        cacheManager.setExpires(expires);
        cacheManager.setDefaultExpiration(expiration);
        cacheManager.setCacheNames(Collections.singletonList(userTokenCache));

        return cacheManager;
    }
}
