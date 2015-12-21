/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.persistence.mongo.converter.spring;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.time.Period;
import java.time.format.DateTimeParseException;

/**
 * Created by Bjorn Harvold
 * Date: 10/24/15
 * Time: 15:12
 * Responsibility:
 */
@ReadingConverter
@Log4j2
public class PeriodReadConverter implements Converter<String, Period> {

    @Override
    public Period convert(String value) {
        Period result = null;

        try {
            if (log.isTraceEnabled()) {
                log.trace("Converting String {} to Period", value);
            }

            result = Period.parse(value);
        } catch (DateTimeParseException e) {
            log.error("{} could not be converted to java.time.Period", value);
        }

        return result;
    }
}
