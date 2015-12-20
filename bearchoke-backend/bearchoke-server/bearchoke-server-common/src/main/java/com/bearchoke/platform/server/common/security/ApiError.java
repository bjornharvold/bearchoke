/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.server.common.security;

import lombok.Data;

/**
 * Created by Bjorn Harvold
 * Date: 9/22/14
 * Time: 9:35 PM
 * Responsibility:
 */
@Data
public class ApiError {
    private String url;
    private int status;
    private String message;

    public ApiError(String url, int status, String message) {
        this.url = url;
        this.status = status;
        this.message = message;
    }
}
