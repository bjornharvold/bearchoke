package com.bearchoke.platform.webservice.impl;

import lombok.extern.slf4j.Slf4j;

import javax.jws.WebService;

/**
 * Created by Bjorn Harvold
 * Date: 1/11/15
 * Time: 3:17 PM
 * Responsibility:
 */
@WebService(
        name = "HelloWorldService",
        serviceName = "HelloWorldService",
        targetNamespace = "com.bearchoke.platform.webservice",
        endpointInterface = "com.bearchoke.platform.webservice.HelloWorldWebService"
)
@Slf4j
public class HelloWorldWebServiceImpl implements com.bearchoke.platform.webservice.HelloWorldWebService {

    public HelloWorldWebServiceImpl() {
        log.info("HelloWebService instantiated successfully");
    }

    public String sayHi(String name) {
        return "Hello " + name;
    }
}
