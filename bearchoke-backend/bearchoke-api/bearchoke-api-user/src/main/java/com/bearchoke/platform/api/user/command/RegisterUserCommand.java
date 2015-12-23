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

import com.bearchoke.platform.api.user.enums.Gender;
import com.bearchoke.platform.api.user.identifier.UserIdentifier;
import com.bearchoke.platform.api.user.dto.RegisterUserDto;
import lombok.Getter;
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;
import org.axonframework.common.Assert;

import javax.validation.constraints.NotNull;

/**
 * Created by Bjorn Harvold
 * Date: 8/13/14
 * Time: 5:07 PM
 * Responsibility:
 */
public class RegisterUserCommand {

    @TargetAggregateIdentifier
    @Getter
    private final UserIdentifier userId;

    @NotNull
    @Getter
    private final String username;

    @NotNull
    @Getter
    private final String email;

    @NotNull
    @Getter
    private final String firstName;

    @NotNull
    @Getter
    private final String lastName;

    @NotNull
    @Getter
    private final String password;

    @Getter
    private final String profilePictureUrl;

    @Getter
    private final Gender gender;

    public RegisterUserCommand(UserIdentifier userId, RegisterUserDto user) {

        Assert.notNull(user, "Dto cannot be null");
        Assert.notNull(userId, "Identifier cannot be null");
        Assert.notNull(user.getEmail(), "Email cannot be null");
        Assert.notNull(user.getUsername(), "Username cannot be null");
        Assert.notNull(user.getFirstName(), "First name cannot be null");
        Assert.notNull(user.getLastName(), "Last name cannot be null");
        Assert.notNull(user.getPassword(), "Password cannot be null");

        this.userId = userId;
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.password = user.getPassword();
        this.profilePictureUrl = user.getProfilePictureUrl();
        this.gender = user.getGender();
    }
}
