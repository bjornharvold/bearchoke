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

angular.module("app").factory('MailChimpFactory', function ($log, $resource) {
    // MailChimp settings
    var username;
    var dc;
    var u;
    var id;

    var MailChimpFactory = function (username, u, dc, id) {
        $log.debug("Creating MailChimpFactory using username: " + username + ", u: " + u + ", dc: " + dc + ", id: " + id);
        this.username = username;
        this.u = u;
        this.dc = dc;
        this.id = id;
    };

    MailChimpFactory.prototype.subscribeToNewsletter = function (user, success, failure) {

        if (user.email === 'undefined') {
            var errorMsg = "Email is required";
            $log.error(errorMsg);
            failure(errorMsg);
        }

        var url = '//' + this.username + '.' + this.dc + '.list-manage.com/subscribe/post-json';
        var params = {
            'EMAIL': user.email,
            'FNAME': user.fname,
            'LNAME': user.lname,
            'c': 'JSON_CALLBACK',
            'u': this.u,
            'id': this.id
        };

        var actions = {
            'save': {
                method: 'jsonp'
            }
        };

        $log.debug('Subscribing user to newsletter...');

        var mailChimpSubscriptionResource = $resource(url, params, actions);

        mailChimpSubscriptionResource.save(
                // Successfully sent data to MailChimp.
                function (response) {

                    // Mailchimp returned an error.
                    if (response.result === 'error') {
                        if (response.msg) {
                            // Remove error numbers, if any.
                            var errorMessageParts = response.msg.split(' - ');
                            if (errorMessageParts.length > 1) {
                                errorMessageParts.shift(); // Remove the error number
                            }
                            failure(errorMessageParts.join(' '));
                        } else {
                            failure('Sorry! An unknown error occured.');
                        }
                    }
                    // MailChimp returns a success.
                    else if (response.result === 'success') {
                        success(response.msg);
                    }
                },

                // Error sending data to MailChimp
                function (error) {
                    var errorMsg = 'MailChimp Error: %o' + error;
                    $log.error(errorMsg);
                    failure(errorMsg);
                }
        );

    };

    return MailChimpFactory;
});
