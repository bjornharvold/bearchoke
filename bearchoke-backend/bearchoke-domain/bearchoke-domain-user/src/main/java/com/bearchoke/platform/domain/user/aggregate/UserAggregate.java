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

package com.bearchoke.platform.domain.user.aggregate;

import com.bearchoke.platform.api.user.enums.Gender;
import com.bearchoke.platform.api.user.event.UserAuthenticatedEvent;
import com.bearchoke.platform.api.user.event.UserCreatedEvent;
import com.bearchoke.platform.api.user.identifier.UserIdentifier;
import lombok.extern.log4j.Log4j2;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 8/13/14
 * Time: 5:10 PM
 * Responsibility:
 */
@Component
@Log4j2
public class UserAggregate extends AbstractAnnotatedAggregateRoot {

    @AggregateIdentifier
    private UserIdentifier id;

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String profilePictureUrl;
    private Gender gender;
    private Integer source;
    private List<String> roles;

    public UserAggregate() {
    }

    public UserAggregate(UserIdentifier id, Integer source, String username, String password, String email, String firstName, String lastName, String profilePictureUrl, Gender gender, List<String> roles) {
        apply(new UserCreatedEvent(
                id,
                source,
                username.toLowerCase(),
                password,
                email.toLowerCase(),
                firstName,
                lastName,
                profilePictureUrl,
                gender,
                roles
        ));
    }

    @EventHandler
    public void onUserCreated(UserCreatedEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Caught: " + event.getClass().getSimpleName());
        }

        this.id = event.getUserId();
        this.source = event.getSource();
        this.username = event.getUsername();
        this.password = event.getPassword();
        this.email = event.getEmail();
        this.firstName = event.getFirstName();
        this.lastName = event.getLastName();
        this.profilePictureUrl = event.getProfilePictureUrl();
        this.gender = event.getGender();
        this.roles = event.getRoles();
    }

    public UserIdentifier getId() {
        return id;
    }
}
