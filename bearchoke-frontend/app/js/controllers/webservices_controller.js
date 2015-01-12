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

angular.module("app").controller('WebServicesController', function ($scope, $log, HelloWorldFactory, SweetAlert) {
    $log.info('WebServicesController');

    $scope.firstName = "";

    $scope.submitted = false;
    $scope.submit = function() {
        $scope.submitted = true;

        if ($scope.helloWorldForm.$valid) {
            $log.debug("Sending name to SOAP service: " + $scope.firstName);
            HelloWorldFactory.hello(onHelloSuccess, onHelloFailure, $scope.firstName);
        }
    };

    $scope.interacted = function(field) {
        return $scope.submitted || field.$dirty;
    };

    var onHelloSuccess = function (data) {
        $log.debug('Hello success');
        SweetAlert.success("Success", data._helloResponse);
    };

    var onHelloFailure = function(data) {
        $log.debug('Hello failure');
        SweetAlert.error("Failure", data);
    };
});
