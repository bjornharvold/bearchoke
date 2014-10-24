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

package com.bearchoke.platform.server.service;

/**
 * Created by Bjorn Harvold
 * Date: 8/12/14
 * Time: 6:14 PM
 * Responsibility:
 */
public interface GreetingService {
    Greeting greet(String name);
    Greeting securedGreeting(Float version);
    Greeting greet(String name, Float version);
}
