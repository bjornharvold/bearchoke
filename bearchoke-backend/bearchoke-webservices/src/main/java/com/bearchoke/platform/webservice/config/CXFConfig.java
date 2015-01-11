package com.bearchoke.platform.webservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by Bjorn Harvold
 * Date: 1/11/15
 * Time: 2:19 PM
 * Responsibility: Configured Apache CXF using Java Config
 */
@Configuration
@ImportResource({
        "classpath:META-INF/cxf/cxf.xml",
        "classpath:META-INF/cxf/cxf-servlet.xml"
})
public class CXFConfig {
}
