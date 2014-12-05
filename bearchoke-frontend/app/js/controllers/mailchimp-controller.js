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

angular.module("app").controller('MailchimpSubscriptionCtrl', function ($log, $resource, $scope) {
    // Handle clicks on the form submission.
    $scope.addSubscription = function (mailchimp) {
        var actions,
                MailChimpSubscription,
                params,
                url;

        // Create a resource for interacting with the MailChimp API
        url = '//' + mailchimp.username + '.' + mailchimp.dc + '.list-manage.com/subscribe/post-json';
        params = {
            'EMAIL': mailchimp.email,
            'FNAME': mailchimp.fname,
            'LNAME': mailchimp.lname,
            'c': 'JSON_CALLBACK',
            'u': mailchimp.u,
            'id': mailchimp.id
        };
        actions = {
            'save': {
                method: 'jsonp'
            }
        };
        MailChimpSubscription = $resource(url, params, actions);

        // Send subscriber data to MailChimp
        MailChimpSubscription.save(
                // Successfully sent data to MailChimp.
                function (response) {
                    // Define message containers.
                    mailchimp.errorMessage = '';
                    mailchimp.successMessage = '';

                    // Store the result from MailChimp
                    mailchimp.result = response.result;

                    // Mailchimp returned an error.
                    if (response.result === 'error') {
                        if (response.msg) {
                            // Remove error numbers, if any.
                            var errorMessageParts = response.msg.split(' - ');
                            if (errorMessageParts.length > 1) {
                                errorMessageParts.shift(); // Remove the error number
                            }
                            mailchimp.errorMessage = errorMessageParts.join(' ');
                        } else {
                            mailchimp.errorMessage = 'Sorry! An unknown error occured.';
                        }
                    }
                    // MailChimp returns a success.
                    else if (response.result === 'success') {
                        mailchimp.successMessage = response.msg;
                    }
                },

                // Error sending data to MailChimp
                function (error) {
                    $log.error('MailChimp Error: %o', error);
                }
        );
    };
});
