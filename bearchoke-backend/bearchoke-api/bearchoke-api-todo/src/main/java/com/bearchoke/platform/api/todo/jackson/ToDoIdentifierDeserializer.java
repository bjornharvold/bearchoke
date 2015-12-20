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

package com.bearchoke.platform.api.todo.jackson;

import com.bearchoke.platform.api.todo.identifier.ToDoIdentifier;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

/**
 * Created by Bjorn Harvold
 * Date: 8/10/11
 * Time: 2:30 PM
 * Responsibility:
 */
public class ToDoIdentifierDeserializer extends StdDeserializer<ToDoIdentifier> {

    public ToDoIdentifierDeserializer() {
        super(ToDoIdentifier.class);
    }

    @Override
    public ToDoIdentifier deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return new ToDoIdentifier(jsonParser.getText());
    }
}
