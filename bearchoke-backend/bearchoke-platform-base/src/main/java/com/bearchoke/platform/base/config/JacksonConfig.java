/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.base.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.geo.GeoJsonModule;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.zalando.jackson.datatype.money.MoneyModule;

/**
 * Created by Bjorn Harvold
 * Date: 8/13/14
 * Time: 3:21 PM
 * Responsibility:
 */
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();

        builder.featuresToDisable(
                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
        );

        builder.featuresToEnable(
                SerializationFeature.WRITE_DATES_WITH_ZONE_ID,
//                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
//                SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS,
//                DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS,
                DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT
        );

        builder.indentOutput(true);

        builder.failOnEmptyBeans(false);
        builder.failOnUnknownProperties(false);

        // do not include null value in json to make object graph smaller
        builder.serializationInclusion(JsonInclude.Include.NON_NULL);

        builder.modules(new GeoJsonModule(), new JavaTimeModule(), new MoneyModule());

        return builder.build();
    }

}
