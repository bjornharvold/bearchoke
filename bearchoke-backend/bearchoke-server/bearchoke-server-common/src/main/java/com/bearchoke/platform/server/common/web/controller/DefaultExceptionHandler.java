/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.server.common.web.controller;

import com.bearchoke.platform.server.common.ApplicationMediaType;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
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
        log.error("Http Status: " + HttpStatus.UNSUPPORTED_MEDIA_TYPE + " : " + ex.getMessage());
        Map<String, Object>  map = new HashMap<>(3);
        map.put("error", "Unsupported Media Type");
        map.put("cause", ex.getLocalizedMessage());
        map.put("supported", ex.getSupportedMediaTypes());
        return map;
    }

    @RequestMapping(produces = {ApplicationMediaType.APPLICATION_BEARCHOKE_V1_JSON_VALUE, ApplicationMediaType.APPLICATION_BEARCHOKE_V2_JSON_VALUE})
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody Map<String, Object> handleUncaughtException(Exception ex) throws IOException {
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
        log.error("Http Status: " + HttpStatus.FORBIDDEN + " : " + ex.getMessage());
        return new ErrorMessage(new Date(), HttpStatus.FORBIDDEN.value(), ex.getClass().getName(), ex.getMessage());
    }

}
