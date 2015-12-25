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

package com.bearchoke.platform.domain.user.handler;

import com.bearchoke.platform.api.user.command.CreateUserCommand;
import com.bearchoke.platform.api.user.enums.Gender;
import com.bearchoke.platform.api.user.event.UserCreatedEvent;
import com.bearchoke.platform.api.user.identifier.UserIdentifier;
import com.bearchoke.platform.domain.user.UserConstants;
import com.bearchoke.platform.domain.user.aggregate.UserAggregate;
import com.bearchoke.platform.domain.user.repositories.UserRepository;
import com.bearchoke.platform.domain.user.test.TestPasswordEncryptor;
import lombok.extern.log4j.Log4j2;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.jasypt.util.password.PasswordEncryptor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;



import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * Created by Bjorn Harvold
 * Date: 8/13/14
 * Time: 5:09 PM
 * Responsibility:
 */
@Log4j2
public class UserCommandHandlerTest {
    private FixtureConfiguration fixture;

    @Before
    public void setUp() throws Exception {
        // set up the encryptor here
        PasswordEncryptor passwordEncryptor = new TestPasswordEncryptor();

        fixture = Fixtures.newGivenWhenThenFixture(UserAggregate.class);
        UserCommandHandler commandHandler = new UserCommandHandler(fixture.getRepository(), mock(UserRepository.class), passwordEncryptor);
        fixture.registerAnnotatedCommandHandler(commandHandler);
        fixture.setReportIllegalStateChange(false);
    }

    @Test
    public void testSaveOrUpdateUserCommand() throws Exception {
        UserIdentifier id = new UserIdentifier("user1");

        String firstName = "Gavin";
        String lastName = "King";
        String username = "gaving.king@gmail.comr";
        String password = "password";
        String profilePictureUrl = "http://lorempixel.com/50/50/people";
        Gender gender = Gender.Male;
        List<String> roles = Collections.singletonList("ROLE_USER");

        fixture.given()
                .when(new CreateUserCommand(
                        id,
                        username,
                        username,
                        firstName,
                        lastName,
                        profilePictureUrl,
                        gender,
                        password,
                        roles))
                .expectEvents(new UserCreatedEvent(
                        id,
                        UserConstants.SITE_SOURCE,
                        username.toLowerCase(),
                        password,
                        username.toLowerCase(),
                        firstName,
                        lastName,
                        profilePictureUrl,
                        gender,
                        roles));

    }
}
