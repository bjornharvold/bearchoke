package com.bearchoke.platform.webservice.config;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.Bus;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.jaxws.spring.EndpointDefinitionParser;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jws.WebService;
import javax.xml.ws.WebServiceProvider;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 1/11/15
 * Time: 2:56 PM
 * Responsibility:
 */
@Named
@Log4j2
public class JaxWsBeanPostProcessor implements BeanPostProcessor {
    @Inject
    ListableBeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (isWebService(bean)) {
            Bus bus = beanFactory.getBean(Bus.DEFAULT_BUS_ID, Bus.class);
            EndpointDefinitionParser.SpringEndpointImpl endpoint = new EndpointDefinitionParser.SpringEndpointImpl(bus, bean);

            WebService ws = bean.getClass().getAnnotation(WebService.class);
            endpoint.setAddress("/" + ws.serviceName());

            // capitalization is just a nice feature - totally optional
//            endpoint.setAddress("/" + StringUtils.capitalize(beanName));

            // adds ALL features registered / discovered by Spring
            Map<String, AbstractFeature> featureMap = beanFactory.getBeansOfType(AbstractFeature.class);
            endpoint.getFeatures().addAll(featureMap.values());

            // publish bean
            endpoint.publish();
        }

        return bean;
    }

    boolean isWebService(Object bean) {
        Class<?> beanClass = bean.getClass();
        return beanClass.getAnnotation(WebService.class) != null
                || beanClass.getAnnotation(WebServiceProvider.class) != null;
    }
}
