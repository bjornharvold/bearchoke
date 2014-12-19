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


import com.bearchoke.platform.platform.base.config.CQRSConfig;
import com.bearchoke.platform.platform.base.config.CacheConfig;
import com.bearchoke.platform.platform.base.config.EncryptionConfig;
import com.bearchoke.platform.platform.base.config.RabbitMQLocalConfig;
import com.bearchoke.platform.platform.base.config.RedisConfig;
import com.bearchoke.platform.platform.base.config.RedisLocalConfig;
import com.bearchoke.platform.server.config.AppConfig;
import com.bearchoke.platform.server.config.TestRESTAppConfig;
import com.bearchoke.platform.server.web.config.WebMvcConfig;
import com.bearchoke.platform.server.config.WebSecurityConfig;
import com.bearchoke.platform.server.web.ApplicationMediaType;
import com.bearchoke.platform.user.config.UserConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by Bjorn Harvold
 * <p/>
 * Date: 7/28/14
 * <p/>
 * Time: 3:26 PM
 * <p/>
 * Responsibility:
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes =
        {
                TestRESTAppConfig.class,
                WebMvcConfig.class
        }
)
@TestExecutionListeners(listeners={ServletTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
public class UserControllerTest {

    private static final String UNIQUE_EMAIL = "harry1@mitchell.com";
    private static final String EXISTING_EMAIL = "harry@mitchell.com";

    private static final String UNIQUE_USERNAME = "harry1mitchell";
    private static final String EXISTING_USERNAME = "harrymitchell";

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private Filter springSecurityFilterChain;

    private MockMvc mockMvc;

    @Before
    public void setup() {

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .addFilters(springSecurityFilterChain)
                .build();

    }

    @Test
    public void testIsEmailUnique() throws Exception {
        log.info("Testing UserController.isEmailUnique...");

        log.info("First we expect a unique email");
        this.mockMvc.perform(get("/api/user/uniqueemail").param("email", UNIQUE_EMAIL).accept(getMediaType()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(getMediaType()))
                .andExpect(jsonPath("$.unique").value(true));

        log.info("And now we expect email to already exist");
        this.mockMvc.perform(get("/api/user/uniqueemail").param("email", EXISTING_EMAIL).accept(getMediaType()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(getMediaType()))
                .andExpect(jsonPath("$.unique").value(false));

        log.info("Testing UserController.isEmailUnique SUCCESSFUL");
    }

    @Test
    public void testIsUsernameUnique() throws Exception {
        log.info("Testing UserController.isUsernameUnique...");

        log.info("First we expect a unique username");
        this.mockMvc.perform(get("/api/user/uniqueusername").param("username", UNIQUE_USERNAME).accept(getMediaType()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(getMediaType()))
                .andExpect(jsonPath("$.unique").value(true));

        log.info("And now we expect username to already exist");
        this.mockMvc.perform(get("/api/user/uniqueusername").param("username", EXISTING_USERNAME).accept(getMediaType()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(getMediaType()))
                .andExpect(jsonPath("$.unique").value(false));

        log.info("Testing UserController.isUsernameUnique SUCCESSFUL");
    }

    private MediaType getMediaType() {
        return MediaType.parseMediaType(ApplicationMediaType.APPLICATION_BEARCHOKE_V1_JSON_VALUE + ";charset=UTF8");
    }
}

