/*
 * Copyright (c) 2015. Bearchoke
 */
package com.bearchoke.platform.server.common.web.interceptor;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * A geo-location that can be plotted on a map.
 * @author Keith Donald
 */
public final class Location {
	
	private final Double latitude;
	
	private final Double longitude;

	public Location(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * The lat value.
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * The long value.
	 */
	public Double getLongitude() {
		return longitude;
	}
	
	public int hashCode() { 
		return latitude.hashCode() + longitude.hashCode() * 29;
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof Location)) {
			return false;
		}
		Location l = (Location) o;
		return latitude.equals(l.latitude) && l.longitude.equals(l.longitude);
	}

	/**
	 * Get the location of the user associated with the current request, if resolvable.
	 */
	public static Location getCurrentLocation() {
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		return attributes != null ? (Location) attributes.getAttribute(UserLocationHandlerInterceptor.USER_LOCATION_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST) : null;
	}
	
}
