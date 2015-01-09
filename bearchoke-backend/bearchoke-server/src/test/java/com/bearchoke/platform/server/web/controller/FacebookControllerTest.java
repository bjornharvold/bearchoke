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

package com.bearchoke.platform.server.web.controller;


import com.bearchoke.platform.api.user.UserIdentifier;
import com.bearchoke.platform.api.user.dto.FacebookUserDto;
import com.bearchoke.platform.server.config.WebSecurityConfig;
import com.bearchoke.platform.server.web.config.MockAppConfig;
import com.bearchoke.platform.server.web.config.WebMvcConfig;
import com.bearchoke.platform.user.UserAggregate;
import com.bearchoke.platform.user.UserCommandHandler;
import com.bearchoke.platform.user.document.Role;
import com.bearchoke.platform.user.document.User;
import com.bearchoke.platform.user.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.axonframework.commandhandling.callbacks.FutureCallback;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.axonframework.test.GivenWhenThenTestFixture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by Bjorn Harvold
 * <p>
 * Date: 7/28/14
 * <p>
 * Time: 3:26 PM
 * <p>
 * Responsibility:
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes =
        {
                MockAppConfig.class,
                WebSecurityConfig.class,
                WebMvcConfig.class
        }
)
@TestExecutionListeners(listeners = {ServletTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
public class FacebookControllerTest extends AbstractControllerTest {

    private FixtureConfiguration fixture;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private CommandBus commandBus;

    @Autowired
    private UserRepository userQueryRepository;

    private MockMvc mockMvc;

    @Before
    public void setup() {

        fixture = Fixtures.newGivenWhenThenFixture(UserAggregate.class);

        UserCommandHandler commandHandler = new UserCommandHandler(fixture.getRepository(), userQueryRepository);
        fixture.registerAnnotatedCommandHandler(commandHandler);

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .addFilters(springSecurityFilterChain)
                .build();
    }

    @Test
    public void testUpdateFacebookUser() throws Exception {
        log.info("Testing FacebookController.updateFacebookUser...");

        // yes fb email is username now. api doesn't give us user's username any longer
        final String emailAndUsername = "facebook@user.com";
        final String id = "1111111111111111";

        FutureCallback<UserIdentifier> callback = new FutureCallback<>();
        callback.onSuccess(new UserIdentifier(id));

        assertNotNull("Future result cannot be null", callback.get());

        // try to handle the asynchronous commandBus callback with Mockito
        doAnswer(invocationOnMock ->
        {
            ((FutureCallback<UserIdentifier>) invocationOnMock.getArguments()[1]).onSuccess(new UserIdentifier(id));
            return null;
        }).when(commandBus).dispatch(anyObject(), anyObject());

        FacebookUserDto dto = new FacebookUserDto();
        dto.setEmail(emailAndUsername);
        dto.setFirst_name("Facebook");
        dto.setGender("Male");
        dto.setId(id);
        dto.setLast_name("User");
        dto.setName("Facebook User");
        dto.setVerified(true);
        dto.setLocale("US");

        this.mockMvc.perform(post("/api/facebook")
                .content(convertObjectToJsonBytes(dto))
                .contentType(getBearchokeVersion1MediaType()))
                .andDo(print())
                .andExpect(status().isOk());

        log.info("Testing FacebookController.updateFacebookUser SUCCESSFUL");
    }

}

