/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.server.common;

import org.springframework.http.MediaType;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 8/12/14
 * Time: 4:38 PM
 * Responsibility:
 */
public class ApplicationMediaType extends MediaType {
    public final static String APPLICATION_BEARCHOKE_V1_JSON_VALUE = "application/vnd.bearchoke-v1+json";
    public final static MediaType APPLICATION_BEARCHOKE_V1_JSON = valueOf(APPLICATION_BEARCHOKE_V1_JSON_VALUE);
    public final static String APPLICATION_BEARCHOKE_V2_JSON_VALUE = "application/vnd.bearchoke-v2+json";
    public final static MediaType APPLICATION_BEARCHOKE_V2_JSON = valueOf(APPLICATION_BEARCHOKE_V2_JSON_VALUE);

    public ApplicationMediaType(String type) {
        super(type);
    }

    public ApplicationMediaType(String type, String subtype) {
        super(type, subtype);
    }

    public ApplicationMediaType(String type, String subtype, Charset charset) {
        super(type, subtype, charset);
    }

    public ApplicationMediaType(String type, String subtype, double qualityValue) {
        super(type, subtype, qualityValue);
    }

    public ApplicationMediaType(MediaType other, Map<String, String> parameters) {
        super(other, parameters);
    }

    public ApplicationMediaType(String type, String subtype, Map<String, String> parameters) {
        super(type, subtype, parameters);
    }
}
