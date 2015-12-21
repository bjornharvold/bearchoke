/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.persistence.search.utils;

import com.bearchoke.platform.persistence.search.dto.Location;
import com.bearchoke.platform.persistence.search.enums.LocationType;

public class LocationBuilder {

    private Location result;

    public LocationBuilder(String id, LocationType type) {
        result = new Location(id, type);
    }

    public LocationBuilder code(String code) {
        result.setCode(code);
        return this;
    }

    public LocationBuilder name(String name) {
        result.setName(name);
        return this;
    }

    public LocationBuilder description(String description) {
        result.setDescription(description);
        return this;
    }

    public Location build() {
        return result;
    }

}
