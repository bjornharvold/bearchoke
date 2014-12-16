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

package com.bearchoke.platform.api.user;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by Bjorn Harvold
 * Date: 8/13/14
 * Time: 5:07 PM
 * Responsibility:
 */
public class UserCreatedEvent {
    private UserIdentifier userId;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String[] roles;

    public UserCreatedEvent(UserIdentifier userId, String username, String password, String email, String firstName, String lastName, String[] roles) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }

    public UserIdentifier getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String[] getRoles() {
        return roles;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("userId", userId)
                .append("username", username)
                .append("password", password)
                .append("email", email)
                .append("firstName", firstName)
                .append("lastName", lastName)
                .append("roles", roles)
                .toString();
    }
}
