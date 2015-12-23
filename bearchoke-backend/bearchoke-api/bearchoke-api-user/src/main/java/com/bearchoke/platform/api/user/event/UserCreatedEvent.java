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

package com.bearchoke.platform.api.user.event;

import com.bearchoke.platform.api.user.enums.Gender;
import com.bearchoke.platform.api.user.identifier.UserIdentifier;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 8/13/14
 * Time: 5:07 PM
 * Responsibility:
 */
public class UserCreatedEvent {
    @Getter
    private final UserIdentifier userId;

    @Getter
    private final String username;

    @Getter
    private final String password;

    @Getter
    private final String email;

    @Getter
    private final String firstName;

    @Getter
    private final String lastName;

    @Getter
    private final String profilePictureUrl;

    @Getter
    private final Gender gender;

    @Getter
    private Integer source;

    @Getter
    private List<String> roles;

    public UserCreatedEvent(UserIdentifier userId, Integer source, String username, String password, String email, String firstName, String lastName, String profilePictureUrl, Gender gender, List<String> roles) {
        this.userId = userId;
        this.source = source;
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
        this.profilePictureUrl = profilePictureUrl;
        this.gender = gender;
    }
}
