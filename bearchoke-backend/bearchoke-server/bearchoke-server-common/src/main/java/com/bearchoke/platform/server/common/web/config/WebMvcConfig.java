/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.server.common.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.bearchoke.platform.server.common.ApplicationMediaType;
import com.bearchoke.platform.server.common.web.argumentresolver.DateTimeZoneHandlerMethodArgumentResolver;
import com.bearchoke.platform.server.common.web.argumentresolver.LocationHandlerMethodArgumentResolver;
import com.bearchoke.platform.server.common.web.interceptor.DateTimeZoneHandlerInterceptor;
import com.bearchoke.platform.server.common.web.interceptor.UserLocationHandlerInterceptor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.Jsr310DateTimeFormatAnnotationFormatterFactory;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MessageSourceResourceBundleLocator;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
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
@ComponentScan( basePackages =
        {
                "com.bearchoke.platform.server.**.web"
        }
)
@Log4j2
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Inject
    private ObjectMapper objectMapper;

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

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldAnnotation(new Jsr310DateTimeFormatAnnotationFormatterFactory());
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

    @Bean(name = "multipartResolver")
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
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
