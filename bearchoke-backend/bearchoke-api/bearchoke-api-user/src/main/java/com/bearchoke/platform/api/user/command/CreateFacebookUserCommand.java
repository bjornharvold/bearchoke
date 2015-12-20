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

package com.bearchoke.platform.api.user.command;

import com.bearchoke.platform.api.user.identifier.UserIdentifier;
import com.bearchoke.platform.api.user.dto.FacebookUserDto;
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;
import org.axonframework.common.Assert;

import javax.validation.constraints.NotNull;

/**
 * Created by Bjorn Harvold
 * Date: 8/13/14
 * Time: 5:07 PM
 * Responsibility: This is to keep the facebook user and our user in sync.
 */
public class CreateFacebookUserCommand {

    @TargetAggregateIdentifier
    private final UserIdentifier userId;

    @NotNull
    private final String email;

    @NotNull
    private final String firstName;

    @NotNull
    private final String lastName;

    @NotNull
    private final String password;

    public CreateFacebookUserCommand(UserIdentifier userId, FacebookUserDto dto) {

        Assert.notNull(userId, "Identifier cannot be null");
        Assert.notNull(dto.getEmail(), "Email cannot be null");
        Assert.notNull(dto.getFirst_name(), "First name cannot be null");
        Assert.notNull(dto.getLast_name(), "Last name cannot be null");
        Assert.notNull(dto.getPassword(), "Password cannot be null");

        this.userId = userId;
        this.email = dto.getEmail();
        this.firstName = dto.getFirst_name();
        this.lastName = dto.getLast_name();
        this.password = dto.getPassword();
    }

    public UserIdentifier getUserId() {
        return userId;
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
}
