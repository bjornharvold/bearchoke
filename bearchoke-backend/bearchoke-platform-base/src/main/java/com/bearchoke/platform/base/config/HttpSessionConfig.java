package com.bearchoke.platform.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

/**
 * Created by Bjorn Harvold
 * Date: 3/12/15
 * Time: 10:49 AM
 * Responsibility:
 */
@Configuration
public class HttpSessionConfig {
    @Bean
    public HttpSessionStrategy httpSessionStrategy() {
        return new HeaderHttpSessionStrategy();
    }
}
