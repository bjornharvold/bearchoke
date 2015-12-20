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

angular.module("app").service("AnalyticsEventService", function($rootScope, $log, eventConstants, MixPanelFactory) {
    // this is a service with no methods - it's only here to catch events and pass them to MixPanel

    $rootScope.$on(eventConstants.authentication, function(event, data) {
        $log.debug("Caught authentication success event");
        MixPanelFactory.login(data, data.loginType);
    });

    $rootScope.$on(eventConstants.registration, function(event, data) {
        $log.debug("Caught registration success event");
        MixPanelFactory.signup(data.user, data.registerType);
    });
});