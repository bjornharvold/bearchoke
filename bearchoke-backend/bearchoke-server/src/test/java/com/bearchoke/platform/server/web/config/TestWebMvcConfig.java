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

package com.bearchoke.platform.server.web.config;


import com.bearchoke.platform.server.jackson.CustomObjectMapper;
import com.bearchoke.platform.server.web.ApplicationMediaType;
import com.bearchoke.platform.server.web.argumentresolver.DateTimeZoneHandlerMethodArgumentResolver;
import com.bearchoke.platform.server.web.argumentresolver.LocationHandlerMethodArgumentResolver;
import com.bearchoke.platform.server.web.interceptor.DateTimeZoneHandlerInterceptor;
import com.bearchoke.platform.server.web.interceptor.UserLocationHandlerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MessageSourceResourceBundleLocator;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * <p>
 * Date: 1/3/14
 * <p>
 * Time: 4:12 PM
 * <p>
 * Responsibility:
 */

@Configuration
@EnableWebMvc
@ComponentScan("com.bearchoke.platform.server.web")
@Slf4j
public class TestWebMvcConfig extends WebMvcConfigurerAdapter {

    @Inject
    private CustomObjectMapper objectMapper;

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /**
     * Messages to support internationalization/localization.
     */
    @Bean(name = "messageSource")
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("/WEB-INF/i18n/messages");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setCacheSeconds(30);

        return messageSource;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("Configuring http message converters...");
        MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter();
        List<MediaType> types = new ArrayList<>(1);
        types.add(ApplicationMediaType.APPLICATION_JSON);
        types.add(ApplicationMediaType.APPLICATION_BEARCHOKE_V1_JSON);
        types.add(ApplicationMediaType.APPLICATION_BEARCHOKE_V2_JSON);
        jacksonConverter.setSupportedMediaTypes(types);
        jacksonConverter.setObjectMapper(objectMapper);
        converters.add(jacksonConverter);
    }

    @Bean(name = "methodValidationPostProcessor")
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
        methodValidationPostProcessor.setValidator(localValidatorFactoryBean());

        return methodValidationPostProcessor;
    }

    @Bean(name = "localValidatorFactoryBean")
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setMessageInterpolator(
                new ResourceBundleMessageInterpolator(
                        new MessageSourceResourceBundleLocator(messageSource())
                )
        );

        return localValidatorFactoryBean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("Adding interceptors...");
        registry.addInterceptor(new DateTimeZoneHandlerInterceptor());
        registry.addInterceptor(new UserLocationHandlerInterceptor());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        log.info("Adding argument resolvers...");
        argumentResolvers.add(new DateTimeZoneHandlerMethodArgumentResolver());
        argumentResolvers.add(new LocationHandlerMethodArgumentResolver());
    }


}
