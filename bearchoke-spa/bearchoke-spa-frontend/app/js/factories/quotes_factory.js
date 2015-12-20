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

angular.module("app").factory("QuoteFactory", function ($q, $log, $timeout, configuration, ApplicationContext, SweetAlert) {
    // default service values
    var service = {};
    service.SOCKET_URL = configuration.websocketBaseUrl;
    service.RECONNECT_TIMEOUT = 30000;

    var quoteListener = $q.defer();

    var socket = {
        client: null,
        stomp: null
    };

    var QuoteFactory = function(quote) {
        service.QUOTE = quote;
    };

    QuoteFactory.prototype.receiveQuote = function() {
        return quoteListener.promise;
    };

    var reconnect = function() {
        $timeout(function() {
            initialize();
        }, this.RECONNECT_TIMEOUT);
    };

    var getQuote = function(data) {
        return JSON.parse(data);
    };

    var startListener = function() {
        socket.stomp.subscribe(service.QUOTE, function(data) {
            quoteListener.notify(getQuote(data.body));
        });
    };

    var initialize = function() {
        socket.client = new SockJS(service.SOCKET_URL);
        socket.stomp = Stomp.over(socket.client);

        socket.stomp.connect({}, startListener);
        socket.stomp.onclose = reconnect;
    };

    // If you want CSRF enabled for web sockets
    function addHeaders() {
        var headers = {};
        if (ApplicationContext.getCsrfToken() === undefined) {
            SweetAlert.error("Missing CSRF token", "Make sure a csrf token is available.");
        } else {
            var headerName = ApplicationContext.getCsrfToken().headerName;
            var token = ApplicationContext.getCsrfToken().token;
            $log.info("CSRF header name: " + headerName + ", token: " + token);
            headers[headerName] = token;
        }

        return headers;
    }

    initialize();

    return QuoteFactory;
    
    
    
});
