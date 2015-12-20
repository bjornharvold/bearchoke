/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.server.common.init;

import com.bearchoke.platform.base.init.DBInit;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Special class used to initialize the database when starting the container. The database is only initialized
 * when the collection "User" is not yet available.
 * We need to check for the display name of the application context since we by default have two using spring-mvc
 * the way we do.
 */
@Component
@Log4j2
public class RunDBInitializerWhenNeeded implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if ("Root WebApplicationContext".equals(event.getApplicationContext().getDisplayName())) {
            log.info("Initializing and populating DB if necessary");

            Map<String, DBInit> map = event.getApplicationContext().getBeansOfType(DBInit.class);

            if (map != null && !map.isEmpty()) {
                List<DBInit> list = new ArrayList<>(map.values());

                // order based on the @Order annotation
                list.sort(new AnnotationAwareOrderComparator());

                log.info(String.format("Found %d beans to init", list.size()));
                for (int i = 0; i < list.size(); i++) {
                    DBInit dbInit = list.get(i);
                    log.info(String.format("Init-ing (order: %d): %s", i, dbInit.getClass().getSimpleName()));
                    // our current strategy is ONLY to persist these objects if the data does NOT already exist
                    boolean didExecute = dbInit.initIfNotExist();

                    if (didExecute) {
                        // wait a bit in case the next db init is dependent on the former
                        try {
                            log.info("Sleeping 2 seconds before starting the next DB init bean");
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }
            }
        }
    }
}
