//
// Definitions for schema: com.bearchoke.platform.webservice
//  http://dev.bearchoke.com:8080/services/HelloWorldService?wsdl#types2
//
//
// Constructor for XML Schema item {com.bearchoke.platform.webservice}hello
//
function com_bearchoke_platform_webservice_hello () {
    this.typeMarker = 'com_bearchoke_platform_webservice_hello';
    this._name = null;
}

//
// accessor is com_bearchoke_platform_webservice_hello.prototype.getName
// element get for name
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for name
// setter function is is com_bearchoke_platform_webservice_hello.prototype.setName
//
function com_bearchoke_platform_webservice_hello_getName() { return this._name;}

com_bearchoke_platform_webservice_hello.prototype.getName = com_bearchoke_platform_webservice_hello_getName;

function com_bearchoke_platform_webservice_hello_setName(value) { this._name = value;}

com_bearchoke_platform_webservice_hello.prototype.setName = com_bearchoke_platform_webservice_hello_setName;
//
// Serialize {com.bearchoke.platform.webservice}hello
//
function com_bearchoke_platform_webservice_hello_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    // block for local variables
    {
     if (this._name != null) {
      xml = xml + '<name>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._name);
      xml = xml + '</name>';
     }
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

com_bearchoke_platform_webservice_hello.prototype.serialize = com_bearchoke_platform_webservice_hello_serialize;

function com_bearchoke_platform_webservice_hello_deserialize (cxfjsutils, element) {
    var newobject = new com_bearchoke_platform_webservice_hello();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing name');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'name')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setName(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    return newobject;
}

//
// Constructor for XML Schema item {com.bearchoke.platform.webservice}helloResponse
//
function com_bearchoke_platform_webservice_helloResponse () {
    this.typeMarker = 'com_bearchoke_platform_webservice_helloResponse';
    this._helloResponse = null;
}

//
// accessor is com_bearchoke_platform_webservice_helloResponse.prototype.getHelloResponse
// element get for helloResponse
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for helloResponse
// setter function is is com_bearchoke_platform_webservice_helloResponse.prototype.setHelloResponse
//
function com_bearchoke_platform_webservice_helloResponse_getHelloResponse() { return this._helloResponse;}

com_bearchoke_platform_webservice_helloResponse.prototype.getHelloResponse = com_bearchoke_platform_webservice_helloResponse_getHelloResponse;

function com_bearchoke_platform_webservice_helloResponse_setHelloResponse(value) { this._helloResponse = value;}

com_bearchoke_platform_webservice_helloResponse.prototype.setHelloResponse = com_bearchoke_platform_webservice_helloResponse_setHelloResponse;
//
// Serialize {com.bearchoke.platform.webservice}helloResponse
//
function com_bearchoke_platform_webservice_helloResponse_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     xml = xml + ' ';
     xml = xml + 'xmlns:jns0=\'http://www.bearchoke.com/types\' ';
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    // block for local variables
    {
     if (this._helloResponse != null) {
      xml = xml + '<jns0:helloResponse>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._helloResponse);
      xml = xml + '</jns0:helloResponse>';
     }
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

com_bearchoke_platform_webservice_helloResponse.prototype.serialize = com_bearchoke_platform_webservice_helloResponse_serialize;

function com_bearchoke_platform_webservice_helloResponse_deserialize (cxfjsutils, element) {
    var newobject = new com_bearchoke_platform_webservice_helloResponse();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing helloResponse');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, 'http://www.bearchoke.com/types', 'helloResponse')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setHelloResponse(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    return newobject;
}

//
// Constructor for XML Schema item {com.bearchoke.platform.webservice}WebServiceException
//
function com_bearchoke_platform_webservice_WebServiceException () {
    this.typeMarker = 'com_bearchoke_platform_webservice_WebServiceException';
    this._message = null;
}

//
// accessor is com_bearchoke_platform_webservice_WebServiceException.prototype.getMessage
// element get for message
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for message
// setter function is is com_bearchoke_platform_webservice_WebServiceException.prototype.setMessage
//
function com_bearchoke_platform_webservice_WebServiceException_getMessage() { return this._message;}

com_bearchoke_platform_webservice_WebServiceException.prototype.getMessage = com_bearchoke_platform_webservice_WebServiceException_getMessage;

function com_bearchoke_platform_webservice_WebServiceException_setMessage(value) { this._message = value;}

com_bearchoke_platform_webservice_WebServiceException.prototype.setMessage = com_bearchoke_platform_webservice_WebServiceException_setMessage;
//
// Serialize {com.bearchoke.platform.webservice}WebServiceException
//
function com_bearchoke_platform_webservice_WebServiceException_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     xml = xml + ' ';
     xml = xml + 'xmlns:jns0=\'http://www.bearchoke.com/types\' ';
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    // block for local variables
    {
     if (this._message != null) {
      xml = xml + '<message>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._message);
      xml = xml + '</message>';
     }
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

com_bearchoke_platform_webservice_WebServiceException.prototype.serialize = com_bearchoke_platform_webservice_WebServiceException_serialize;

function com_bearchoke_platform_webservice_WebServiceException_deserialize (cxfjsutils, element) {
    var newobject = new com_bearchoke_platform_webservice_WebServiceException();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing message');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'message')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setMessage(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    return newobject;
}

//
// Definitions for schema: http://www.bearchoke.com/types
//  http://dev.bearchoke.com:8080/services/HelloWorldService?wsdl#types1
//
//
// Definitions for service: {com.bearchoke.platform.webservice}HelloWorldService
//

// Javascript for {com.bearchoke.platform.webservice}HelloWorldService

function com_bearchoke_platform_webservice_HelloWorldService () {
    this.jsutils = new CxfApacheOrgUtil();
    this.jsutils.interfaceObject = this;
    this.synchronous = false;
    this.url = null;
    this.client = null;
    this.response = null;
    this.globalElementSerializers = [];
    this.globalElementDeserializers = [];
    this.globalElementSerializers['{com.bearchoke.platform.webservice}hello'] = com_bearchoke_platform_webservice_hello_serialize;
    this.globalElementDeserializers['{com.bearchoke.platform.webservice}hello'] = com_bearchoke_platform_webservice_hello_deserialize;
    this.globalElementSerializers['{com.bearchoke.platform.webservice}helloResponse'] = com_bearchoke_platform_webservice_helloResponse_serialize;
    this.globalElementDeserializers['{com.bearchoke.platform.webservice}helloResponse'] = com_bearchoke_platform_webservice_helloResponse_deserialize;
    this.globalElementSerializers['{com.bearchoke.platform.webservice}WebServiceException'] = com_bearchoke_platform_webservice_WebServiceException_serialize;
    this.globalElementDeserializers['{com.bearchoke.platform.webservice}WebServiceException'] = com_bearchoke_platform_webservice_WebServiceException_deserialize;
    this.globalElementSerializers['{com.bearchoke.platform.webservice}hello'] = com_bearchoke_platform_webservice_hello_serialize;
    this.globalElementDeserializers['{com.bearchoke.platform.webservice}hello'] = com_bearchoke_platform_webservice_hello_deserialize;
    this.globalElementSerializers['{com.bearchoke.platform.webservice}helloResponse'] = com_bearchoke_platform_webservice_helloResponse_serialize;
    this.globalElementDeserializers['{com.bearchoke.platform.webservice}helloResponse'] = com_bearchoke_platform_webservice_helloResponse_deserialize;
    this.globalElementSerializers['{com.bearchoke.platform.webservice}WebServiceException'] = com_bearchoke_platform_webservice_WebServiceException_serialize;
    this.globalElementDeserializers['{com.bearchoke.platform.webservice}WebServiceException'] = com_bearchoke_platform_webservice_WebServiceException_deserialize;
}

function com_bearchoke_platform_webservice_hello_op_onsuccess(client, responseXml) {
    if (client.user_onsuccess) {
     var responseObject = null;
     var element = responseXml.documentElement;
     this.jsutils.trace('responseXml: ' + this.jsutils.traceElementName(element));
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('first element child: ' + this.jsutils.traceElementName(element));
     while (!this.jsutils.isNodeNamedNS(element, 'http://schemas.xmlsoap.org/soap/envelope/', 'Body')) {
      element = this.jsutils.getNextElementSibling(element);
      if (element == null) {
       throw 'No env:Body in message.'
      }
     }
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('part element: ' + this.jsutils.traceElementName(element));
     this.jsutils.trace('calling com_bearchoke_platform_webservice_helloResponse_deserializeResponse');
     responseObject = com_bearchoke_platform_webservice_helloResponse_deserializeResponse(this.jsutils, element);
     client.user_onsuccess(responseObject);
    }
}

com_bearchoke_platform_webservice_HelloWorldService.prototype.hello_onsuccess = com_bearchoke_platform_webservice_hello_op_onsuccess;

function com_bearchoke_platform_webservice_hello_op_onerror(client) {
    if (client.user_onerror) {
     var httpStatus;
     var httpStatusText;
     try {
      httpStatus = client.req.status;
      httpStatusText = client.req.statusText;
     } catch(e) {
      httpStatus = -1;
      httpStatusText = 'Error opening connection to server';
     }
     client.user_onerror(httpStatus, httpStatusText);
    }
}

com_bearchoke_platform_webservice_HelloWorldService.prototype.hello_onerror = com_bearchoke_platform_webservice_hello_op_onerror;

//
// Operation {com.bearchoke.platform.webservice}hello
// - bare operation. Parameters:
// - com_bearchoke_platform_webservice_hello
//
function com_bearchoke_platform_webservice_hello_op(successCallback, errorCallback, hello) {
    this.client = new CxfApacheOrgClient(this.jsutils);
    var xml = null;
    var args = new Array(1);
    args[0] = hello;
    xml = this.hello_serializeInput(this.jsutils, args);
    this.client.user_onsuccess = successCallback;
    this.client.user_onerror = errorCallback;
    var closureThis = this;
    this.client.onsuccess = function(client, responseXml) { closureThis.hello_onsuccess(client, responseXml); };
    this.client.onerror = function(client) { closureThis.hello_onerror(client); };
    var requestHeaders = [];
    requestHeaders['SOAPAction'] = '';
    this.jsutils.trace('synchronous = ' + this.synchronous);
    this.client.request(this.url, xml, null, this.synchronous, requestHeaders);
}

com_bearchoke_platform_webservice_HelloWorldService.prototype.hello = com_bearchoke_platform_webservice_hello_op;

function com_bearchoke_platform_webservice_hello_serializeInput(cxfjsutils, args) {

    var xml;
    xml = cxfjsutils.beginSoap11Message("xmlns:jns0='http://www.bearchoke.com/types' xmlns:jns1='com.bearchoke.platform.webservice' ");
    // block for local variables
    {
     xml = xml + args[0].serialize(cxfjsutils, 'jns1:hello', null);
    }
    xml = xml + cxfjsutils.endSoap11Message();
    return xml;
}

com_bearchoke_platform_webservice_HelloWorldService.prototype.hello_serializeInput = com_bearchoke_platform_webservice_hello_serializeInput;

function com_bearchoke_platform_webservice_helloResponse_deserializeResponse(cxfjsutils, partElement) {
    var returnObject = com_bearchoke_platform_webservice_helloResponse_deserialize (cxfjsutils, partElement);

    return returnObject;
}
function com_bearchoke_platform_webservice_HelloWorldService_com_bearchoke_platform_webservice_HelloWorldServicePort () {
  this.url = 'http://dev.bearchoke.com:8080/services/HelloWorldService';
}
com_bearchoke_platform_webservice_HelloWorldService_com_bearchoke_platform_webservice_HelloWorldServicePort.prototype = new com_bearchoke_platform_webservice_HelloWorldService;
