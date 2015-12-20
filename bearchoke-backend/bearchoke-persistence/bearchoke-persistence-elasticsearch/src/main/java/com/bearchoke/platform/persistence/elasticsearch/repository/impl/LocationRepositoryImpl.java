/*
 * Copyright (c) 2015. Bearchoke
 */

package com.traveliko.platform.persistence.search.repository.impl;

import com.traveliko.platform.persistence.search.dto.Location;
import com.traveliko.platform.persistence.search.repository.LocationRepository;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Created by Bjorn Harvold
 * Date: 2/26/15
 * Time: 3:56 PM
 * Responsibility:
 */
@Repository
@Log4j2
public class LocationRepositoryImpl implements LocationRepository {

    private static final String LOCATION_INDEX_NAME = "locations";
    private static final String LOCATION_INDEX_TYPE = "location";

    private final JestClient jestClient;
    @Autowired
    public LocationRepositoryImpl(JestClient jestClient) {
        this.jestClient = jestClient;
    }

    /**
     * What we want this complex elasticsearch query to do is:
     * 1. Match user search input to the fields code, name and description
     * 2. If any of the documents are of type REGION --> boost document 4
     * 2. If any of the documents are of type CITY --> boost document 3
     * 3. If any of the documents are of type AIRPORT --> boost document 2
     * 4. If any of the documents are of type COUNTRY --> boost document 1
     * 4. If any of the documents are of type HOTEL --> no boost necessary
     *
     * @param userInput user input from search
     * @return
     */
    public List<Location> locationSearch(String userInput) {
        List<Location> locations = null;

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.queryStringQuery(userInput));

        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(LOCATION_INDEX_NAME)
                .addType(LOCATION_INDEX_TYPE).build();

        try {
            JestResult result = jestClient.execute(search);

            locations = result.getSourceAsObjectList(Location.class);

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return locations;
    }
}
