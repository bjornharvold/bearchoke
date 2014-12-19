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

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;
import org.axonframework.common.Assert;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 8/13/14
 * Time: 5:07 PM
 * Responsibility: The difference between this and RegisterUserCommand is that we expect the system to use this command when
 * creating users. That will avoid emails being sent out and other unneccessary tasks being completed. RegisterUserCommand
 * should only be issued by the user when registering with our site.
 */
public class CreateUserCommand {

    @TargetAggregateIdentifier
    private final UserIdentifier userId;

    @NotNull
    private final String username;

    @NotNull
    private final String email;

    @NotNull
    private final String firstName;

    @NotNull
    private final String lastName;

    @NotNull
    private final String password;

    @NotNull
    private final List<RoleIdentifier> roles;

    public CreateUserCommand(UserIdentifier userId, String email, String username, String firstName, String lastName, String password, List<RoleIdentifier> roles) {

        Assert.notNull(userId, "Identifier cannot be null");
        Assert.notNull(email, "Email cannot be null");
        Assert.notNull(username, "Username cannot be null");
        Assert.notNull(firstName, "First name cannot be null");
        Assert.notNull(lastName, "Last name cannot be null");
        Assert.notNull(password, "Password cannot be null");
        Assert.notNull(roles, "Roles cannot be null");

        this.userId = userId;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.roles = roles;
    }

    public UserIdentifier getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
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

    public String getPassword() {
        return password;
    }

    public List<RoleIdentifier> getRoles() {
        return roles;
    }
}
