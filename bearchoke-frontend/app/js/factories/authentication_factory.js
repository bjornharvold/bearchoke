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

angular.module("app").factory('AuthenticationFactory', function ($rootScope, $state, $log, AuthRestangular, $localStorage, ApplicationContext, eventConstants, ezfb) {

    var self = {

        login: function (username, password, success, error) {

            $log.debug('Attempting to authenticate...');

            // authenticate with the server
            AuthRestangular.one('authenticate').customPOST({username: username, password: password}).then(function(data) {

                // fire off successful login event
                $rootScope.$emit(eventConstants.authentication, {username: username});

                self.getUser(success, error);
            }, function(data) {
                $log.error("Authentication failure: " + data.statusText);
                self.clearAuth();

                if (error) {
                    error("Could not verify email and password.");
                }
            });

        },

        register: function (user, success, error) {
            $log.debug('Registering new user....');
            //$log.debug(user);

            // authenticate with the server
            AuthRestangular.one('user/register').customPOST(user).then(function(data) {

                // fire off successful login event
                $rootScope.$emit(eventConstants.registration, {username: user.username});

                // fire off success event
                success();
            }, function(data) {
                $log.error("Registration failure: " + data.statusText);

                if (error) {
                    error("There was a problem with your registration.");
                }
            });
        },

        logout: function (success) {
            $log.debug("logging out user");
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
                //$log.debug(data);
                ApplicationContext.setUser({username: data.username, roles: data.roles});

                if (success) {
                    // fire off an identify user
                    $rootScope.$emit(eventConstants.identify, {id: data.username});

                    // dispatch login success event
                    success(data);
                }
            }, function (err) {
                $log.error("getUser() failure: " + err.statusText);
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
        },

        facebookLogin: function (success, error) {
            ezfb.login(function(res) {
                if (res.authResponse) {
                    updateLoginStatus(updateApiMe, success, error);
                }
            }, {scope: 'email'});
        }
    };

    /**
     * Update loginStatus result
     */
    function updateLoginStatus(more, success, error) {
        ezfb.getLoginStatus(function (res) {
            $log.debug(res);

            (more || angular.noop)(success, error);
        });
    }

    /**
     * Retrieve facebook user's object and sync it with the server
     */
    function updateApiMe(success, error) {
        ezfb.api('/me', function (res) {
            // authenticate with the server
            AuthRestangular.one('facebook').customPOST(res).then(function(data) {

                // fire off successful login event
                $rootScope.$emit(eventConstants.facebook, {email: res.email});

                self.getUser(success, error);
            }, function(data) {
                $log.error("Failure failure: " + data.statusText);

                if (error) {
                    error("There was a problem with Facebook.");
                }
            });
        });
    }

    //
    // EVENTS
    //
    $rootScope.$on("event.login.unauthorized", function() {
        $log.warn("Caught unauthorized event, redirect to login");
        self.clearAuth();
        $state.go("home");
    });

    return self;
});
