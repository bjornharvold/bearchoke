/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.persistence.search.repository;

import com.bearchoke.platform.persistence.search.dto.Location;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 2/26/15
 * Time: 3:55 PM
 * Responsibility:
 */
public interface LocationRepository {
    List<Location> locationSearch(String userInput);
}
