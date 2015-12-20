/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.server.common.web.argumentresolver;

import org.springframework.core.MethodParameter;
import org.springframework.format.datetime.standard.DateTimeContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.time.ZoneId;

/**
 * Created by Bjorn Harvold
 * Date: 10/12/14
 * Time: 1:28 PM
 * Responsibility:
 */
public class DateTimeZoneHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return ZoneId.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        return DateTimeContextHolder.getDateTimeContext().getTimeZone();
    }
}
