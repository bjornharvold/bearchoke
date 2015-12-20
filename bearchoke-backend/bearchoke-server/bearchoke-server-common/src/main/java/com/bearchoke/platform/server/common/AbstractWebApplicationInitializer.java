/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.server.common;

import com.bearchoke.platform.server.common.web.config.WebMvcConfig;
import com.bearchoke.platform.server.common.web.filter.JsonHttpRequestFilter;
import com.bearchoke.platform.server.common.web.filter.SimpleCORSFilter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.DispatcherType;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 8/19/15
 * Time: 15:32
 * Responsibility:
 */
@Log4j2
public abstract class AbstractWebApplicationInitializer {
    private static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";
    private static final String LOCAL = "local";
    private static final String CLOUD = "cloud";
    private static final String MONGODB_LOCAL = "mongodb-local";
    private static final String MONGODB_CLOUD = "mongodb-cloud";
    private static final String JPA = "jpa";
    private static final String JPA_LOCAL = "jpa-local";
    private static final String JPA_CLOUD = "jpa-cloud";
    private static final String REDIS_LOCAL = "redis-local";
    private static final String REDIS_CLOUD = "redis-cloud";
    private static final String RABBIT_LOCAL = "rabbit-local";
    private static final String RABBIT_CLOUD = "rabbit-cloud";
    private static final String ELASTICSEARCH_CLOUD = "elasticsearch-cloud";
    private static final String ELASTICSEARCH_LOCAL = "elasticsearch-local";
    private static final String FILTER_MAPPING = "/*";
    private static final String REDIS_SERVICE_ID = "traveliko-redis";
    private static final String RABBIT_SERVICE_ID = "traveliko-rabbit";
    private static final String MONGODB_SERVICE_ID = "traveliko-mongodb";
    private static final String JPA_SERVICE_ID = "traveliko-jpa";
    private static final String ELASTICSEARCH_SERVICE_ID = "traveliko-elasticsearch";

    protected void createWebApplicationContext(ServletContext servletContext, Class clazz) {
        log.info("Creating Web Application Context started");

        List<Class> configClasses = new ArrayList<>();
        configClasses.add(clazz);

        // let's determine if this is a cloud based server
        Cloud cloud = getCloud();

        String activeProfiles = System.getProperty(SPRING_PROFILES_ACTIVE);

        if (StringUtils.isEmpty(activeProfiles)) {
            if (cloud == null) {
                // if no active profiles are specified, we default to these profiles
                activeProfiles = String.format("%s,%s,%s,%s,%s", MONGODB_LOCAL,REDIS_LOCAL,RABBIT_LOCAL,ELASTICSEARCH_LOCAL,LOCAL);
            } else {
                activeProfiles = String.format("%s,%s,%s,%s,%s", MONGODB_CLOUD,REDIS_CLOUD,RABBIT_CLOUD,ELASTICSEARCH_CLOUD,CLOUD);
            }
        }

        log.info("Active spring profiles: " + activeProfiles);

        // load local or cloud based configs
        if (cloud != null) {

            // list available service - fail servlet initializing if we are missing one that we require below
            printAvailableCloudServices(cloud.getServiceInfos());

        }

        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.register(configClasses.toArray(new Class[configClasses.size()]));

        servletContext.addListener(new ContextLoaderListener(appContext));
        servletContext.addListener(new RequestContextListener());

//        log.info("Creating Web Application Context completed");
    }

    protected void createSpringServlet(ServletContext servletContext) {
        log.info("Creating Spring Servlet started....");

        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.register(
                WebMvcConfig.class
        );

        DispatcherServlet sc = new DispatcherServlet(appContext);

        ServletRegistration.Dynamic appServlet = servletContext.addServlet("DispatcherServlet", sc);
        appServlet.setLoadOnStartup(1);
        appServlet.addMapping("/");

        // for serving up asynchronous events in tomcat
        appServlet.setInitParameter("dispatchOptionsRequest", "true");
        appServlet.setAsyncSupported(true);

        // enable multipart file upload support
        // file size limit is 10Mb
        // max file request size = 20Mb
        appServlet.setMultipartConfig(new MultipartConfigElement("/tmp", 10000000l, 20000000l, 0));

//        log.info("Creating Spring Servlet completed");
    }


    protected void createFilters(ServletContext ctx) {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");

        SimpleCORSFilter corsFilter = new SimpleCORSFilter();
        corsFilter.setCorsAllowCredentials("true");
        corsFilter.setCorsAllowMethods("GET, POST, PUT, PATCH, DELETE, OPTIONS");
        corsFilter.setCorsAllowHeaders("content-type, x-requested-with, origin, accept, authorization, username, password, x-app-type, x-app-version, x-auth-token, soapaction");
        corsFilter.setCorsExposeHeaders("content-type, cookie, x-requested-with, origin, accept, username, password, x-app-type, x-app-version, x-auth-token, soapaction");
        corsFilter.setCorsMaxAge("3600");

        ctx.addFilter("springSessionRepositoryFilter", DelegatingFilterProxy.class).addMappingForUrlPatterns(
                EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), false, FILTER_MAPPING);
        ctx.addFilter("simpleCORSFilter", DelegatingFilterProxy.class).addMappingForUrlPatterns(
                EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), false, FILTER_MAPPING);
//        ctx.addFilter("SimpleCorsFilter", corsFilter).addMappingForUrlPatterns(
//                EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), false, FILTER_MAPPING);
        ctx.addFilter("JsonHttpRequestFilter", new JsonHttpRequestFilter()).addMappingForUrlPatterns(
                EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), false, "/api/authenticate");
        ctx.addFilter("springSecurityFilterChain", DelegatingFilterProxy.class).addMappingForUrlPatterns(
                EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), false, FILTER_MAPPING);
        ctx.addFilter("CharacterEncodingFilter", characterEncodingFilter).addMappingForUrlPatterns(
                EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), false, FILTER_MAPPING);
        ctx.addFilter("HiddenHttpMethodFilter", new HiddenHttpMethodFilter()).addMappingForUrlPatterns(
                EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), false, FILTER_MAPPING);
        ctx.addFilter("HttpPutFormContentFilter", new HttpPutFormContentFilter()).addMappingForUrlPatterns(
                EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), false, FILTER_MAPPING);
        ctx.addFilter("ShallowEtagHeaderFilter", new ShallowEtagHeaderFilter()).addMappingForUrlPatterns(
                EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), false, FILTER_MAPPING);

    }

    protected void printAvailableCloudServices(List<ServiceInfo> serviceInfos) {

        if (serviceInfos != null && serviceInfos.size() > 0) {
            log.info("Available Cloud Foundry Services are:");

            for (ServiceInfo si : serviceInfos) {
                log.info("Service ID: " + si.getId());
            }

        }

    }

    protected boolean isCloudServiceAvailable(Cloud cloud, String id) {
        if (cloud.getServiceInfo(id) != null) {
            return true;
        } else {
            String error = "Required cloud service: " + id + " not available";
            log.error(error);
            throw new RuntimeException(error);
        }
    }

    protected Cloud getCloud() {
        try {
            CloudFactory cloudFactory = new CloudFactory();
            return cloudFactory.getCloud();
        } catch (CloudException ce) {
            return null;
        }
    }
}
