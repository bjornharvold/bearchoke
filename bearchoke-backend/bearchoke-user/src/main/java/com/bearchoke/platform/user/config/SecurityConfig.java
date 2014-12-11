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

package com.bearchoke.platform.user.config;

import com.bearchoke.platform.user.repositories.UserRepository;
import com.bearchoke.platform.user.repositories.impl.UserRepositoryImpl;
import com.bearchoke.platform.user.security.UserAuthenticationProvider;
import com.bearchoke.platform.user.security.UserDetailsServiceImpl;
import org.axonframework.commandhandling.CommandBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class SecurityConfig extends GlobalMethodSecurityConfiguration {

    @Inject
    private CommandBus commandBus;

    @Inject
    private UserRepository userRepository;

    @Bean(name = "userDetailsService")
    public UserDetailsServiceImpl userDetailsService() {
        return new UserDetailsServiceImpl(userRepository);
    }

    @Bean(name = "authenticationProvider")
    public UserAuthenticationProvider authenticationProvider() {
        return new UserAuthenticationProvider(commandBus);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.authenticationProvider(authenticationProvider());
    }

}
