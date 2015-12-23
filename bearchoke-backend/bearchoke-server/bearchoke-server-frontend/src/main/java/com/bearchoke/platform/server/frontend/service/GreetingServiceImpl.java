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

package com.bearchoke.platform.server.frontend.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Created by Bjorn Harvold
 * Date: 8/12/14
 * Time: 6:13 PM
 * Responsibility:
 */
@Log4j2
@Service
public class GreetingServiceImpl implements GreetingService {
    private static final String template = "Hello, %s!";

    @Override
    public Greeting greet(String name) {
        return createGreeting(name, 0.0f);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @Override
    public Greeting securedGreeting(Float version) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return createGreeting(authentication.getName() + ". You have the role of ROLE_USER", version);
    }

    @Override
    public Greeting greet(String name, Float version) {
        return createGreeting(name, version);
    }

    private Greeting createGreeting(String name, Float version) {
        Greeting greeting = new Greeting();
        greeting.setId(1l);
        greeting.setContent(String.format(template, name));
        greeting.setVersion(version);

        log.info(greeting.toString());

        return greeting;
    }
}
