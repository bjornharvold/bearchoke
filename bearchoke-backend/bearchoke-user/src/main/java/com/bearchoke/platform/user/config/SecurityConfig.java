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

import com.bearchoke.platform.user.security.PreAuthUserDetailsService;
import com.bearchoke.platform.user.security.UserAuthenticationProvider;
import org.axonframework.commandhandling.CommandBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

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
    private PreAuthUserDetailsService apiPreAuthUserDetailsService;

    @Bean(name = "authenticationProvider")
    public UserAuthenticationProvider authenticationProvider() {
        return new UserAuthenticationProvider(commandBus);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.authenticationProvider(authenticationProvider());
    }

    @Bean(name = "preAuthAuthenticationManager")
    public AuthenticationManager preAuthAuthenticationManager() {
        PreAuthenticatedAuthenticationProvider preAuthProvider = new PreAuthenticatedAuthenticationProvider();
        preAuthProvider.setPreAuthenticatedUserDetailsService(apiPreAuthUserDetailsService);

        List<AuthenticationProvider> providers = new ArrayList<AuthenticationProvider>();
        providers.add(preAuthProvider);

        return new ProviderManager(providers);
    }
}
