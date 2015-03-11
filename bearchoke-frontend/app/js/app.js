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

// Create the Application module
angular.module("app", [
    "ngResource",
    "ngMessages",
    "ngSanitize",
    "ngTouch",
    "ngLodash",
    "ui.router",
    "ui.bootstrap",
    "restangular",
    "ngStorage",
    "pascalprecht.translate",
    "ezfb",
    "oitozero.ngSweetAlert"
])
        .run(function ($rootScope, $state, $stateParams, $modal, $log, $timeout, ApplicationContext, AnalyticsEventService, AuthRestangular) {

            $rootScope.alert = function (thing) {
                alert(thing);
            };

            $rootScope.$state = $state;
            $rootScope.$stateParams = $stateParams;
            $rootScope.isLoggedIn = ApplicationContext.isLoggedIn();
            $rootScope.hasRole = function(role) {
                var result = ApplicationContext.hasRole(role);

                return result;
            };

            // retrieve csrf token if not already exists
            if (ApplicationContext.getCsrfToken() === undefined) {
                AuthRestangular.one("csrf").get().then(function(response) {
                    //ApplicationContext.setCsrfToken(response);
                    $log.info("We don't have to do anything here. AuthRestangular will take care of it.");
                });
            }
        }
);
