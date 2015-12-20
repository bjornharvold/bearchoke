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

angular.module("app").controller('QuoteController', function ($scope, $log, configuration, QuoteService) {
    $log.info("Quotes Controller");

    $scope.quotes = [];
    var quoteLookup = {};

    QuoteService.receiveQuote().then(null, null, function(quote) {
        if (quoteLookup.hasOwnProperty(quote.ticker)) {
            updatePrice(quoteLookup[quote.ticker], quote.price);
        } else {
            $scope.quotes.push(quote);
            quoteLookup[quote.ticker] = quote;
        }
    });

    var updatePrice = function (quote, newPrice) {
        var delta = (newPrice - quote.price).toFixed(2);
        quote.arrow = (delta < 0) ? '<i class="fa fa-chevron-down"></i>' : '<i class="fa fa-chevron-up"></i>';
        quote.change = (delta / quote.price * 100).toFixed(2);
        quote.price = newPrice;
    };
});
