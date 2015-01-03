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


import com.bearchoke.platform.server.config.WebSecurityConfig;
import com.bearchoke.platform.server.web.ApplicationMediaType;
import com.bearchoke.platform.server.web.config.MockAppConfig;
import com.bearchoke.platform.server.web.config.WebMvcConfig;
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
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
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
                MockAppConfig.class,
                WebSecurityConfig.class,
                WebMvcConfig.class
        }
)
@TestExecutionListeners(listeners={ServletTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
public class GreetingControllerTest {

    private static final String NAME = "Bjorn";

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
    public void testVersionedGreeting() throws Exception {
        log.info("Testing GreetingController.testVersionedGreeting...");

        this.mockMvc.perform(get("/api/greeting").param("name", NAME).accept(MediaType.parseMediaType(ApplicationMediaType.APPLICATION_BEARCHOKE_V1_JSON_VALUE + ";charset=UTF8")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.parseMediaType(ApplicationMediaType.APPLICATION_BEARCHOKE_V1_JSON_VALUE + ";charset=UTF8")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("Hello, " + NAME + "!"));

        log.info("Testing GreetingController.testVersionedGreeting SUCCESSFUL");
    }

    @Test
    public void testSecuredGreeting() throws Exception {
        log.info("Testing GreetingController.testSecuredGreeting...");

        this.mockMvc.perform(get("/api/secured/greeting")
                .with(csrf())
                .with(regularUser())
                .accept(MediaType.parseMediaType(ApplicationMediaType.APPLICATION_BEARCHOKE_V1_JSON_VALUE + ";charset=UTF8")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.parseMediaType(ApplicationMediaType.APPLICATION_BEARCHOKE_V1_JSON_VALUE + ";charset=UTF8")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("Hello, " + NAME + ". You have the role of ROLE_USER!"))
                .andExpect(authenticated().withRoles("USER"))
        ;

        log.info("Testing GreetingController.testSecuredGreeting SUCCESSFUL");
    }

    @Test
    public void testUnauthenticated() throws Exception {
        log.info("Testing hitting a secured url while unauthenticated...");

        this.mockMvc.perform(get("/api/secured/greeting")
                .with(csrf()))
                .andExpect(unauthenticated())
        ;

        log.info("Testing hitting a secured url while unauthenticated SUCCESS");
    }

    private static RequestPostProcessor regularUser() {
        return user(NAME).password("password").roles("USER");
    }

    private static RequestPostProcessor adminUser() {
        return user("admin").password("password").roles("ADMIN");
    }
}

