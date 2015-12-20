/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.server.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by Bjorn Harvold
 * Date: 2/23/15
 * Time: 5:47 PM
 * Responsibility:
 */
@Configuration
@Profile("local")
@PropertySource(value = "classpath:server-local.properties")
public class AppLocalConfig {
}
