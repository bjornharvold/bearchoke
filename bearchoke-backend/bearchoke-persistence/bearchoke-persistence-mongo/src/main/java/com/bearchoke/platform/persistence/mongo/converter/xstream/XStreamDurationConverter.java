/*
 * Copyright (c) 2015. Traveliko
 */

package com.bearchoke.platform.persistence.mongo.converter.xstream;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.time.Duration;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

/**
 * Created by Bjorn Harvold
 * Date: 10/24/15
 * Time: 14:20
 * Responsibility:
 */
public class XStreamDurationConverter implements Converter {

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(Duration.class);
    }

    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
        Duration zdt = (Duration) value;
        writer.setValue(zdt.toString());
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        return Duration.parse(reader.getValue());
    }
}
