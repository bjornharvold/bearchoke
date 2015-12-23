/*
 * Copyright 2015 the original author or authors.
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

package com.bearchoke.platform.server.frontend.web.config;

import com.bearchoke.platform.server.common.security.ApiAuthenticationFailureHandler;
import com.bearchoke.platform.server.common.security.ApiAuthenticationSuccessHandler;
import com.bearchoke.platform.server.frontend.service.GreetingService;
import com.bearchoke.platform.server.frontend.service.GreetingServiceImpl;
import com.bearchoke.platform.domain.user.document.Role;
import com.bearchoke.platform.domain.user.document.User;
import com.bearchoke.platform.domain.user.repositories.UserRepository;
import com.bearchoke.platform.domain.user.security.PreAuthUserDetailsService;
import com.bearchoke.platform.domain.user.security.PreAuthenticatedTokenCacheService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.log4j.Log4j2;
import org.axonframework.commandhandling.CommandBus;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.geo.GeoJsonModule;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.zalando.jackson.datatype.money.MoneyModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;

/**
 * Created by Bjorn Harvold
 * <p>
 * Date: 1/3/14
 * <p>
 * Time: 4:12 PM
 * <p>
 * Responsibility:
 */

@Configuration
@Log4j2
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class MockServerConfig extends GlobalMethodSecurityConfiguration {

    @Bean(name = "customObjectMapper")
    public ObjectMapper customObjectMapper() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();

        builder.featuresToDisable(
                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
        );

        builder.featuresToEnable(
                SerializationFeature.WRITE_DATES_WITH_ZONE_ID,
//                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
//                SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS,
//                DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS,
                DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT
        );

        builder.indentOutput(true);

        builder.failOnEmptyBeans(false);
        builder.failOnUnknownProperties(false);

        // do not include null value in json to make object graph smaller
        builder.serializationInclusion(JsonInclude.Include.NON_NULL);

        builder.modules(new GeoJsonModule(), new JavaTimeModule(), new MoneyModule());

        return builder.build();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.authenticationProvider(authenticationProvider());
    }

    @Bean(name = "commandBus")
    public CommandBus commandBus() {
        return mock(CommandBus.class);
    }

    @Bean(name = "preAuthenticatedTokenCacheService")
    public PreAuthenticatedTokenCacheService preAuthenticatedTokenCacheService() {
        return mock(PreAuthenticatedTokenCacheService.class);
    }

    @Bean(name = "cacheManager")
    public CacheManager cacheManager() {
        return mock(CacheManager.class);
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate redisTemplate() {
        return mock(RedisTemplate.class);
    }

    @Bean(name = "userDetailsService")
    public UserDetailsService inMemoryUserDetailsManager() {
        List<UserDetails> users = new ArrayList<>(1);
        users.add(new User("user", "user@user.com", "User", "User", "user", Arrays.asList(new Role("ROLE_USER", Arrays.asList("RIGHT_AS_USER")))));
        users.add(new User("facebook@user.com", "facebook@user.com", "Facebook", "User", "facebook", Arrays.asList(new Role("ROLE_USER", Arrays.asList("RIGHT_AS_USER")))));

        return new InMemoryUserDetailsManager(users);
    }

    @Bean(name = "authenticationProvider")
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider result = new DaoAuthenticationProvider();
        result.setUserDetailsService(inMemoryUserDetailsManager());

        return result;
    }

    @Bean(name = "apiAuthenticationSuccessHandler")
    public ApiAuthenticationSuccessHandler apiAuthenticationSuccessHandler() {
        return new ApiAuthenticationSuccessHandler(preAuthenticatedTokenCacheService());
    }

    @Bean(name = "apiAuthenticationFailureHandler")
    public ApiAuthenticationFailureHandler apiAuthenticationFailureHandler() {
        return new ApiAuthenticationFailureHandler(customObjectMapper());
    }

    @Bean(name = "greetingService")
    public GreetingService greetingService() {
        return new GreetingServiceImpl();
    }

    @Bean(name = "userRepository")
    public UserRepository userRepository() {
        return mock(UserRepository.class);
    }

    @Bean(name = "messageSendingOperations")
    public MessageSendingOperations messageSendingOperations() {
        return mock(MessageSendingOperations.class);
    }

    @Bean(name = "springSessionRepositoryFilter")
    public SessionRepositoryFilter sessionRepositoryFilter() {
        return new SessionRepositoryFilter(new MapSessionRepository());
    }

    @Bean(name = "preAuthUserDetailsService")
    public PreAuthUserDetailsService preAuthUserDetailsService() {
        PreAuthUserDetailsService result = mock(PreAuthUserDetailsService.class);

        // set this bean to never return anything of value
        given(result.loadUserDetails(anyObject())).willThrow(new UsernameNotFoundException("Pre authenticated token not found"));

        return result;
    }

    @Bean(name = "preAuthAuthenticationManager")
    public AuthenticationManager preAuthAuthenticationManager() {
        PreAuthenticatedAuthenticationProvider preAuthProvider = new PreAuthenticatedAuthenticationProvider();
        preAuthProvider.setPreAuthenticatedUserDetailsService(preAuthUserDetailsService());

        return new ProviderManager(Arrays.asList(preAuthProvider));
    }


}
