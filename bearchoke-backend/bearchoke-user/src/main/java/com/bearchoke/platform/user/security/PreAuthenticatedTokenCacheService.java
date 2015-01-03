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

package com.bearchoke.platform.user.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.session.ExpiringSession;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service("preAuthenticatedTokenCacheService")
public class PreAuthenticatedTokenCacheService {

    private final Environment environment;
    private final CacheManager cacheManager;
    private final RedisTemplate<String,ExpiringSession> redisTemplate;

    @Autowired
    public PreAuthenticatedTokenCacheService(Environment environment, CacheManager cacheManager, RedisTemplate<String, ExpiringSession> redisTemplate) {
        this.environment = environment;
        this.cacheManager = cacheManager;
        this.redisTemplate = redisTemplate;
    }

    public void putInCache(String xAuthToken, UserDetails user) {
		cacheManager.getCache(environment.getProperty("user.session.cache.name")).put(xAuthToken, user);
	}
	
	public UserDetails getFromCache(String xAuthToken) {
		return cacheManager.getCache(environment.getProperty("user.session.cache.name")).get(xAuthToken, User.class);
	}

    public Set<String> findKeysMatching(String key) {
        return redisTemplate.keys(key);
    }

    public void deleteExistingSessions(List<String> keys) {
        redisTemplate.delete(keys);
    }

    public void updateExpiration(String key) {
        Long expiration = environment.getProperty("cache.default.timeout", Long.class);

        Boolean result = redisTemplate.expire(key, expiration, TimeUnit.SECONDS);

        if (log.isTraceEnabled()) {
            log.trace("Extended expiration time for key: " + key + " with: " + expiration + " seconds. Success: " + result);
        }
    }
	
}
