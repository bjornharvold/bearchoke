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

angular.module("app").factory("MessageFactory", function($q, $timeout, configuration, lodash) {
    // default service values
    var service = {};
    service.SOCKET_URL = configuration.websocketBaseUrl;
    service.RECONNECT_TIMEOUT = 30000;

    var listener = $q.defer();
    var socket = {
        client: null,
        stomp: null
    };
    var messageIds = [];

    var MessageFactory = function(topic, broker) {
        service.TOPIC = topic;
        service.BROKER = broker;
    };

    MessageFactory.prototype.receive = function() {
        return listener.promise;
    };

    MessageFactory.prototype.send = function(message) {
        var id = Math.floor(Math.random() * 1000000);
        socket.stomp.send(service.BROKER, {
            priority: 9
        }, JSON.stringify({
            message: message,
            id: id
        }));
        messageIds.push(id);
    };

    var reconnect = function() {
        $timeout(function() {
            initialize();
        }, this.RECONNECT_TIMEOUT);
    };

    var getMessage = function(data) {
        var message = JSON.parse(data);
        var out = {};
        out.message = message.message;
        out.time = new Date(message.time);
        if (lodash.contains(messageIds, message.id)) {
            out.self = true;
            messageIds = lodash.remove(messageIds, message.id);
        }
        return out;
    };

    var startListener = function() {
        socket.stomp.subscribe(service.TOPIC, function(data) {
            listener.notify(getMessage(data.body));
        });
    };

    var initialize = function() {
        socket.client = new SockJS(service.SOCKET_URL);
        socket.stomp = Stomp.over(socket.client);
        socket.stomp.connect({}, startListener);
        socket.stomp.onclose = reconnect;
    };

    initialize();

    return MessageFactory;
});