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
import com.bearchoke.platform.api.user.dto.FacebookUserDto;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
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
    @Getter
    private final UserIdentifier userId;

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
    @NotNull
    private final Gender gender;

    public CreateFacebookUserCommand(UserIdentifier userId, FacebookUserDto dto) {

        Assert.notNull(userId, "Identifier cannot be null");
        Assert.notNull(dto.getEmail(), "Email cannot be null");
        Assert.notNull(dto.getFirst_name(), "First name cannot be null");
        Assert.notNull(dto.getLast_name(), "Last name cannot be null");
        Assert.notNull(dto.getPassword(), "Password cannot be null");
        Assert.notNull(dto.getGender(), "Gender cannot be null");

        this.userId = userId;
        this.email = dto.getEmail();
        this.firstName = dto.getFirst_name();
        this.lastName = dto.getLast_name();
        this.profilePictureUrl = dto.getPicture().getData().getUrl();
        this.gender = Gender.valueOf(StringUtils.capitalize(dto.getGender()));
        this.password = dto.getPassword();
    }

    public static void main(String[] args) {
        String gender = "male";
        String genderCapitalized = StringUtils.capitalize(gender);

        System.out.println(genderCapitalized);

        Gender genderEnum = Gender.valueOf(genderCapitalized);

        System.out.println(genderEnum);
    }
}
