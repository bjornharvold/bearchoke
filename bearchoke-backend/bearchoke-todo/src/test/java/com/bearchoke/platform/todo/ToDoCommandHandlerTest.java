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

package com.bearchoke.platform.todo;

import com.bearchoke.platform.api.todo.CreateToDoItemCommand;
import com.bearchoke.platform.api.todo.MarkToDoItemAsCompleteCommand;
import com.bearchoke.platform.api.todo.ToDoIdentifier;
import com.bearchoke.platform.api.todo.ToDoItemCompletedEvent;
import com.bearchoke.platform.api.todo.ToDoItemCreatedEvent;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Bjorn Harvold
 * Date: 8/13/14
 * Time: 5:09 PM
 * Responsibility:
 */
public class ToDoCommandHandlerTest {
    private FixtureConfiguration fixture;

    @Before
    public void setUp() throws Exception {
        fixture = Fixtures.newGivenWhenThenFixture(ToDoItemAggregate.class);
        ToDoCommandHandler commandHandler = new ToDoCommandHandler(fixture.getRepository());
        fixture.registerAnnotatedCommandHandler(commandHandler);
    }

    @Test
    public void testCreateToDoItem() throws Exception {
        ToDoIdentifier id = new ToDoIdentifier("todo1");
        fixture.given()
                .when(new CreateToDoItemCommand(id, "need to implement the aggregate", "user"))
                .expectEvents(new ToDoItemCreatedEvent(id.toString(), "need to implement the aggregate", "user"));
    }

    @Test
    public void testMarkToDoItemAsCompleted() throws Exception {
        ToDoIdentifier id = new ToDoIdentifier("todo1");
        fixture.given(new ToDoItemCreatedEvent(id.toString(), "need to implement the aggregate", "user"))
                .when(new MarkToDoItemAsCompleteCommand(id, "user"))
                .expectEvents(new ToDoItemCompletedEvent(id.toString(), "user"));
    }
}
