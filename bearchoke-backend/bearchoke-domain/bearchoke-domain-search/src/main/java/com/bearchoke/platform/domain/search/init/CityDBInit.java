/*
 * Copyright (c) 2015. Traveliko
 */

package com.bearchoke.platform.domain.search.init;

import com.bearchoke.platform.base.init.DBInit;
import com.bearchoke.platform.domain.search.dto.Location;
import com.bearchoke.platform.domain.search.service.SearchIndexingService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 2/21/15
 * Time: 1:23 PM
 * Responsibility: Create all country reference data
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Component
@Log4j2
@Order(4)
public class CityDBInit implements DBInit {
    private static final String JSON = "cities.json";
    private final ObjectMapper objectMapper;
    private final SearchIndexingService searchIndexingService;

    @Autowired
    public CityDBInit(ObjectMapper objectMapper,
                      SearchIndexingService searchIndexingService) {

        this.objectMapper = objectMapper;
        this.searchIndexingService = searchIndexingService;
    }

    @Override
    public boolean initEvenIfExist() {
        // overwrite everything
        return initIfNotExist();
    }

    @Override
    public boolean initIfNotExist() {
        boolean result = false;


        log.info("Persisting locations in Elasticsearch");

        ClassPathResource json = new ClassPathResource(JSON);

        if (json.exists() && json.isReadable()) {

            try {
                List<Location> locations = objectMapper.readValue(json.getInputStream(), new TypeReference<List<Location>>() {});

                log.info(String.format("Persisting %d locations", locations.size()));

                searchIndexingService.indexLocations(locations);

                result = true;

            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }

        } else {
            log.warn(String.format("Could not access '%s'", JSON));
        }

        return result;
    }
}
