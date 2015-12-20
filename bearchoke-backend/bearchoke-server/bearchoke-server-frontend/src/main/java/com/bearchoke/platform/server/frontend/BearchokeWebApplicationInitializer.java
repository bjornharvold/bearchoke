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

import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.transport.servlet.CXFServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

@Slf4j
public class BearchokeWebApplicationInitializer extends AbstractWebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        createWebApplicationContext(servletContext);
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
