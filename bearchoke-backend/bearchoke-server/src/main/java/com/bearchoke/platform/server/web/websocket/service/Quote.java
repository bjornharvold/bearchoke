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
package com.bearchoke.platform.server.web.websocket.service;

import lombok.Data;

@Data
public class Quote {

	private final String company;

	private final String ticker;

	private double price;

	private final long updateTime;

	public Quote(String company, String ticker, double price) {
		this.company = company;
		this.ticker = ticker;
		this.price = price;
		this.updateTime = System.currentTimeMillis();
	}

	@Override
	public String toString() {
		return "PortfolioPosition [company=" + this.company + ", ticker=" + this.ticker
				+ ", price=" + this.price + "]";
	}

}
