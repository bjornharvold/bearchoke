/*
 * Copyright (c) 2015. Traveliko
 */

package com.bearchoke.platform.persistence.mongo.converter.xstream;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Bjorn Harvold
 * Date: 10/24/15
 * Time: 14:20
 * Responsibility:
 */
public class XStreamZonedDateTimeConverter implements Converter {

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(ZonedDateTime.class);
    }

    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
        ZonedDateTime zdt = (ZonedDateTime) value;
        writer.setValue(zdt.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        return ZonedDateTime.parse(reader.getValue());
    }
}
