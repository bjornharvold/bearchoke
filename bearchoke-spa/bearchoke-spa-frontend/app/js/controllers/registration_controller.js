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

angular.module("app").controller('RegistrationController', function ($scope, $state, $log, AuthenticationFactory, SweetAlert) {
    $log.info('RegistrationController');

    $scope.user = {};

    $scope.submitted = false;
    $scope.submit = function() {
        $scope.submitted = true;

        if ($scope.userForm.$valid) {
            AuthenticationFactory.register($scope.user, onRegistrationSuccess, onRegistrationFailure);
        }
    };

    $scope.interacted = function(field) {
        return $scope.submitted || field.$dirty;
    };

    var onRegistrationSuccess = function () {
        $log.debug('Registration success');
        $state.go('home');
        SweetAlert.success("Congratulations!", "You can now sign in to your account.");
    };

    var onRegistrationFailure = function(message) {
        SweetAlert.error("Failure", message);
    };
});
