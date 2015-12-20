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

/**
 * Created with IntelliJ IDEA.
 * User: Bjorn
 * Date: 11/15/13
 * Time: 2:21 PM
 */
angular.module("app").config(function ($stateProvider, $locationProvider, $urlRouterProvider, $httpProvider) {

    $locationProvider.html5Mode(true);

    $httpProvider.defaults.withCredentials = true;

    // Un-matched URLs redirect to /
    $urlRouterProvider.otherwise("/");

    $stateProvider
        //
        // Top-level Layouts
        //

            .state('static-content-layout', {
                abstract: 'true',
                templateUrl: "layout/static_content_layout.html",
                controller: "StaticContentLayoutController"
            })

        //
        // Home, Login, Registration
        //
            .state('home', {
                parent: "static-content-layout",
                url: "/",
                templateUrl: "home.html",
                controller: 'HomeController',
                data: {
                    auth: false,
                    title: "HOME",
                    meta: {
                        'description': 'Test description',
                        'author': 'Bjorn Harvold',
                        'keywords': 'test,cool,bearchoke'
                    }
                }
            })
            .state('chat', {
                parent: "static-content-layout",
                url: "/ui/chat",
                templateUrl: "chat.html",
                controller: 'ChatController',
                data: {
                    auth: false,
                    title: "CHAT",
                    meta: {
                        'description': 'Test description',
                        'author': 'Bjorn Harvold',
                        'keywords': 'test,cool,bearchoke'
                    }
                }
            })
            .state('quotes', {
                parent: "static-content-layout",
                url: "/ui/quotes",
                templateUrl: "quotes.html",
                controller: 'QuoteController',
                data: {
                    auth: false,
                    title: "QUOTES",
                    meta: {
                        'description': 'Serves up quotes',
                        'author': 'Bjorn Harvold',
                        'keywords': 'quote'
                    }
                }
            })
            .state('todo', {
                parent: "static-content-layout",
                url: "/ui/todo",
                templateUrl: "todo.html",
                controller: 'ToDoController',
                data: {
                    auth: true,
                    title: "TODO",
                    meta: {
                        'description': 'Creates todos',
                        'author': 'Bjorn Harvold',
                        'keywords': 'todos'
                    }
                }
            })
            .state('localization', {
                parent: "static-content-layout",
                url: "/ui/localization",
                templateUrl: "localization.html",
                controller: 'LocalizationController',
                data: {
                    auth: false,
                    title: "LOCALIZATION",
                    meta: {
                        'description': 'Test description',
                        'author': 'Bjorn Harvold',
                        'keywords': 'test,cool,bearchoke'
                    }
                }
            })
            .state('rest', {
                parent: "static-content-layout",
                url: "/ui/rest",
                templateUrl: "rest.html",
                controller: 'RestController',
                data: {
                    auth: false,
                    title: "REST",
                    meta: {
                        'description': 'Test description',
                        'author': 'Bjorn Harvold',
                        'keywords': 'test,cool,bearchoke'
                    }
                }
            })
            .state('register', {
                parent: "static-content-layout",
                url: "/ui/register",
                templateUrl: "registration.html",
                controller: 'RegistrationController',
                data: {
                    auth: false,
                    title: "REGISTER",
                    meta: {
                        'description': 'Site registration',
                        'author': 'Bjorn Harvold',
                        'keywords': 'registration,bearchoke'
                    }
                }
            })
            .state('newsletter', {
                parent: "static-content-layout",
                url: "/ui/newsletter",
                templateUrl: "newsletter.html",
                controller: 'MailChimpController',
                data: {
                    auth: false,
                    title: "NEWSLETTER",
                    meta: {
                        'description': 'Subscribe to our newsletter',
                        'author': 'Bjorn Harvold',
                        'keywords': 'newsletter,subscription,bearchoke'
                    }
                }
            })
            .state('webservices', {
                parent: "static-content-layout",
                url: "/ui/webservices",
                templateUrl: "webservices.html",
                controller: 'WebServicesController',
                data: {
                    auth: false,
                    title: "WEBSERVICES",
                    meta: {
                        'description': 'Web Services SOAP test',
                        'author': 'Bjorn Harvold',
                        'keywords': 'webservices,soap,bearchoke'
                    }
                }
            })
            .state('faq', {
                parent: "static-content-layout",
                url: '/ui/faq',
                templateUrl: 'static/faq.html',
                data: {
                    auth: false,
                    title: "FAQ",
                    meta: {
                        'description': 'Frequently asked questions',
                        'author': 'Bjorn Harvold',
                        'keywords': 'faq'
                    }
                }
            })
            .state('contact', {
                parent: "static-content-layout",
                url: '/ui/contact',
                templateUrl: 'static/contact.html',
                data: {
                    auth: false,
                    title: "CONTACT",
                    meta: {
                        'description': 'Contact Us',
                        'author': 'Bjorn Harvold',
                        'keywords': 'contact us'
                    }
                }
            })

    ;

});