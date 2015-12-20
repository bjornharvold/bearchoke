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

angular.module("app").controller('RestController', function ($scope, $log, AuthRestangular) {
    $log.info("Rest Controller");

    $scope.greeting = {};

    $scope.restGreeting = function (version) {
        var headers = {};

        if (version) {
            $log.info("Retrieving REST results for version: " + version);
            headers = {"Accept":version};
        }

        AuthRestangular.one("greeting").get({}, headers).then(function(response) {
            $scope.greeting = response;
        });
    };

    $scope.restSecuredGreeting = function (version) {
        var headers = {};

        if (version) {
            $log.info("Retrieving secured REST results for version: " + version);
            headers = {"Accept":version};
        }

        AuthRestangular.one("secured/greeting").get({}, headers).then(function(response) {
            $scope.greeting = response;
        });
    };
});
