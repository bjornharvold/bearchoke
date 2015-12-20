/*
 * Copyright (c) 2015. Bearchoke
 */
package com.bearchoke.platform.server.common.web.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.format.datetime.standard.DateTimeContext;
import org.springframework.format.datetime.standard.DateTimeContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * Spring MVC Interceptor that sets the request's {@link org.joda.time.DateTimeZone} from a cookie.
 * Allows the client's timezone to be figured out by JavaScript, then submitted and applied on the server as the default timezone for the request.
 * Useful when you need to render dates in client local time.
 * @author Keith Donald
 */
@Component
public class DateTimeZoneHandlerInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		DateTimeContext context = new DateTimeContext();
		context.setTimeZone(getZoneId(request));
		DateTimeContextHolder.setDateTimeContext(context);
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if (modelAndView != null) {
			modelAndView.addObject("zoneId", DateTimeContextHolder.getDateTimeContext());
		}
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		DateTimeContextHolder.resetDateTimeContext();
	}

	// interna helpers
	
	private ZoneId getZoneId(HttpServletRequest request) {
        String offset = request.getHeader("TimeZone-Offset");

		ZoneOffset zoneOffset = getZoneOffset(offset);

		if (zoneOffset != null) {
			try {
				return zoneOffset.normalized();
			} catch (IllegalArgumentException e) {
                return ZoneId.systemDefault();
			}
		} else {
			return ZoneId.systemDefault();
		}
	}
	
	private ZoneOffset getZoneOffset(String offsetS) {

        // we expect the front-end to be sending a number over such as 420 (Indo time)

		if (StringUtils.isNotBlank(offsetS)) {
            // we get minutes from the header so we need to add 60 to get to seconds
            int offset = Integer.valueOf(offsetS) * 60;

			return ZoneOffset.ofTotalSeconds(offset);
		} else {
			return ZoneOffset.UTC;
		}
	}
}
