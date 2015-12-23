package com.bearchoke.platform.webservice.impl;

import lombok.extern.log4j.Log4j2;

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
@Log4j2
public class HelloWorldWebServiceImpl implements com.bearchoke.platform.webservice.HelloWorldWebService {

    public HelloWorldWebServiceImpl() {
        log.info("HelloWebService instantiated successfully");
    }

    public String sayHi(String name) {
        return "Hello " + name;
    }
}
