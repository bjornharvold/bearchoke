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

package com.bearchoke.platform.server.frontend.web.controller;

import com.bearchoke.platform.server.frontend.web.ApplicationMediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import javax.inject.Inject;
import java.io.IOException;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

/**
 * Created by Bjorn Harvold
 * Date: 1/4/15
 * Time: 12:13 PM
 * Responsibility:
 */
public abstract class AbstractControllerTest {
    protected static final String USER = "user";
    protected static final String ADMIN = "admin";

    @Inject
    protected ObjectMapper mapper;

    protected static RequestPostProcessor regularUser() {
        return user(USER).password("password").roles("USER");
    }

    protected static RequestPostProcessor adminUser() {
        return user(ADMIN).password("password").roles("ADMIN");
    }

    protected MediaType getBearchokeVersion1MediaType() {
        return MediaType.parseMediaType(ApplicationMediaType.APPLICATION_BEARCHOKE_V1_JSON_VALUE + ";charset=UTF8");
    }

    protected MediaType getBearchokeVersion2MediaType() {
        return MediaType.parseMediaType(ApplicationMediaType.APPLICATION_BEARCHOKE_V2_JSON_VALUE + ";charset=UTF8");
    }

    protected byte[] convertObjectToJsonBytes(Object object) throws IOException {
        return mapper.writeValueAsBytes(object);
    }

    protected String createStringWithLength(int length) {
        StringBuilder builder = new StringBuilder();

        for (int index = 0; index < length; index++) {
            builder.append("a");
        }

        return builder.toString();
    }
}
