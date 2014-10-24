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

package com.bearchoke.platform.server.jackson;

import com.bearchoke.platform.api.todo.ToDoIdentifier;
import com.bearchoke.platform.api.todo.jackson.ToDoIdentifierDeserializer;
import com.bearchoke.platform.api.todo.jackson.ToDoIdentifierSerializer;
import com.bearchoke.platform.mongo.utils.jackson.ObjectIdDeserializer;
import com.bearchoke.platform.mongo.utils.jackson.ObjectIdSerializer;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

/**
 * Created by Bjorn Harvold
 * Date: 8/13/14
 * Time: 3:21 PM
 * Responsibility:
 */
@Component
public class CustomObjectMapper extends ObjectMapper {
    public CustomObjectMapper() {
        // SerializationFeature for changing how JSON is written

        // to enable standard indentation ("pretty-printing"):
        enable(SerializationFeature.INDENT_OUTPUT);
        // to allow serialization of "empty" POJOs (no properties to serialize)
        // (without this setting, an exception is thrown in those cases)
        disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // to write java.util.Date, Calendar as number (timestamp):
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Deserialization Feature for changing how JSON is read as POJOs:
        // to prevent exception when encountering unknown property:
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // to allow coercion of JSON empty String ("") to null Object value:
        enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        // JsonParser.Feature for configuring parsing settings:

        // to allow C/C++ style comments in JSON (non-standard, disabled by default)
//        enable(JsonParser.Feature.ALLOW_COMMENTS);
        // to allow (non-standard) unquoted field names in JSON:
//        enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        // to allow use of apostrophes (single quotes), non standard
//        enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);

// JsonGenerator.Feature for configuring low-level JSON generation:

// to force escaping of non-ASCII characters:
//        enable(JsonGenerator.Feature.ESCAPE_NON_ASCII);

        SimpleModule objectIdModule = new SimpleModule("ObjectIdModule", new Version(1, 0, 0, null, "com.mongo", "objectID"));
        objectIdModule.addDeserializer(ObjectId.class, new ObjectIdDeserializer());
        objectIdModule.addSerializer(ObjectId.class, new ObjectIdSerializer());

        registerModule(objectIdModule);

        registerModule(new JodaModule());

        SimpleModule todoIdentifierModule = new SimpleModule("ToDoIdentifierModule", new Version(1, 0, 0, null, "com.bearchoke", "ToDoIdentifier"));
        todoIdentifierModule.addSerializer(ToDoIdentifier.class, new ToDoIdentifierSerializer());
        todoIdentifierModule.addDeserializer(ToDoIdentifier.class, new ToDoIdentifierDeserializer());

        registerModule(todoIdentifierModule);
    }
}
