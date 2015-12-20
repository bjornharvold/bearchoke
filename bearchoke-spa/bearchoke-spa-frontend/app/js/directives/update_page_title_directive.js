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

angular.module("app").directive('updateTitle', function ($rootScope, $timeout, $log, configuration, $translate) {
    return {
        link: function (scope, element) {
            var rawTitle;

            var listener = function (event, toState, toParams, fromState, fromParams) {
//                $log.info("State change success occurred");
                var title = "Unknown - Could not find translation";

                if (toState.data.title) {

//                    $log.info(toState.title);
                    rawTitle = toState.data.title;

                    $translate(rawTitle).then(function (translation) {
                        title = translation + " | " + configuration.siteName;

                        // Set asynchronously so page changes before title does
                        $timeout(function () {
                            element.text(title);
                        });
                    });
                }

            };

            var translateListener = function () {
//                $log.info("Translation change success occurred. Translating: " + title);
                var title = "Unknown - Could not find translation";

                if (rawTitle) {
                    $translate(rawTitle).then(function (translation) {
                        title = translation + " | " + configuration.siteName;

                        // Set asynchronously so page changes before title does
                        $timeout(function () {
                            element.text(title);
                        });
                    });
                }

            };

            // this event listens to page changes and updates the title accordingly
            $rootScope.$on('$stateChangeSuccess', listener);

            // this event listens to language changes and updates the title accordingly
            // NOTE: It is assumed that the page change listener has already been called and the raw page name has been retrieved from router_config.js
            $rootScope.$on('$translateChangeSuccess', translateListener);

        }
    };
});