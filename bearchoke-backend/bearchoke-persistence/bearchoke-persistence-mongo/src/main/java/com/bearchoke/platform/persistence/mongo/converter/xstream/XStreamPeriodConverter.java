/*
 * Copyright (c) 2015. Traveliko
 */

package com.bearchoke.platform.persistence.mongo.converter.xstream;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.time.Period;

/**
 * Created by Bjorn Harvold
 * Date: 10/24/15
 * Time: 14:20
 * Responsibility:
 */
public class XStreamPeriodConverter implements Converter {

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(Period.class);
    }

    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
        Period zdt = (Period) value;
        writer.setValue(zdt.toString());
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        return Period.parse(reader.getValue());
    }
}
