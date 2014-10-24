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

package com.bearchoke.platform.jpa.security.config;

import com.bearchoke.platform.jpa.repository.JpaUserRepository;
import com.bearchoke.platform.jpa.security.spring.impl.JpaUserDetailsServiceImpl;
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
@Configuration
@Profile("jpa")
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class JpaSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Inject
    private PasswordEncryptor passwordEncryptor;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private JpaUserRepository jpaUserRepository;

    @Bean
    public JpaUserDetailsServiceImpl jpaUserDetailsService() {
        return new JpaUserDetailsServiceImpl(jpaUserRepository, passwordEncryptor);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        DaoAuthenticationProvider ap = new DaoAuthenticationProvider();
        ap.setUserDetailsService(jpaUserDetailsService());
        ap.setPasswordEncoder(passwordEncoder);

        builder.authenticationProvider(ap);
    }

}
