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

package com.bearchoke.platform.server;

import com.bearchoke.platform.server.config.AppConfig;
import com.bearchoke.platform.server.web.filter.JsonHttpRequestFilter;
import com.bearchoke.platform.server.web.filter.SimpleCORSFilter;
import com.bearchoke.platform.server.web.config.WebMvcConfig;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.web.WebApplicationInitializer;
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
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Slf4j
public class BearchokeWebApplicationInitializer implements WebApplicationInitializer {

    private static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";
    private static final String MONGODB_LOCAL = "mongodb-local";
    private static final String MONGODB_CLOUD = "mongodb-cloud";
    private static final String JPA = "jpa";
    private static final String JPA_LOCAL = "jpa-local";
    private static final String JPA_CLOUD = "jpa-cloud";
    private static final String REDIS_LOCAL = "redis-local";
    private static final String REDIS_CLOUD = "redis-cloud";
    private static final String RABBIT_LOCAL = "rabbit-local";
    private static final String RABBIT_CLOUD = "rabbit-cloud";
    private static final String FILTER_MAPPING = "/*";
    private static final String REDIS_SERVICE_ID = "bearchoke-redis";
    private static final String RABBIT_SERVICE_ID = "bearchoke-rabbit";
    private static final String MONGODB_SERVICE_ID = "bearchoke-mongodb";
    private static final String JPA_SERVICE_ID = "bearchoke-jpa";

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        createWebApplicationContext(servletContext);
        createSpringServlet(servletContext);
        createCXFServlet(servletContext);
        createFilters(servletContext);
    }

    private void createWebApplicationContext(ServletContext servletContext) {
        log.info("Creating Web Application Context started");

        List<Class> configClasses = new ArrayList<>();
        configClasses.add(AppConfig.class);

        // let's determine if this is a cloud based server
        Cloud cloud = getCloud();

        String activeProfiles = System.getProperty(SPRING_PROFILES_ACTIVE);

        if (StringUtils.isEmpty(activeProfiles)) {
            if (cloud == null) {
                // if no active profiles are specified, we default to these profiles
                activeProfiles = MONGODB_LOCAL + "," + REDIS_LOCAL + "," + RABBIT_LOCAL;
            } else {
                activeProfiles = MONGODB_CLOUD + "," + REDIS_CLOUD + "," + RABBIT_CLOUD;
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

    private void createSpringServlet(ServletContext servletContext) {
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

//        log.info("Creating Spring Servlet completed");
    }

    private void createCXFServlet(ServletContext servletContext) {
        log.info("Creating CXF Servlet started....");

        CXFServlet servlet = new CXFServlet();

        ServletRegistration.Dynamic appServlet = servletContext.addServlet("CXFServlet", servlet);
        appServlet.setLoadOnStartup(1);
        appServlet.addMapping("/services/*");

        // for serving up asynchronous events in tomcat
        appServlet.setAsyncSupported(true);

//        log.info("Creating CXF Servlet completed");
    }

    private void createFilters(ServletContext ctx) {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");

        SimpleCORSFilter corsFilter = new SimpleCORSFilter();
        corsFilter.setCorsAllowCredentials("true");
        corsFilter.setCorsAllowMethods("GET, POST, PUT, PATCH, DELETE, OPTIONS");
        corsFilter.setCorsAllowHeaders("content-type, x-requested-with, origin, accept, authorization, username, password, x-app-type, x-app-version, x-auth-token, soapaction");
        corsFilter.setCorsExposeHeaders("content-type, cookie, x-requested-with, origin, accept, username, password, x-app-type, x-app-version, x-auth-token, soapaction");
        corsFilter.setCorsMaxAge("3600");

        ctx.addFilter("SimpleCorsFilter", corsFilter).addMappingForUrlPatterns(
                EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), false, FILTER_MAPPING);
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

    private void printAvailableCloudServices(List<ServiceInfo> serviceInfos) {

        if (serviceInfos != null && serviceInfos.size() > 0) {
            log.info("Available Cloud Foundry Services are:");

            for (ServiceInfo si : serviceInfos) {
                log.info("Service ID: " + si.getId());
            }

        }

    }

    private boolean isCloudServiceAvailable(Cloud cloud, String id) {
        if (cloud.getServiceInfo(id) != null) {
            return true;
        } else {
            String error = "Required cloud service: " + id + " not available";
            log.error(error);
            throw new RuntimeException(error);
        }
    }

    private Cloud getCloud() {
        try {
            CloudFactory cloudFactory = new CloudFactory();
            return cloudFactory.getCloud();
        } catch (CloudException ce) {
            return null;
        }
    }
}
