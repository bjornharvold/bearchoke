/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.persistence.elasticsearch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("local")
@PropertySource(value = "classpath:elasticsearch-local.properties")
public class ElasticsearchLocalConfig {

}
