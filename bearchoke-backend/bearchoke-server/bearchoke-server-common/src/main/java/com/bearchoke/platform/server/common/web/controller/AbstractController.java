/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.server.common.web.controller;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Bjorn Harvold
 * Date: 6/12/15
 * Time: 15:11
 * Responsibility:
 */
public class AbstractController {
    protected String getServerUrl(HttpServletRequest request) {
        String result;

        String origin = request.getHeader("Origin");
        String host = request.getHeader("Host");

        if (StringUtils.isBlank(origin)) {
            result = "http://" + host;
        } else {
            result = origin;
        }

        return result;
    }
}
