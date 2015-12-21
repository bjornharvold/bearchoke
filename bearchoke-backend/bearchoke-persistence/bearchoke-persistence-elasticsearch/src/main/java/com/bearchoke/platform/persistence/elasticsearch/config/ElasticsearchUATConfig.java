/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.persistence.search.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("uat")
@PropertySource(value = "classpath:elasticsearch-uat.properties")
public class ElasticsearchUATConfig {
}
