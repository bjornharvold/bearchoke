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

package com.bearchoke.platform.server.frontend.web.controller;

import com.bearchoke.platform.server.common.ServerConstants;
import com.bearchoke.platform.server.frontend.FrontendWebApplicationInitializer;
import com.bearchoke.platform.server.frontend.web.support.client.StompMessageHandler;
import com.bearchoke.platform.server.frontend.web.support.client.StompSession;
import com.bearchoke.platform.server.frontend.web.support.client.WebSocketStompClient;
import com.bearchoke.platform.server.frontend.web.support.server.TomcatWebSocketTestServer;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.test.util.JsonPathExpectationsHelper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.SocketUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.fail;

@Log4j2
public class IntegrationQuotesWebSocketTest {

	private static int port;

	private static TomcatWebSocketTestServer server;

	private static SockJsClient sockJsClient;

	private final static WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

	@BeforeClass
	public static void setup() throws Exception {
		log.info("Setting up Quotes Web Socket Integration test....");
		port = SocketUtils.findAvailableTcpPort();

		server = new TomcatWebSocketTestServer(port);
		server.deployConfig(FrontendWebApplicationInitializer.class);
		server.start();

		loginAndSaveXAuthToken("harrymitchell", "HarryMitchell5!", headers);

		List<Transport> transports = new ArrayList<>();
		transports.add(new WebSocketTransport(new StandardWebSocketClient()));
		RestTemplateXhrTransport xhrTransport = new RestTemplateXhrTransport(new RestTemplate());
		xhrTransport.setRequestHeaders(headers);
		transports.add(xhrTransport);

		sockJsClient = new SockJsClient(transports);
        sockJsClient.setHttpHeaderNames("X-Auth-Token");
		log.info("Setup complete!");
	}

	private static void loginAndSaveXAuthToken(final String user, final String password,
											   final HttpHeaders headersToUpdate) {

		log.info("Authenticating user before subscribing to web socket");

		String url = "http://dev.bearchoke.com:" + port + "/api/authenticate";

		try {
			new RestTemplate().execute(url, HttpMethod.POST,

					request -> {
						MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
						map.add("username", user);
						map.add("password", password);
						new FormHttpMessageConverter().write(map, MediaType.APPLICATION_FORM_URLENCODED, request);
					},

					response -> {
						String xAuthToken = response.getHeaders().getFirst(ServerConstants.X_AUTH_TOKEN);
						log.info("Retrieved x-auth-token: " + xAuthToken);
						headersToUpdate.add(ServerConstants.X_AUTH_TOKEN, xAuthToken);
						return null;
					});
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}

	@AfterClass
	public static void teardown() throws Exception {
		log.info("Tearing down server after integration test complete");

		if (server != null) {
			try {
				server.undeployConfig();
			}
			catch (Throwable t) {
				log.error("Failed to undeploy application", t);
			}

			try {
				server.stop();
			}
			catch (Throwable t) {
				log.error("Failed to stop server", t);
			}
		}
	}


	@Test
	public void getQuotes() throws Exception {
		log.info("Testing getting stock quotes from QuoteService...");
		final CountDownLatch latch = new CountDownLatch(1);
		final AtomicReference<Throwable> failure = new AtomicReference<Throwable>();

		URI uri = new URI("ws://dev.bearchoke.com:" + port + "/ws");
		WebSocketStompClient stompClient = new WebSocketStompClient(uri, this.headers, sockJsClient);
		stompClient.setMessageConverter(new MappingJackson2MessageConverter());

		stompClient.connect(new StompMessageHandler() {

			private StompSession stompSession;

			@Override
			public void afterConnected(StompSession stompSession, StompHeaderAccessor headers) {
				String channel = "/topic/price.stock.*";
				log.info("Subscribing to channel: " + channel);
				stompSession.subscribe(channel, RandomStringUtils.randomAlphabetic(10));
				this.stompSession = stompSession;
			}

			@Override
			public void handleMessage(Message<byte[]> message) {
				StompHeaderAccessor headers = StompHeaderAccessor.wrap(message);
				if (!headers.getDestination().startsWith("/topic/price.stock.")) {
					 failure.set(new IllegalStateException("Unexpected message: " + message));
				}

				log.debug("Got \n" + new String(message.getPayload()));
				try {
					String json = new String(message.getPayload(), Charset.forName("UTF-8"));
					new JsonPathExpectationsHelper("$.company").assertValue(json, "Citrix Systems, Inc.");
					new JsonPathExpectationsHelper("$.ticker").assertValue(json, "CTXS");
				}
				catch (Throwable t) {
					failure.set(t);
				}
				finally {
					this.stompSession.disconnect();
					latch.countDown();
				}
			}

			@Override
			public void handleError(Message<byte[]> message) {
				failure.set(new Exception(new String(message.getPayload(), Charset.forName("UTF-8"))));
			}

			@Override
			public void handleReceipt(String receiptId) {}

			@Override
			public void afterDisconnected() {
				log.info("Successfully disconnected from Web Socket channel");
			}
		});

		if (failure.get() != null) {
			throw new AssertionError("", failure.get());
		}

		if (!latch.await(5, TimeUnit.SECONDS)) {
			fail("Quotes not received");
		}
	}

}
