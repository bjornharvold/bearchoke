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

package com.bearchoke.platform.server.frontend;

import com.bearchoke.platform.server.common.AbstractWebApplicationInitializer;

import com.bearchoke.platform.server.frontend.config.FrontendAppConfig;
import lombok.extern.log4j.Log4j2;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

@Log4j2
public class FrontendWebApplicationInitializer extends AbstractWebApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        createWebApplicationContext(servletContext, FrontendAppConfig.class);
        createSpringServlet(servletContext);
        createCXFServlet(servletContext);
        createFilters(servletContext);
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
}
