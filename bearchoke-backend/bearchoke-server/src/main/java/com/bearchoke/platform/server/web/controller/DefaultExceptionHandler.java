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

package com.bearchoke.platform.server.web.controller;

import com.bearchoke.platform.server.web.ApplicationMediaType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 8/27/14
 * Time: 1:44 AM
 * Responsibility:
 */
@ControllerAdvice
@Slf4j
public class DefaultExceptionHandler {

    @RequestMapping(produces = {ApplicationMediaType.APPLICATION_BEARCHOKE_V1_JSON_VALUE, ApplicationMediaType.APPLICATION_BEARCHOKE_V2_JSON_VALUE})
    @ExceptionHandler({MissingServletRequestParameterException.class,
            UnsatisfiedServletRequestParameterException.class,
            HttpRequestMethodNotSupportedException.class,
            ServletRequestBindingException.class
    })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorMessage handleRequestException(Exception ex) {
        log.error("Http Status: " + HttpStatus.BAD_REQUEST + " : " + ex.getMessage(), ex);
        return new ErrorMessage(new Date(), HttpStatus.BAD_REQUEST.value(), ex.getClass().getName(), ex.getMessage());
    }

    @RequestMapping(produces = {ApplicationMediaType.APPLICATION_BEARCHOKE_V1_JSON_VALUE, ApplicationMediaType.APPLICATION_BEARCHOKE_V2_JSON_VALUE})
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(value = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public @ResponseBody Map<String, Object> handleUnsupportedMediaTypeException(HttpMediaTypeNotSupportedException ex) throws IOException {
        log.error("Http Status: " + HttpStatus.UNSUPPORTED_MEDIA_TYPE + " : " + ex.getMessage(), ex);
        Map<String, Object>  map = new HashMap<>(3);
        map.put("error", "Unsupported Media Type");
        map.put("cause", ex.getLocalizedMessage());
        map.put("supported", ex.getSupportedMediaTypes());
        return map;
    }

    @RequestMapping(produces = {ApplicationMediaType.APPLICATION_BEARCHOKE_V1_JSON_VALUE, ApplicationMediaType.APPLICATION_BEARCHOKE_V2_JSON_VALUE})
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody
    Map<String, Object> handleUncaughtException(Exception ex) throws IOException {
        log.error("Http Status: " + HttpStatus.INTERNAL_SERVER_ERROR + " : " + ex.getMessage(), ex);
        Map<String, Object> map = new HashMap<>(2);
        map.put("error", "Unknown Error");
        if (ex.getCause() != null) {
            map.put("cause", ex.getCause().getMessage());
        } else {
            map.put("cause", ex.getMessage());
        }
        return map;
    }

    @ExceptionHandler(value = AuthenticationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorMessage handleAuthenticationException(Exception ex) {
        log.error("Http Status: " + HttpStatus.FORBIDDEN + " : " + ex.getMessage(), ex);
        return new ErrorMessage(new Date(), HttpStatus.FORBIDDEN.value(), ex.getClass().getName(), ex.getMessage());
    }

}
