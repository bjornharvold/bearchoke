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

package com.bearchoke.platform.api.user.dto;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
*
* @author Gunnar Hillert
* @since 1.0
*
*/
public class AuthenticationToken {
	@Getter
	private final String username;

	@Getter
	private final String name;

	@Getter
	private final String firstName;

	@Getter
	private final String lastName;

	@Getter
	private final String profilePictureUrl;

	@Getter
	private final Map<String, Boolean> roles;

	public AuthenticationToken(String username, String name, String firstName, String lastName, String profilePictureUrl, Map<String, Boolean> roles) {

		Map<String, Boolean> mapOfRoles = new ConcurrentHashMap<>();
		for (String k : roles.keySet()) {
			mapOfRoles.put(k, roles.get(k));
		}

		this.roles = mapOfRoles;
		this.username = username;
		this.name = name;
		this.firstName = firstName;
		this.lastName = lastName;
		this.profilePictureUrl = profilePictureUrl;
	}

	public AuthenticationToken(String username, Map<String, Boolean> roles) {

		Map<String, Boolean> mapOfRoles = new ConcurrentHashMap<>();
		for (String k : roles.keySet()) {
			mapOfRoles.put(k, roles.get(k));
		}

		this.roles = mapOfRoles;
		this.username = username;
		this.name = null;
		this.firstName = null;
		this.lastName = null;
		this.profilePictureUrl = null;
	}

	public static void main(String[] args) {
		String name = "Bjorn Harvold";

		System.out.println("|" + StringUtils.substringBefore(name, " ") + "|");
	}

}