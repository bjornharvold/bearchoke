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

package com.bearchoke.platform.persistence.jpa.config;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by Bjorn Harvold
 * Date: 1/9/14
 * Time: 11:55 PM
 * Responsibility:
 */
@Configuration
@Profile("local")
@PropertySource(value = "classpath:jpa-local.properties")
public class JpaLocalConfig {

    @Inject
    private Environment environment;

    @Bean(initMethod = "init", destroyMethod = "close")
    public DataSource dataSource() {
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        ds.setUniqueResourceName(environment.getProperty("jpa.unique.resource.name"));
        ds.setXaDataSourceClassName(environment.getProperty("jpa.xa.datasource.classname"));
        ds.setMinPoolSize(environment.getProperty("jpa.ds.minpoolsize", Integer.class));
        ds.setMaxPoolSize(environment.getProperty("jpa.ds.maxpoolsize", Integer.class));

        Properties props = new Properties();
        props.put("databaseName", environment.getProperty("jpa.db.name"));
        props.put("createDatabase", environment.getProperty("jpa.db.create.strategy"));
        ds.setXaProperties(props);

        ds.setPoolSize(1);


        return ds;
    }

}
