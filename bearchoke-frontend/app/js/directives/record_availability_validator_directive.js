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

angular.module("app").directive('recordAvailabilityValidator', function ($http) {

    return {
        require: 'ngModel',
        link: function (scope, element, attrs, ngModel) {
            var apiUrl = attrs.recordAvailabilityValidator;

            function setAsLoading(bool) {
                ngModel.$setValidity('recordLoading', !bool);
            }

            function setAsAvailable(bool) {
                ngModel.$setValidity('recordAvailable', bool);
            }

            ngModel.$parsers.push(function (value) {
                if (!value || value.length < 4) {
                    return;
                }

                setAsLoading(true);
                setAsAvailable(false);

                $http.get(apiUrl, {v: value})
                        .success(function () {
                            setAsLoading(false);
                            setAsAvailable(true);
                        })
                        .error(function () {
                            setAsLoading(false);
                            setAsAvailable(false);
                        });

                return value;
            });
        }
    };

});