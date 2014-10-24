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
angular.module("app", ["ngResource", "ngMessages", "ngSanitize", "ngTouch", "ngLodash", "ui.router", "ui.bootstrap", "restangular", "ngStorage", "pascalprecht.translate"])
        .run(function ($rootScope, $state, $stateParams, $modal, $log, $timeout, AuthenticationFactory, ApplicationContext) {
            // for login form in header.html
            $rootScope.credentials = {};

            $rootScope.alert = function (thing) {
                alert(thing);
            };

            //===========================LOGIN=========================
            $rootScope.login = function() {
                $rootScope.error = null;
                AuthenticationFactory.login($rootScope.credentials.username, $rootScope.credentials.password, onLoginSuccess, onLoginFailure);
            };

            $rootScope.logout = function() {
                AuthenticationFactory.logout($rootScope.credentials.username, $rootScope.credentials.password, onLogoutSuccess);
            };

            var onLoginSuccess = function () {
                // Close the modal
                $log.info('login success');

                $timeout(function() {
                    // Check for a pre-login state
                    var preLoginState = ApplicationContext.getPreLoginState();
                    if (preLoginState) {
                        $log.info('Found pre login state ' + preLoginState.toState);
                        $state.go(preLoginState.toState, preLoginState.toParams, {reload: true});
                    } else {
                        $state.reload();
                    }
                });
            };

            var onLogoutSuccess = function () {
                $log.info('logout success');

                $timeout(function() {
                    $state.go('home', {}, { reload: true });
                });
            };

            $rootScope.facebookLoginClick = function() {
                $rootScope.error = null;
                FacebookService.login().then(onLoginSuccess, onLoginFailure);
            };

            $rootScope.forgotPasswordClick = function() {
                $scope.$close();
                $state.go('forgot-password');
            };

            var onLoginFailure = function(message) {
                $rootScope.error = message;
            };

            var onRememberMeFailure = function(message) {
                $log.debug(message);
            };

            $rootScope.$state = $state;
            $rootScope.$stateParams = $stateParams;
            $rootScope.isLoggedIn = ApplicationContext.isLoggedIn();
            $rootScope.hasRole = function(role) {
                var result = ApplicationContext.hasRole(role);

                return result;
            };


//            $log.info("Is logged in: " + $rootScope.isLoggedIn);

            // Check for a cookie to login
            // If the cookie auth token is there, assume logged in while waiting for the getUser call
            AuthenticationFactory.remember(onLoginSuccess(), onRememberMeFailure);
            //===========================END LOGIN=========================
        }
);
