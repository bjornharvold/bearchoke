/*
 * Copyright 2015 the original author or authors.
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

package com.bearchoke.platform.base.axon;

import com.mongodb.DBCollection;
import org.axonframework.eventstore.mongo.MongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;

/**
 * Created by Bjorn Harvold
 * Date: 1/4/15
 * Time: 11:32 AM
 * Responsibility:
 */
public class AxonMongoTemplate implements MongoTemplate {
    private static final String DEFAULT_DOMAINEVENTS_COLLECTION = "domainevents";
    private static final String DEFAULT_SNAPSHOTEVENTS_COLLECTION = "snapshotevents";

    private final String domainEventsCollectionName;
    private final String snapshotEventsCollectionName;

    private final MongoDbFactory mongoDbFactory;
    private final org.springframework.data.mongodb.core.MongoTemplate mongoTemplate;

    @Autowired
    public AxonMongoTemplate(MongoDbFactory mongoDbFactory) {
        this.mongoDbFactory = mongoDbFactory;
        this.mongoTemplate = new org.springframework.data.mongodb.core.MongoTemplate(mongoDbFactory);

        domainEventsCollectionName = DEFAULT_DOMAINEVENTS_COLLECTION;
        snapshotEventsCollectionName = DEFAULT_SNAPSHOTEVENTS_COLLECTION;
    }

    @Override
    public DBCollection domainEventCollection() {
        return mongoTemplate.getCollection(domainEventsCollectionName);
    }

    @Override
    public DBCollection snapshotEventCollection() {
        return mongoTemplate.getCollection(snapshotEventsCollectionName);
    }
}
