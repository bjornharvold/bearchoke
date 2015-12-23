/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.domain.search.dto;

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
    private String name;
    private String code;

    public Location() {
    }

    public Location(String id) {
        this.id = id;
    }

    public Location(String id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }
}
