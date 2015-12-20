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

angular.module("app").controller('MailChimpController', function ($log, $scope, SitewideNewsletterService, SweetAlert) {
    $scope.mailchimp = {};
    $scope.submitted = false;

    $scope.submit = function() {
        $scope.submitted = true;

        if ($scope.newsletterForm.$valid) {
            $log.debug("Subscribe to newsletter form is valid. Submitting...");
            SitewideNewsletterService.subscribeToNewsletter($scope.mailchimp, onNewsletterSubscriptionSuccess, onNewsletterSubscriptionFailure);
        }
    };

    $scope.interacted = function(field) {
        return $scope.submitted || field.$dirty;
    };

    var onNewsletterSubscriptionFailure = function(message) {
        SweetAlert.error("Failure", message);
    };

    var onNewsletterSubscriptionSuccess = function(message) {
        SweetAlert.success("Thank you!", message);
    };
});
