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

package com.bearchoke.platform.api.todo.event;

import com.bearchoke.platform.api.todo.AbstractAuthenticatedEvent;
import com.bearchoke.platform.api.todo.enums.ToDoAction;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by Bjorn Harvold
 * Date: 8/13/14
 * Time: 5:08 PM
 * Responsibility:
 */
public class ToDoItemRemovedEvent extends AbstractAuthenticatedEvent {
    private ToDoAction action = ToDoAction.DELETE;
    private final String todoId;

    public ToDoItemRemovedEvent(String todoId, String username) {
        super(username);
        this.todoId = todoId;
    }

    public String getTodoId() {
        return todoId;
    }

    public ToDoAction getAction() {
        return action;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("action", action)
                .append("todoId", todoId)
                .toString();
    }
}
