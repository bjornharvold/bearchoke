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

package com.bearchoke.platform.user.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
@Slf4j
public class PreAuthUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

	@Inject
	private PreAuthenticatedTokenCacheService preAuthenticatedTokenCacheService;
	
	@Override
	public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
		String xAuthToken = (String) token.getPrincipal();
		User user = preAuthenticatedTokenCacheService.getFromCache(xAuthToken);

		if (user == null) {
            throw new UsernameNotFoundException("Pre authenticated token not found : " + xAuthToken);
        } else {
            if (log.isTraceEnabled()) {
                log.trace("Retrieved user from cache: " + user.getUsername());
            }

            // we want to update the expiration date on this key because the user is actively using it
            preAuthenticatedTokenCacheService.updateExpiration(xAuthToken);
        }

		return user;
	}

}
