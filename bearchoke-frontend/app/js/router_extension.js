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

angular.module("app")
        .run(function ($rootScope, $state, $stateParams, $modal, $log, ApplicationContext, PageContext) {
            //
            // WEB CLIENT ROUTE HANDLING
            //

            // Watch for states requiring user to be authenticated
            // And track open modals by state
            var modalByStateName = {};

            $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
                $log.info('State Change: ' + toState.name);

                var handled = false;
                if (toState.data && toState.data.auth && !ApplicationContext.isLoggedIn()) {
                    $log.info('State requires authentication: ' + toState.name);
                    ApplicationContext.setPreLoginState({
                        toState: toState.name,
                        toParams: toParams
                    });

                    event.preventDefault();
                    handled = true;

                    // If the user is trying to go to a logged in state, and they either weren't on a state
                    // or are coming from a logged in state but have lost their authentication... show login
                    $state.go('home');
                } else if (toState.name !== 'home') {
                    $log.info('Clear pre login state: ' + toState.name);
                    ApplicationContext.setPreLoginState($rootScope.preLoginState);
                }

                // Launch the state as a modal if configured in ui_router.js
                // Prevent a modal for a state from being opened twice
                if (!handled && toState.data && toState.data.modal) {
                    if (!modalByStateName[toState.name]) {
                        $log.info('launch modal for state: ' + toState.name);
                        event.preventDefault();
                        handled = true;

                        // Close prior modals
                        if (toState.data.closePriorModal) {
                            _.each(_.keys(modalByStateName), function (key) {
                                if (_.has(modalByStateName, key)) {
                                    var modal = modalByStateName[key].close();
                                }
                            });
                        }

                        var modalConfig = {
                            templateUrl: toState.templateUrl,
                            controller: toState.controller,
                            resolve: {
                                '$modalState': function () {
                                    return toState;
                                }
                            }
                        };

                        if (typeof toState.data.modal === 'object') {
                            angular.extend(modalConfig, toState.data.modal);
                        }

                        // Open the modal and flag it as opened
                        var modal = $modal.open(modalConfig);
                        modalByStateName[toState.name] = modal;

                        // Regardless of the modal result, flag it as closed
                        modal.result.then(function () {
                            delete modalByStateName[toState.name];
                            if ($state.$current.name === '') {
                                $state.go('home');
                            }
                        }, function () {
                            delete modalByStateName[toState.name];
                            if ($state.$current.name === '') {
                                $state.go('home');
                            }
                        });
                    } else {
                        // Modal is already open for this state... cancel the state change
                        event.preventDefault();
                    }
                } else {
                    // Reset the Page
                    PageContext.reset();

                    // Check for page level definition
                    if (toState.data && toState.data.page) {

                        if (toState.data.page.meta) {
                            PageContext.addMeta(toState.data.page.meta);
                        }
                    }
                }
            });
        });