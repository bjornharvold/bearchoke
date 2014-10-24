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

import com.bearchoke.platform.api.todo.ToDoIdentifier;
import com.bearchoke.platform.api.todo.ToDoItemCompletedEvent;
import com.bearchoke.platform.api.todo.ToDoItemCreatedEvent;
import com.bearchoke.platform.api.todo.ToDoItemRemovedEvent;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.springframework.stereotype.Component;

/**
 * Created by Bjorn Harvold
 * Date: 8/13/14
 * Time: 5:10 PM
 * Responsibility:
 */
@Component
public class ToDoItem extends AbstractAnnotatedAggregateRoot {
    @AggregateIdentifier
    private ToDoIdentifier id;
    private String description;
    private Boolean completed = false;

    public ToDoItem() {
    }

    public ToDoItem(ToDoIdentifier id, String description, String username) {
        apply(new ToDoItemCreatedEvent(id.toString(), description, username));
    }

    public void markAsCompleted(ToDoIdentifier todoId, String username) {
        apply(new ToDoItemCompletedEvent(todoId.toString(), username));
    }

    public void remove(ToDoIdentifier todoId, String username) {
        apply(new ToDoItemRemovedEvent(todoId.toString(), username));
    }

    @EventHandler
    public void onToDoItemCreated(ToDoItemCreatedEvent event) {
        this.id = new ToDoIdentifier(event.getTodoId());
        this.description = event.getDescription();
    }

    @EventHandler
    public void onMarkCompleted(ToDoItemCompletedEvent event) {
        this.completed = true;
    }

    @EventHandler
    public void onToDoItemRemoved(ToDoItemRemovedEvent event) {
        this.markDeleted();
    }
}
