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

angular.module("app").factory('AuthenticationFactory', function ($rootScope, $state, $log, AuthRestangular, $localStorage, ApplicationContext, eventConstants) {

    var self = {

        login: function (username, password, success, error) {

            // authenticate with the server
            AuthRestangular.one('authenticate').customPOST({username: username, password: password}).then(function(data) {

                // fire off successful login event
                $rootScope.$emit(eventConstants.authentication, {action: 'Logged in'});

                self.getUser(success, error);
            }, function() {
                $log.info("auth failure");
                self.clearAuth();

                if (error) {
                    error("Could not verify email and password.  Please try again");
                }
            });

        },

        logout: function (success) {
            $log.info("logout");
            var promise = AuthRestangular.one('logout').get();

            promise.then(function(data) {
                $log.info("server logout success for user");
            }, function() {
                $log.info("server logout failure");
            });

            // clear out everything on the front-end side related to the user session
            self.clearAuth();

            $rootScope.$emit(eventConstants.logout);

            if (success) {
                success();
            }
        },

        remember: function (success, error) {
            var authToken = ApplicationContext.getAuthToken();
            if (authToken) {
                $log.info("Checking for remember me user with token: " + authToken);
                // Update the Auth headers
                AuthRestangular.setDefaultHeaders(ApplicationContext.getHeaders());

                self.getUser(success, error);
            }
        },

        resetPassword: function(email) {
            var promise = AuthRestangular.all('password/reset').post({ email: email });
            return promise;
        },

        getUser: function (success, error) {
            // Get the logged in user information
//            $log.debug(ApplicationContext.getHeaders());
            $log.debug("Attempting to retrieve user");

            AuthRestangular.one('secured/user').get().then(function (data) {
                $log.debug('Retrieved user successfully');
//                $log.debug(data);
                ApplicationContext.setUser({username: data.username, roles: data.roles});

                if (success) {
                    // fire off an identify user
                    $rootScope.$emit(eventConstants.identify, {id: data.username});

                    // dispatch login success event
                    success(data);
                }
            }, function (err) {
                $log.info("get user auth failure");
                if (err.status === 401) {
                   $log.debug("Clearing out old auth token");
                    self.clearAuth();
                }

                $state.go("home");

                if (error) {
                    error("Could not verify email and password.  Please try again", err);
                }
            });
        },

        clearAuth: function() {
            // Reset the values
            ApplicationContext.clear();

            // Reset the AuthRestangular headers
            AuthRestangular.setDefaultHeaders(ApplicationContext.getHeaders());
        }
    };

    //
    // EVENTS
    //
    $rootScope.$on("event.login.unauthorized", function() {
        $log.info("Caught unauthorized event, redirect to login");
        self.clearAuth();
        $state.go("home");
    });

    return self;
});
