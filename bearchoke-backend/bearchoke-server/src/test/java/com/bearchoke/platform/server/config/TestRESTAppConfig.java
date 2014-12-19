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

import com.bearchoke.platform.server.web.config.WebMvcConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Created by Bjorn Harvold
 * Date: 1/3/14
 * Time: 4:13 PM
 * Responsibility:
 */
@Configuration
@EnableAsync
@ComponentScan(basePackages = {"com.bearchoke.platform"}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = WebMvcConfig.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = WebSocketConfig.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = WebSocketSecurityConfig.class)
})
public class TestRESTAppConfig {

}
