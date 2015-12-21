/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.persistence.mongo.config;

import com.thoughtworks.xstream.XStream;
import com.bearchoke.platform.persistence.mongo.converter.xstream.XStreamDurationConverter;
import com.bearchoke.platform.persistence.mongo.converter.xstream.XStreamLocalDateConverter;
import com.bearchoke.platform.persistence.mongo.converter.xstream.XStreamLocalDateTimeConverter;
import com.bearchoke.platform.persistence.mongo.converter.xstream.XStreamLocalTimeConverter;
import com.bearchoke.platform.persistence.mongo.converter.xstream.XStreamPeriodConverter;
import com.bearchoke.platform.persistence.mongo.converter.xstream.XStreamZonedDateTimeConverter;
import org.axonframework.serializer.xml.CompactDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Bjorn Harvold
 * Date: 7/19/14
 * Time: 10:36 PM
 * Responsibility:
 */
@Configuration
public class XStreamConfig {

    @Bean(name = "xStream")
    public XStream xStream() {
        XStream xStream = new XStream(new CompactDriver());

        xStream.registerConverter(new XStreamZonedDateTimeConverter());
        xStream.registerConverter(new XStreamDurationConverter());
        xStream.registerConverter(new XStreamLocalDateTimeConverter());
        xStream.registerConverter(new XStreamLocalDateConverter());
        xStream.registerConverter(new XStreamLocalTimeConverter());
        xStream.registerConverter(new XStreamPeriodConverter());

        return xStream;
    }

}
