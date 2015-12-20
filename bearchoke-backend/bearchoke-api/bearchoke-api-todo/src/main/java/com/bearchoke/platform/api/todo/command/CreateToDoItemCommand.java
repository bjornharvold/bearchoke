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

package com.bearchoke.platform.api.todo.command;

import com.bearchoke.platform.api.todo.identifier.ToDoIdentifier;
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;
import org.axonframework.common.Assert;

import javax.validation.constraints.NotNull;

/**
 * Created by Bjorn Harvold
 * Date: 8/13/14
 * Time: 5:07 PM
 * Responsibility:
 */
public class CreateToDoItemCommand {

    @TargetAggregateIdentifier
    private final ToDoIdentifier todoId;

    @NotNull
    private final String description;

    @NotNull
    private final String username;

    public CreateToDoItemCommand(ToDoIdentifier todoId, String description, String username) {

        Assert.notNull(todoId, "Identifier cannot be null");
        Assert.notNull(description, "Description cannot be null");
        Assert.notNull(username, "username cannot be null");

        this.todoId = todoId;
        this.description = description;
        this.username = username;
    }

    public ToDoIdentifier getTodoId() {
        return todoId;
    }

    public String getDescription() {
        return description;
    }

    public String getUsername() {
        return username;
    }
}
