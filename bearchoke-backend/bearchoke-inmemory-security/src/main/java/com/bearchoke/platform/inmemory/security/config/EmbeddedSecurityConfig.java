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

package com.bearchoke.platform.inmemory.security.config;

import com.bearchoke.platform.inmemory.security.User;
import com.bearchoke.platform.inmemory.security.service.impl.ExtendedInMemoryUserDetailsManagerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 1/3/14
 * Time: 4:13 PM
 * Responsibility:
 */
@Configuration
@Profile("in-memory")
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class EmbeddedSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        DaoAuthenticationProvider ap = new DaoAuthenticationProvider();
        ap.setUserDetailsService(inMemoryUserDetailsManager());

        builder.authenticationProvider(ap);
        builder.userDetailsService(inMemoryUserDetailsManager());
    }

    @Bean(name = "userDetailsService")
    public ExtendedInMemoryUserDetailsManagerImpl inMemoryUserDetailsManager() {
        List<GrantedAuthority> userRoles = new ArrayList<>();
        userRoles.add(new SimpleGrantedAuthority("ROLE_USER"));

        List<GrantedAuthority> adminRoles = new ArrayList<>();
        adminRoles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        adminRoles.add(new SimpleGrantedAuthority("ROLE_USER"));

        List<UserDetails> users = new ArrayList<>();

        users.add(new User("1", "harrymitchell", "harry@mitchell.com", "harry", userRoles));
        users.add(new User("2", "sallymalone", "sally@malone.com", "sally", adminRoles));

        return new ExtendedInMemoryUserDetailsManagerImpl(users);
    }

    @Bean(name = "authenticationProvider")
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider ap = new DaoAuthenticationProvider();
        ap.setUserDetailsService(inMemoryUserDetailsManager());

        return ap;
    }

}
