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
import com.bearchoke.platform.platform.base.config.AxonCQRSConfig;
import com.bearchoke.platform.todo.config.ToDoConfig;
import com.bearchoke.platform.platform.base.config.RabbitMQLocalConfig;
import com.bearchoke.platform.platform.base.config.SchedulerConfig;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by Bjorn Harvold
 * Date: 8/13/14
 * Time: 5:13 PM
 * Responsibility:
 */
public class ToDoItemRunner {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
                SchedulerConfig.class,
                RabbitMQLocalConfig.class,
                AxonCQRSConfig.class,
                ToDoConfig.class
        );

        try {
            // let's start with the Command Bus
//        CommandBus commandBus = new SimpleCommandBus();
            CommandBus commandBus = ctx.getBean(CommandBus.class);

            // the CommandGateway provides a friendlier API
//        CommandGateway commandGateway = new DefaultCommandGateway(commandBus);
            CommandGateway commandGateway = ctx.getBean(CommandGateway.class);

            // we'll store Events on the FileSystem, in the "events/" folder
//        EventStore eventStore = new FileSystemEventStore(new SimpleEventFileResolver(new File("./events")));
//        EventStore eventStore = ctx.getBean(EventStore.class);

            // a Simple Event Bus will do
//        EventBus eventBus = new SimpleEventBus();
            EventBus eventBus = ctx.getBean(EventBus.class);

            // we need to configure the repository
//        EventSourcingRepository repository = new EventSourcingRepository(ToDoItem.class, eventStore);
//        repository.setEventBus(eventBus);
            EventSourcingRepository repository = ctx.getBean("toDoItemRepository", EventSourcingRepository.class);

            // Axon needs to know that our ToDoItem Aggregate can handle commands
//            AggregateAnnotationCommandHandler.subscribe(ToDoItem.class, repository, commandBus);
//            AnnotationEventListenerAdapter.subscribe(new ToDoEventHandler(), eventBus);

            // and let's send some Commands on the CommandBus.
            final ToDoIdentifier id = new ToDoIdentifier();
            commandGateway.send(new CreateToDoItemCommand(id, "Need to do this", "user"));
            commandGateway.send(new MarkToDoItemAsCompleteCommand(id, "user"));
        } finally {
            ctx.destroy();
        }

    }
}
