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
package com.bearchoke.platform.server.web.websocket.service.impl;

import com.bearchoke.platform.server.web.websocket.service.Quote;
import com.bearchoke.platform.server.web.websocket.service.QuoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Services that push data to web socket channels need to be under the 'web' package as that is where the web socket configuration
 * is located. If it is below 'web', it won't find the messagingTemplate.
 */
@Slf4j
@Service
public class QuoteServiceImpl implements ApplicationListener<BrokerAvailabilityEvent>, QuoteService {

	private final MessageSendingOperations<String> messagingTemplate;

	private final StockQuoteGenerator quoteGenerator = new StockQuoteGenerator();

	private AtomicBoolean brokerAvailable = new AtomicBoolean();


	@Autowired
	public QuoteServiceImpl(MessageSendingOperations<String> messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	@Override
	public void onApplicationEvent(BrokerAvailabilityEvent event) {
		this.brokerAvailable.set(event.isBrokerAvailable());
	}

//	@Scheduled(fixedDelay=2000)
	public void sendQuotes() {
		for (Quote quote : this.quoteGenerator.generateQuotes()) {
			if (log.isTraceEnabled()) {
				log.trace("Sending quote " + quote);
			}
			if (this.brokerAvailable.get()) {
				this.messagingTemplate.convertAndSend("/topic/price.stock." + quote.getTicker(), quote);
			}
		}
	}

	private static class StockQuoteGenerator {

		private static final MathContext mathContext = new MathContext(2);

		private final Random random = new Random();

        private final List<Quote> quotes = new ArrayList<>(8);


		public StockQuoteGenerator() {
            quotes.add(new Quote("Citrix Systems, Inc.", "CTXS", 24.30));
            quotes.add(new Quote("Dell Inc.", "DELL", 13.44));
            quotes.add(new Quote("Microsoft", "MSFT", 34.15));
            quotes.add(new Quote("Oracle", "ORCL", 31.22));
            quotes.add(new Quote("Google", "GOOG", 39.31));
            quotes.add(new Quote("EMC Corp", "EMC", 45.01));
            quotes.add(new Quote("VMWare Inc.", "VMW", 95.11));
            quotes.add(new Quote("Red Hat Inc.", "RHT", 58.30));
		}

		public List<Quote> generateQuotes() {

            for (int i = 0; i < quotes.size(); i++) {
                Quote quote = quotes.get(i);
                quote.setPrice(getNewPrice(quote.getPrice()));
            }

			return quotes;
		}

		private double getNewPrice(double currentPrice) {
			BigDecimal seedPrice = new BigDecimal(currentPrice, mathContext);
			double range = seedPrice.multiply(new BigDecimal(0.02)).doubleValue();
			BigDecimal priceChange = new BigDecimal(String.valueOf(this.random.nextDouble() * range), mathContext);

            return seedPrice.add(priceChange).doubleValue();
		}

	}

}
