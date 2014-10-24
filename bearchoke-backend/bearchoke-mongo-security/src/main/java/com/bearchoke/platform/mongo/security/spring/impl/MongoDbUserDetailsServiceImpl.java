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

package com.bearchoke.platform.mongo.security.spring.impl;

import com.bearchoke.platform.mongo.document.User;
import com.bearchoke.platform.mongo.repository.MongoDbUserRepository;
import com.bearchoke.platform.mongo.security.spring.MongoDbUserDetailsService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.util.password.PasswordEncryptor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by Bjorn Harvold
 * Date: 1/14/14
 * Time: 9:42 PM
 * Responsibility:
 */
public class MongoDbUserDetailsServiceImpl implements MongoDbUserDetailsService {

    private final MongoDbUserRepository mongoDbUserRepository;
    private final PasswordEncryptor passwordEncryptor;

    public MongoDbUserDetailsServiceImpl(MongoDbUserRepository mongoDbUserRepository, PasswordEncryptor passwordEncryptor) {
        this.mongoDbUserRepository = mongoDbUserRepository;
        this.passwordEncryptor = passwordEncryptor;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return mongoDbUserRepository.findUserByUsername(username);
    }

    /**
     * This is the single point at which all Registration Paths MUST come together.
     *
     * @param user user
     * @return Return value
     */
    @Override
    public User registerUser(User user) {
        String rawPassword = user.getPsswrd();

        if (StringUtils.isBlank(user.getMl())) {
            throw new IllegalStateException("User is missing an email");
        }

        if (StringUtils.isBlank(rawPassword)) {
            throw new IllegalStateException("User: " + user.getMl() + " is missing a password");
        }

        if (StringUtils.isBlank(user.getPsswrd())) {
            user.setPsswrd(RandomStringUtils.randomAlphabetic(8));
        }

        // Lowercase email
        user.setMl(user.getMl().toLowerCase());
        String email = user.getMl();

        // If there is already a user with this email, throw an exception
        User existingUser = mongoDbUserRepository.findUserByEmail(email);
        if (existingUser != null) {
            throw new IllegalStateException("User already exists for email: " + email);
        }

        // encrypt password
        user.setPsswrd(passwordEncryptor.encryptPassword(rawPassword));

        // persist user
        mongoDbUserRepository.save(user);

        return user;
    }

    @Override
    public User findUserByEmail(String email) {
        return mongoDbUserRepository.findUserByEmail(email);
    }
}
