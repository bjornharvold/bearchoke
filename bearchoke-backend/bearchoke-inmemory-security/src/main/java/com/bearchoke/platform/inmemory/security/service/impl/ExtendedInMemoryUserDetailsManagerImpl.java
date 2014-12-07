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

package com.bearchoke.platform.inmemory.security.service.impl;

import com.bearchoke.platform.api.user.ExtendedUserDetailsManager;
import com.bearchoke.platform.inmemory.security.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Bjorn Harvold
 * Date: 12/6/14
 * Time: 3:31 PM
 * Responsibility:
 */
public class ExtendedInMemoryUserDetailsManagerImpl extends InMemoryUserDetailsManager implements ExtendedUserDetailsManager {
    private final Map<String, String> emails = new HashMap<>();

    public ExtendedInMemoryUserDetailsManagerImpl(Collection<UserDetails> users) {
        super(users);
        for (UserDetails user : users) {
            String email = ((User)user).getEmail();
            emails.put(email, email);
        }
    }

    public ExtendedInMemoryUserDetailsManagerImpl(Properties users) {
        super(users);
    }

    public boolean emailExists(String email) {
        return emails.containsKey(email);
    }
}
