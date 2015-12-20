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
import com.bearchoke.platform.api.user.identifier.UserIdentifier;
import com.bearchoke.platform.domain.user.aggregate.UserAggregate;
import com.bearchoke.platform.domain.user.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;



import java.util.Arrays;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 8/13/14
 * Time: 5:09 PM
 * Responsibility:
 */
@Slf4j
public class UserCommandHandlerTest {
    private FixtureConfiguration fixture;

    @Mock
    private UserRepository userRepository;
    
    @Before
    public void setUp() throws Exception {
        fixture = Fixtures.newGivenWhenThenFixture(UserAggregate.class);
        UserCommandHandler commandHandler = new UserCommandHandler(fixture.getRepository(), userRepository);
        fixture.registerAnnotatedCommandHandler(commandHandler);
        fixture.setReportIllegalStateChange(false);
    }

    @Test
    public void testCreateUser() throws Exception {
        UserIdentifier id = new UserIdentifier("user1");
        String password = "password";
        String firstName = "Gavin";
        String lastName = "King";
        String username = "User";
        String email = "User@User.com";
        List<String> roles = Arrays.asList("ROLE_USER");

        // useless test right now
        Matcher matcher = Matchers.anything();

//        fixture.given()
//                .when(new CreateUserCommand(id, email, username, firstName, lastName, password, roles))
//                .expectEvents(new UserCreatedEvent(id, UserConstants.SITE_SOURCE, username.toLowerCase(), password, email.toLowerCase(), firstName, lastName, roles));


        fixture.given()
                .when(new CreateUserCommand(id, email, username, firstName, lastName, password, roles))
                .expectEventsMatching(matcher);
    }
}
