/*
 * Copyright (c) 2015. Traveliko
 */

package com.bearchoke.platform.persistence.mongo.converter.spring;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.time.ZonedDateTime;

/**
 * Created by Bjorn Harvold
 * Date: 10/24/15
 * Time: 15:12
 * Responsibility:
 */
@WritingConverter
@Log4j2
public class ZonedDateTimeWriteConverter implements Converter<ZonedDateTime, String> {

    @Override
    public String convert(ZonedDateTime zdt) {
        String result = null;

            if (log.isTraceEnabled()) {
                log.trace("Converting ZonedDateTime {} to string", zdt.toString());
            }

            result = zdt.toString();

        return result;
    }
}
