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

package com.bearchoke.platform.platform.base.config;

import org.jasypt.digest.StandardStringDigester;
import org.jasypt.digest.config.EnvironmentStringDigesterConfig;
import org.jasypt.encryption.pbe.StandardPBEBigDecimalEncryptor;
import org.jasypt.encryption.pbe.StandardPBEBigIntegerEncryptor;
import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.jasypt.salt.RandomSaltGenerator;
import org.jasypt.springsecurity3.authentication.encoding.PasswordEncoder;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.inject.Inject;

/**
 * Created by Bjorn Harvold
 * Date: 1/7/14
 * Time: 4:59 PM
 * Responsibility:
 */
@Configuration
public class EncryptionConfig {

    @Inject
    private Environment environment;

    @Bean
    public StrongPasswordEncryptor strongPasswordEncryptor() {
        return new StrongPasswordEncryptor();
    }

    @Bean
    public RandomSaltGenerator randomSaltGenerator() {
        return new RandomSaltGenerator();
    }

    @Bean
    public StandardStringDigester standardStringDigester() {
        StandardStringDigester ssd = new StandardStringDigester();
        ssd.setConfig(environmentStringDigesterConfig());

        return ssd;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder pe = new PasswordEncoder();
        pe.setPasswordEncryptor(strongPasswordEncryptor());

        return pe;
    }

    @Bean
    public EnvironmentStringPBEConfig environmentStringPBEConfig() {
        EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setPasswordEnvName("PLATFORM_ENCRYPTION_PASSWORD");

        return config;
    }

    @Bean
    public EnvironmentStringDigesterConfig environmentStringDigesterConfig() {
        EnvironmentStringDigesterConfig esdc = new EnvironmentStringDigesterConfig();
        esdc.setAlgorithm("sha-1");
        esdc.setIterations(10000);
        esdc.setSaltGenerator(randomSaltGenerator());

        return esdc;
    }

    @Bean
    public StandardPBEStringEncryptor standardPBEStringEncryptor() {
        StandardPBEStringEncryptor sse = new StandardPBEStringEncryptor();
        sse.setConfig(environmentStringPBEConfig());

        return sse;
    }

    @Bean
    public StandardPBEByteEncryptor standardPBEByteEncryptor() {
        StandardPBEByteEncryptor sse = new StandardPBEByteEncryptor();
        sse.setConfig(environmentStringPBEConfig());
        sse.setSaltGenerator(randomSaltGenerator());
        sse.setKeyObtentionIterations(10000);

        return sse;
    }

    @Bean
    public StandardPBEBigDecimalEncryptor standardPBEBigDecimalEncryptor() {
        StandardPBEBigDecimalEncryptor sse = new StandardPBEBigDecimalEncryptor();
        sse.setConfig(environmentStringPBEConfig());

        return sse;
    }

    @Bean
    public StandardPBEBigIntegerEncryptor standardPBEBigIntegerEncryptor() {
        StandardPBEBigIntegerEncryptor sse = new StandardPBEBigIntegerEncryptor();
        sse.setConfig(environmentStringPBEConfig());

        return sse;
    }

}
