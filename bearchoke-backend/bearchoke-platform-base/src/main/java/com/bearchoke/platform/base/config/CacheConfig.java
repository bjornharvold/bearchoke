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

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.ExpiringSession;

import javax.inject.Inject;
import java.util.Arrays;
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
@Slf4j
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
        cacheManager.setCacheNames(Arrays.asList(userTokenCache));

        return cacheManager;
    }
}
