/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.domain.search.service;

import com.bearchoke.platform.domain.search.dto.Location;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 2/27/15
 * Time: 5:31 PM
 * Responsibility:
 */
public interface SearchIndexingService {
    void indexLocations(List<Location> locations);
}
