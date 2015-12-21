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

package com.bearchoke.platform.server.frontend.web.controller;

import com.bearchoke.platform.server.common.ApplicationMediaType;
import com.bearchoke.platform.server.frontend.service.Greeting;
import com.bearchoke.platform.server.frontend.service.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Example of using versioning on REST endpoints
 * <p/>
 * Date: 1/5/14
 * <p/>
 * Time: 3:32 PM
 * <p/>
 * Responsibility:
 */

@RestController
public class GreetingController {

    private final GreetingService greetingService;

    @Autowired
    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    /**
     * Easy rest mapping
     * @param name
     * @return
     */
    @RequestMapping(value = "/api/greeting")
    @ResponseStatus(HttpStatus.OK)
    public Greeting greeting(@RequestParam(value = "name", required = false, defaultValue = "World") String name) {
        return greetingService.greet(name);
    }

    /**
     * Easy rest mapping that is secured so user has to be authenticated
     * @return
     */
    @RequestMapping(value = "/api/secured/greeting", headers = {"Accept="+ApplicationMediaType.APPLICATION_BEARCHOKE_V1_JSON_VALUE}, produces = ApplicationMediaType.APPLICATION_BEARCHOKE_V1_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Greeting securedGreeting() {
        return greetingService.securedGreeting(1.0f);
    }

    /**
     * Versioned rest mapping using Accept header as the versioned element
     * @param name
     * @return
     */
    @RequestMapping(value = "/api/greeting", headers = {"Accept="+ApplicationMediaType.APPLICATION_BEARCHOKE_V1_JSON_VALUE}, produces = ApplicationMediaType.APPLICATION_BEARCHOKE_V1_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Greeting greetingV1Accept(@RequestParam(value = "name", required = false, defaultValue = "World") String name) {
        return greetingService.greet(name, 1.0f);
    }

    /**
     * Versioned rest mapping using Accept header as the versioned element
     * @param name
     * @return
     */
    @RequestMapping(value = "/api/greeting", headers = {"Accept="+ApplicationMediaType.APPLICATION_BEARCHOKE_V2_JSON_VALUE}, produces = ApplicationMediaType.APPLICATION_BEARCHOKE_V2_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Greeting greetingV2Accept(@RequestParam(value = "name", required = false, defaultValue = "World") String name) {
        return greetingService.greet(name, 2.0f);
    }


    /**
     * Versioned rest mapping using content-type as the versioned element
     * @param name
     * @return
     */
//    @RequestMapping(value = "/api/greeting", produces = ApplicationMediaType.APPLICATION_BEARCHOKE_V1_JSON_VALUE)
//    @ResponseStatus(HttpStatus.OK)
//    public Greeting greetingV1ContentType(@RequestParam(value = "name", required = false, defaultValue = "World") String name) {
//        return greetingService.greet(name, 1.0f);
//    }

    /**
     * Versioned rest mapping using content-type as the versioned element
     * @param name
     * @return
     */
//    @RequestMapping(value = "/api/greeting", produces = ApplicationMediaType.APPLICATION_BEARCHOKE_V2_JSON_VALUE)
//    @ResponseStatus(HttpStatus.OK)
//    public Greeting greetingV2ContentType(@RequestParam(value = "name", required = false, defaultValue = "World") String name) {
//        return greetingService.greet(name, 2.0f);
//    }
}

