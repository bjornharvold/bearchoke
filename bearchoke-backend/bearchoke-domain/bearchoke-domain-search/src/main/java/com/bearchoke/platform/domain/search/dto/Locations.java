package com.bearchoke.platform.domain.search.dto;

import lombok.Getter;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 12/23/15
 * Time: 14:08
 * Responsibility:
 */
public class Locations {
    @Getter
    private final List<Location> list;

    public Locations(List<Location> list) {
        this.list = list;
    }
}
