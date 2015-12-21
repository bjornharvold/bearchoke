/*
 * Copyright (c) 2015. Traveliko
 */

package com.bearchoke.platform.persistence.mongo.converter.spring;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.time.Duration;

/**
 * Created by Bjorn Harvold
 * Date: 10/24/15
 * Time: 15:12
 * Responsibility:
 */
@WritingConverter
@Log4j2
public class DurationWriteConverter implements Converter<Duration, String> {

    @Override
    public String convert(Duration duration) {
        String result = null;

        if (log.isTraceEnabled()) {
            log.trace("Converting Duration {} to string", duration.toString());
        }

        result = duration.toString();

        return result;
    }
}
