/*
 * Copyright (c) 2015. Traveliko
 */

package com.bearchoke.platform.domain.user.init;

import com.bearchoke.platform.base.init.DBInit;
import com.bearchoke.platform.persistence.mongo.axon.AxonMongoTemplate;
import com.bearchoke.platform.persistence.mongo.axon.AxonSagaMongoTemplate;
import lombok.extern.log4j.Log4j2;
import org.axonframework.eventstore.mongo.MongoEventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 *
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Component
@Log4j2
@Order(1)
public class AxonDBInit implements DBInit {

    private static final String DOMAINEVENTS = "domainevents";
    private static final String SNAPSHOTEVENTS = "snapshotevents";
    private final AxonMongoTemplate axonMongoTemplate;
    private final MongoEventStore eventStore;
    private final MongoTemplate mongoTemplate;
    private final AxonSagaMongoTemplate axonSagaMongoTemplate;

    @Autowired
    public AxonDBInit(AxonMongoTemplate systemMongo,
                      MongoEventStore eventStore,
                      MongoTemplate mongoTemplate,
                      AxonSagaMongoTemplate axonSagaMongoTemplate) {

        this.axonMongoTemplate = systemMongo;
        this.eventStore = eventStore;
        this.mongoTemplate = mongoTemplate;
        this.axonSagaMongoTemplate = axonSagaMongoTemplate;
    }

    @Override
    public boolean initEvenIfExist() {
        initializeDB();

        return true;
    }

    @Override
    public boolean initIfNotExist() {
        boolean result = false;

        if (!mongoTemplate.collectionExists(SNAPSHOTEVENTS) && !mongoTemplate.collectionExists(DOMAINEVENTS)) {
            result = initEvenIfExist();
        } else {
            log.info("Collections already exists so will not init");
        }

        return result;
    }

    private void initializeDB() {
        log.info("Clearing out old Axon related collections from MongoDb");
        // clear all of Axon's collections
        axonMongoTemplate.domainEventCollection().drop();
        axonMongoTemplate.snapshotEventCollection().drop();
        axonSagaMongoTemplate.sagaCollection().drop();

        eventStore.ensureIndexes();
    }
}
