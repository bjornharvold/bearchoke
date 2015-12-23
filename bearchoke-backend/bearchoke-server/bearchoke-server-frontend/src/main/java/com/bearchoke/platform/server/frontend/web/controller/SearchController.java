/*
 * Copyright (c) 2015. Traveliko
 */

package com.bearchoke.platform.server.frontend.web.controller;

import com.bearchoke.platform.domain.search.dto.Locations;
import com.bearchoke.platform.domain.search.repository.LocationRepository;
import com.bearchoke.platform.server.common.ApplicationMediaType;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Property search controller
 * @author Bjorn Harvold
 */

@RestController
@Log4j2
public class SearchController {

    private final LocationRepository locationRepository;

    @Autowired
    public SearchController(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    /**
     * Search for properties
     *
     * @param search search string
     * @return
     */
    @RequestMapping(
            value = "/api/search",
            headers = {"Accept="+ ApplicationMediaType.APPLICATION_BEARCHOKE_V1_JSON_VALUE},
            produces = ApplicationMediaType.APPLICATION_BEARCHOKE_V1_JSON_VALUE,
            method = RequestMethod.GET
    )
    @ResponseStatus(HttpStatus.OK)
    public Locations hotelSearch(@RequestParam(value = "s", required = true) String search) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("Location search: %s", search));
        }

        return new Locations(locationRepository.locationSearch(search));
    }

}
