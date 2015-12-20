/*
 * Copyright (c) 2015. Bearchoke
 */

package com.traveliko.platform.persistence.search.dto;

import com.traveliko.platform.persistence.search.enums.LocationType;
import io.searchbox.annotations.JestId;
import lombok.Data;

/**
 * Created by Bjorn Harvold
 * Date: 2/22/15
 * Time: 1:00 PM
 * Responsibility:
 */
@Data
public class Location {

    @JestId
    private String id;
    private LocationType type;
    private String name;
    private String code;
    private String description;

    public Location() {
    }

    public Location(String id, LocationType type) {
        this.id = id;
        this.type = type;
    }

    public Location(String id, LocationType type, String code, String name, String description) {
        this.id = id;
        this.type = type;
        this.code = code;
        this.name = name;
        this.description =  description;
    }
}
