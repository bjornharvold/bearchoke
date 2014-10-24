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

angular.module("app").factory("ToDoFactory", function($q, $timeout, configuration, ApplicationContext) {
    // default service values
    var service = {};
    service.SOCKET_URL = configuration.websocketBaseUrl;
    service.RECONNECT_TIMEOUT = 30000;

    var listener = $q.defer();
    var socket = {
        client: null,
        stomp: null
    };

    var ToDoFactory = function(topic, broker) {
        service.TOPIC = topic;
        service.BROKER = broker;
    };

    ToDoFactory.prototype.receive = function() {
        return listener.promise;
    };

    ToDoFactory.prototype.send = function(todo) {
        var headers = {
            "X-Auth-Token": ApplicationContext.getAuthToken(),
            priority: 9
        };
        socket.stomp.send(service.BROKER, headers, angular.toJson(todo));
    };

    var reconnect = function() {
        $timeout(function() {
            initialize();
        }, this.RECONNECT_TIMEOUT);
    };

    var getMessage = function(data) {
        return JSON.parse(data);
    };

    var startListener = function() {
        var headers = {
            "X-Auth-Token": ApplicationContext.getAuthToken()
        };
        socket.stomp.subscribe(service.TOPIC, function(data) {
            listener.notify(getMessage(data.body));
        }, headers);
    };

    var initialize = function() {
        var headers = {
            "X-Auth-Token": ApplicationContext.getAuthToken()
        };
        socket.client = new SockJS(service.SOCKET_URL);
        socket.stomp = Stomp.over(socket.client);
        socket.stomp.connect(headers, startListener);
        socket.stomp.onclose = reconnect;
    };

    initialize();

    return ToDoFactory;
});