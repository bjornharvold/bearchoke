/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.persistence.mongo.converter.xstream;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Bjorn Harvold
 * Date: 10/24/15
 * Time: 14:20
 * Responsibility:
 */
public class XStreamLocalDateTimeConverter implements Converter {

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(LocalDateTime.class);
    }

    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
        LocalDateTime zdt = (LocalDateTime) value;
        writer.setValue(zdt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        return LocalDateTime.parse(reader.getValue(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
