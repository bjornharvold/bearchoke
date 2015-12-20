/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bearchoke.platform.server.frontend.web;

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
