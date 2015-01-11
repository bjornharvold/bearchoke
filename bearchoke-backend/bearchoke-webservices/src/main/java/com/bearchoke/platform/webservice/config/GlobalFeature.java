package com.bearchoke.platform.webservice.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.Bus;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;

import javax.inject.Named;

/**
 * Created by Bjorn Harvold
 * Date: 1/11/15
 * Time: 3:01 PM
 * Responsibility:
 */
@Named
@Slf4j
public class GlobalFeature extends AbstractFeature {

    private final LoggingInInterceptor loggingInInterceptor;
    private final LoggingOutInterceptor loggingOutInterceptor;

    public GlobalFeature() {
        this.loggingInInterceptor = new LoggingInInterceptor();
        this.loggingInInterceptor.setPrettyLogging(true);
        this.loggingOutInterceptor = new LoggingOutInterceptor();
        this.loggingOutInterceptor.setPrettyLogging(true);
    }

    @Override
    protected void initializeProvider(InterceptorProvider provider, Bus bus) {
        if (log.isDebugEnabled()) {
            log.debug("Applying logging to all incoming and outgoing CXF SOAP Messages");
        }

        bus.getInInterceptors().add(loggingInInterceptor);
        bus.getInFaultInterceptors().add(loggingInInterceptor);
        bus.getOutInterceptors().add(loggingOutInterceptor);
        bus.getOutFaultInterceptors().add(loggingOutInterceptor);
    }
}
