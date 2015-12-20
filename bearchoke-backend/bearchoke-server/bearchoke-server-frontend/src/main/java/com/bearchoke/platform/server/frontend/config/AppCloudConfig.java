package com.bearchoke.platform.server.frontend.config;

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
@Profile("cloud")
@PropertySource(value = "classpath:server-cloud.properties")
public class AppCloudConfig {
}
