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

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Controller;

/**
 * Created by Bjorn Harvold
 * Date: 1/3/14
 * Time: 4:13 PM
 * Responsibility:
 */
@Configuration
@EnableAsync
@ComponentScan(basePackages = "com.bearchoke.platform.server", excludeFilters = {
        @ComponentScan.Filter(value = Controller.class, type = FilterType.ANNOTATION),
        @ComponentScan.Filter(pattern = "com.bearchoke.platform.server.web.*", type = FilterType.REGEX),
        @ComponentScan.Filter(pattern = "com.bearchoke.platform.server.websocket.*", type = FilterType.REGEX),
        @ComponentScan.Filter(value = Configuration.class, type = FilterType.ANNOTATION)
})
public class AppConfig {

}
