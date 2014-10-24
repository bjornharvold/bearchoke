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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.ExpiringSession;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;

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
