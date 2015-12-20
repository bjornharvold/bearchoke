/*
 * Copyright (c) 2015. Traveliko
 */

package com.bearchoke.platform.persistence.mongo.axon;

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
