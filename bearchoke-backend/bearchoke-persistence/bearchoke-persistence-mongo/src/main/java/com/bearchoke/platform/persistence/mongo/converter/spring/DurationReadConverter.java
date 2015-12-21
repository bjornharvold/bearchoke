/*
 * Copyright (c) 2015. Traveliko
 */

package com.bearchoke.platform.persistence.mongo.converter.spring;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.time.Duration;
import java.time.format.DateTimeParseException;

/**
 * Created by Bjorn Harvold
 * Date: 10/24/15
 * Time: 15:12
 * Responsibility:
 */
@ReadingConverter
@Log4j2
public class DurationReadConverter implements Converter<String, Duration> {

    @Override
    public Duration convert(String value) {
        Duration result = null;

        try {
            if (log.isTraceEnabled()) {
                log.trace("Converting String {} to Duration", value);
            }

            result = Duration.parse(value);
        } catch (DateTimeParseException e) {
            log.error("{} could not be converted to java.time.Duration", value);
        }

        return result;
    }
}
