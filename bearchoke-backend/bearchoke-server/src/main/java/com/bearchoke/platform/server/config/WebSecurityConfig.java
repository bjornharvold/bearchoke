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

package com.bearchoke.platform.server.config;

import com.bearchoke.platform.server.jackson.CustomObjectMapper;
import com.bearchoke.platform.server.security.ApiAuthenticationFailureHandler;
import com.bearchoke.platform.server.security.ApiAuthenticationSuccessHandler;
import com.bearchoke.platform.server.security.ApiRequestHeaderAuthenticationFilter;
import com.bearchoke.platform.server.security.UnauthorizedEntryPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.HstsHeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.session.web.http.SessionRepositoryFilter;

import javax.inject.Inject;
import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 1/7/14
 * Time: 9:44 PM
 * Responsibility:
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String API_ADMINISTRATION_URL = "/api/administration/*";
    private static final String API_MANAGER_URL = "/api/manager/*";
    private static final String API_USER_URL = "/api/secured/*";
    private static final String WEBSOCKET_URL = "/ws/*";
    private static final String API_LOGIN_URL = "/api/authenticate";
    private static final String API_PUBLIC_URL = "/*";

    @Inject
    private ApiAuthenticationSuccessHandler apiAuthenticationSuccessHandler;

    @Inject
    private ApiAuthenticationFailureHandler apiAuthenticationFailureHandler;

    @Inject
    @Qualifier("userDetailsService")
    private UserDetailsService userDetailsService;

    @Inject
    @Qualifier("authenticationProvider")
    private AuthenticationProvider authenticationProvider;

    @Inject
    @Qualifier("preAuthAuthenticationManager")
    private AuthenticationManager preAuthAuthenticationManager;

    @Inject
    private CustomObjectMapper objectMapper;

    @Inject
    @Qualifier("springSessionRepositoryFilter")
    private SessionRepositoryFilter sessionRepositoryFilter;

    /**
     * Commons url security strategy
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("Configuring springSecurityFilterChain...");

        // header details
        configureHeaders(http.headers());

        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // the url patterns to secure
        http
                .authorizeRequests()
                .antMatchers(API_ADMINISTRATION_URL).hasRole("ADMIN")
                .antMatchers(API_MANAGER_URL).hasRole("MANAGER")
                .antMatchers(API_USER_URL).hasRole("USER")
                .antMatchers(API_LOGIN_URL).anonymous()
                .antMatchers(API_PUBLIC_URL).permitAll()
        ;

        // filter details
        http
                .addFilterBefore(sessionRepositoryFilter, ChannelProcessingFilter.class)
                .addFilterAfter(authFilter(), ApiRequestHeaderAuthenticationFilter.class)
                .addFilter(preAuthFilter())
                .csrf().disable();
        http
                .csrf().disable();

        http
                .exceptionHandling().authenticationEntryPoint(new UnauthorizedEntryPoint(objectMapper));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(HttpMethod.GET, "/resources/*");
    }

    /**
     * HTTP Strict Transport Security
     * http://tools.ietf.org/html/rfc6797
     * @param headers
     * @throws Exception
     */
    private static void configureHeaders(HeadersConfigurer<?> headers) throws Exception {
        HstsHeaderWriter writer = new HstsHeaderWriter(false);
        writer.setRequestMatcher(AnyRequestMatcher.INSTANCE);
        headers.contentTypeOptions().xssProtection().cacheControl().addHeaderWriter(writer).frameOptions();

        headers.addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
        auth.userDetailsService(userDetailsService);
    }

    @Bean(name = "userDetailsService")
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

    @Bean(name = "authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean(name = "authFilter")
    public Filter authFilter() throws Exception {
        log.info("Creating authFilter...");

        RequestMatcher antReqMatch = new AntPathRequestMatcher(API_LOGIN_URL);

        List<RequestMatcher> reqMatches = new ArrayList<>();
        reqMatches.add(antReqMatch);
        RequestMatcher reqMatch = new AndRequestMatcher(reqMatches);

        UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter();
        filter.setPostOnly(true);
        filter.setUsernameParameter(USERNAME);
        filter.setPasswordParameter(PASSWORD);
        filter.setRequiresAuthenticationRequestMatcher(reqMatch);
        filter.setAuthenticationSuccessHandler(apiAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(apiAuthenticationFailureHandler);
        filter.setAuthenticationManager(authenticationManager());

        return filter;
    }

    @Bean(name = "preAuthFilter")
    public Filter preAuthFilter() {
        log.info("Creating preAuthFilter...");
        ApiRequestHeaderAuthenticationFilter filter = new ApiRequestHeaderAuthenticationFilter();
        filter.setAuthenticationManager(preAuthAuthenticationManager);
        return filter;
    }


//
//    @Bean
//    public AccessDecisionManager accessDecisionManager() {
//        List<AccessDecisionVoter> voters = new ArrayList<>(1);
////        voters.add(new RoleVoter());
////        voters.add(new AuthenticatedVoter());
//        voters.add(new WebExpressionVoter());
//
//        AffirmativeBased ab = new AffirmativeBased(voters);
//        ab.setAllowIfAllAbstainDecisions(true);
//
//        return ab;
//    }

}
