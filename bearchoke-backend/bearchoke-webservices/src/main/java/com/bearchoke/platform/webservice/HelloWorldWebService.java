package com.bearchoke.platform.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * Created by Bjorn Harvold
 * Date: 1/11/15
 * Time: 3:53 PM
 * Responsibility:
 */
@WebService(
        name = "HelloWorldService",
        targetNamespace = "com.bearchoke.platform.webservice"
)
public interface HelloWorldWebService {

//    @RequestWrapper(targetNamespace="http://www.bearchoke.com/types", className="java.lang.String")
//    @ResponseWrapper(targetNamespace="http://www.bearchoke.com/types", className="java.lang.String")
    @WebMethod(operationName = "hello")
    @WebResult(targetNamespace="http://www.bearchoke.com/types",
            name="helloResponse")
    public String sayHi(@WebParam(name = "name") String name) throws WebServiceException;
}
