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

package com.bearchoke.platform.jpa.security.spring;

import com.bearchoke.platform.jpa.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by Bjorn Harvold
 * Date: 1/14/14
 * Time: 9:42 PM
 * Responsibility:
 */
public interface JpaUserDetailsService extends UserDetailsService {
    User registerUser(User user);
    User findUserByEmail(String email);
}
