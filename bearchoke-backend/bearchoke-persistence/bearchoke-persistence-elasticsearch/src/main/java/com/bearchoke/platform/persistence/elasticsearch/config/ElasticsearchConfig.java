/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.persistence.search.config;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.inject.Inject;

@Configuration
@Log4j2
public class ElasticsearchConfig {

    @Inject
    private Environment environment;

    @Bean(destroyMethod = "shutdownClient")
    public JestClient jestClient() {
        // Construct a new Jest client according to configuration via factory
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
                .Builder(environment.getProperty("jest.url"))
                .connTimeout(15000)
                .readTimeout(30000)
                .multiThreaded(true)
                .build());

        return factory.getObject();
    }
}
