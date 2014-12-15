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

angular.module("app").controller('LoginController', function ($scope, $state, $q, $timeout, $log, ApplicationContext, AuthenticationFactory, ezfb, SweetAlert) {
    $log.debug('LoginController');

    // for login form in header.html
    $scope.credentials = {};

    //===========================LOGIN=========================
    $scope.login = function() {
        $log.debug('Logging in with username: ' + $scope.credentials.username + ', password: ' + $scope.credentials.password);
        AuthenticationFactory.login($scope.credentials.username, $scope.credentials.password, onLoginSuccess, onLoginFailure);
    };

    $scope.logout = function() {
        AuthenticationFactory.logout($scope.credentials.username, $scope.credentials.password, onLogoutSuccess);
    };

    var onLoginSuccess = function () {
        $log.debug('login success');

        $timeout(function() {
            // Check for a pre-login state
            var preLoginState = ApplicationContext.getPreLoginState();
            if (preLoginState) {
                $log.debug('Found pre login state ' + preLoginState.toState);
                $state.go(preLoginState.toState, preLoginState.toParams, {reload: true});
            } else {
                $state.reload();
            }
        });
    };

    var onLogoutSuccess = function () {
        $log.debug('logout success');

        $timeout(function() {
            $state.go('home', {}, { reload: true });
        });
    };

    $scope.facebookLogin = function() {
        $log.debug('Logging in with Facebook...');


    };

    $scope.forgotPasswordClick = function() {
        $scope.$close();
        $state.go('forgot-password');
    };

    var onLoginFailure = function(message) {
        SweetAlert.error("Incorrect credentials", message);
    };

    var onRememberMeFailure = function(message) {
        $log.debug(message);
    };

    /**
     * Update loginStatus result
     */
    function updateLoginStatus (more) {
        ezfb.getLoginStatus(function (res) {
            $scope.loginStatus = res;

            (more || angular.noop)();
        });
    }

    /**
     * Update api('/me') result
     */
    function updateApiMe () {
        ezfb.api('/me', function (res) {
            SweetAlert.swal(res);
        });
    }

//            $log.info("Is logged in: " + $rootScope.isLoggedIn);

    // Check for a cookie to login
    // If the cookie auth token is there, assume logged in while waiting for the getUser call
    AuthenticationFactory.remember(onLoginSuccess, onRememberMeFailure);
    //===========================END LOGIN=========================
});
