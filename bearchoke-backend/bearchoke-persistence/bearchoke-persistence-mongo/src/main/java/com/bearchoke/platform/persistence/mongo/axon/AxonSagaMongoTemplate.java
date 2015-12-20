/*
 * Copyright (c) 2015. Traveliko
 */

package com.bearchoke.platform.persistence.mongo.axon;

import com.mongodb.DBCollection;
import org.axonframework.saga.repository.mongo.MongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;

/**
 * Created by Bjorn Harvold
 * Date: 1/4/15
 * Time: 11:32 AM
 * Responsibility:
 */
public class AxonSagaMongoTemplate implements MongoTemplate {
    private static final String DEFAULT_SAGAS_COLLECTION_NAME = "sagas";
    private final String sagasCollectionName;

    private final MongoDbFactory mongoDbFactory;
    private final org.springframework.data.mongodb.core.MongoTemplate mongoTemplate;

    @Autowired
    public AxonSagaMongoTemplate(MongoDbFactory mongoDbFactory) {
        this.mongoDbFactory = mongoDbFactory;
        this.mongoTemplate = new org.springframework.data.mongodb.core.MongoTemplate(mongoDbFactory);

        this.sagasCollectionName = DEFAULT_SAGAS_COLLECTION_NAME;
    }

    @Override
    public DBCollection sagaCollection() {
        return mongoTemplate.getCollection(sagasCollectionName);
    }
}
