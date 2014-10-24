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

package com.bearchoke.platform.mongo.security.config;

import com.bearchoke.platform.mongo.repository.MongoDbUserRepository;
import com.bearchoke.platform.mongo.security.spring.impl.MongoDbUserDetailsServiceImpl;
import org.jasypt.springsecurity3.authentication.encoding.PasswordEncoder;
import org.jasypt.util.password.PasswordEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

import javax.inject.Inject;

/**
 * Created by Bjorn Harvold
 * Date: 1/3/14
 * Time: 4:13 PM
 * Responsibility:
 */

/**
 * Properties to support the 'mongodb' mode of operation.
 * This mode uses MongoDb databases and basic documents for user persistence and assumes a MongoDb instance is available
 */
@Configuration
@Profile("mongodb")
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class MongoSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Inject
    private PasswordEncryptor passwordEncryptor;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private MongoDbUserRepository mongoDbUserRepository;

    @Bean
    public MongoDbUserDetailsServiceImpl mongoDbUserDetailsService() {
        return new MongoDbUserDetailsServiceImpl(mongoDbUserRepository, passwordEncryptor);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        DaoAuthenticationProvider ap = new DaoAuthenticationProvider();
        ap.setUserDetailsService(mongoDbUserDetailsService());
        ap.setPasswordEncoder(passwordEncoder);

        builder.authenticationProvider(ap);
    }

}
