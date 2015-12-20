/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.server.common.web.controller;

import java.util.Date;

/**
*
* @author Gunnar Hillert
* @since 1.0
*
*/
public class ErrorMessage {

	private final Date timestamp;
	private final int status;
	private final String exception;
	private final String message;

	public ErrorMessage(Date timestamp, int status, String exception,
                        String message) {
		super();
		this.timestamp = timestamp;
		this.status = status;
		this.exception = exception;
		this.message = message;
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @return the exception
	 */
	public String getException() {
		return exception;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

}
