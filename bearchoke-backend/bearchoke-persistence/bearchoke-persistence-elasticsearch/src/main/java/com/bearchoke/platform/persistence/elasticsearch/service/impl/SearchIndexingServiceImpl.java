/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.persistence.search.service.impl;

import com.bearchoke.platform.persistence.search.dto.Location;
import com.bearchoke.platform.persistence.search.service.SearchIndexingService;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.IndicesExists;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 2/27/15
 * Time: 5:32 PM
 * Responsibility:
 */
@Service("searchIndexingService")
@Log4j2
public class SearchIndexingServiceImpl implements SearchIndexingService {
    private static final String LOCATION_INDEX_NAME = "locations";
    private static final String LOCATION_INDEX_TYPE = "location";
    private final JestClient jestClient;

    @Autowired
    public SearchIndexingServiceImpl(JestClient jestClient){
        this.jestClient = jestClient;
    }

    @PostConstruct
    public void init() throws Exception {
        log.info("Instantiating jestSearchIndexingService...");
        log.info(String.format("Checking if %s needs to be created", LOCATION_INDEX_NAME));
        IndicesExists indicesExists = new IndicesExists.Builder(LOCATION_INDEX_NAME).build();
        JestResult r = jestClient.execute(indicesExists);

        if (!r.isSucceeded()) {
            log.info("Index does not exist. Creating index...");
            // create new index (if u have this in elasticsearch.yml and prefer
            // those defaults, then leave this out
            ImmutableSettings.Builder settings = ImmutableSettings.settingsBuilder();
            settings.put("number_of_shards", 1);
            settings.put("number_of_replicas", 0);

            CreateIndex.Builder builder = new CreateIndex.Builder(LOCATION_INDEX_NAME);
            builder.settings(settings.build().getAsMap());
            CreateIndex createIndex = builder.build();

            log.info(createIndex.toString());

            jestClient.execute(createIndex);

            log.info("Index created");
        } else {
            log.info("Index already exist!");
        }
    }

    public void indexLocations(List<Location> locations) {
        if (locations != null && !locations.isEmpty()) {
            log.info("Indexing locations with Elasticsearch Jest....");

            Bulk.Builder builder = new Bulk.Builder();

            if (locations != null && !locations.isEmpty()) {
                for (Location location : locations) {
                    builder.addAction(new Index.Builder(location).index(LOCATION_INDEX_NAME).type(LOCATION_INDEX_TYPE).build());
                }
            }

            Bulk bulk = builder.build();

            try {
                JestResult result = jestClient.execute(bulk);
                log.info("Bulk search success: " + result.isSucceeded());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

            log.info(String.format("Indexed %d documents", locations.size()));
        }
    }

}
