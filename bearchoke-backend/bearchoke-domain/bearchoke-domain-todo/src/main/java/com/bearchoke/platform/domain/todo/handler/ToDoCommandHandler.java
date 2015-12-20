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

package com.bearchoke.platform.domain.todo.handler;

import com.bearchoke.platform.api.todo.command.CreateToDoItemCommand;
import com.bearchoke.platform.api.todo.command.DeleteToDoItemCommand;
import com.bearchoke.platform.api.todo.command.MarkToDoItemAsCompleteCommand;
import com.bearchoke.platform.api.todo.identifier.ToDoIdentifier;
import com.bearchoke.platform.domain.todo.aggregate.ToDoItemAggregate;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by Bjorn Harvold
 * Date: 10/11/14
 * Time: 5:49 PM
 * Responsibility:
 */
@Component
public class ToDoCommandHandler {

    @Qualifier("toDoItemRepository")
    private final Repository<ToDoItemAggregate> repository;

    @Autowired
    public ToDoCommandHandler(Repository<ToDoItemAggregate> repository) {
        this.repository = repository;
    }

    @CommandHandler
    public ToDoIdentifier handleCreateToDoItem(CreateToDoItemCommand command) {
        ToDoIdentifier id = command.getTodoId();
        ToDoItemAggregate tdi = new ToDoItemAggregate(id, command.getDescription(), command.getUsername());
        repository.add(tdi);

        return id;
    }

    @CommandHandler
    public void markCompleted(MarkToDoItemAsCompleteCommand command) {
        ToDoItemAggregate tdi = onToDoItem(command.getTodoId());

        tdi.markAsCompleted(command.getTodoId(), command.getUsername());
    }

    @CommandHandler
    public void handleDeleteToDoItem(DeleteToDoItemCommand command) {
        ToDoItemAggregate tdi = onToDoItem(command.getTodoId());

        tdi.remove(command.getTodoId(), command.getUsername());
    }

    private ToDoItemAggregate onToDoItem(ToDoIdentifier toDoIdentifier) {
        return repository.load(toDoIdentifier);
    }
}
