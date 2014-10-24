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


package com.bearchoke.platform.platform.base.security;

//~--- non-JDK imports --------------------------------------------------------

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.intercept.RunAsUserToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 12/20/10
 * Time: 1:26 AM
 * Responsibility:
 */
public class SpringSecurityHelper {
    private static final Logger log = LoggerFactory.getLogger(SpringSecurityHelper.class);

    public static void setAuthentication(Authentication authentication) {
        getSecurityContext().setAuthentication(authentication);
    }

    /**
     * Retrieves the Principal from the spring security context. Null if Principal is not logged in.
     *
     * @return Return value
     */
    public static UserDetails getUserDetails() {
        UserDetails result = null;
        SecurityContext sc     = SecurityContextHolder.getContext();

        if (sc != null) {
            Authentication authentication = sc.getAuthentication();

            if (authentication != null) {
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                if ((principal != null) && (principal instanceof UserDetails)) {
                    result = (UserDetails) principal;
                } else {
                    if (log.isWarnEnabled()) {
                        log.warn("No Principal present in Authentication");
                    }
                }
            } else {
                if (log.isWarnEnabled()) {
                    log.warn("No Authentication present in SecurityContext");
                }
            }
        } else {
            if (log.isWarnEnabled()) {
                log.warn("No SecurityContext present");
            }
        }

        return result;
    }

    public static Authentication getAuthentication() {
        Authentication result = null;
        SecurityContext sc     = SecurityContextHolder.getContext();

        if (sc != null) {
            result = sc.getAuthentication();
        }

        return result;
    }

    public static SecurityContext getSecurityContext() {
        return SecurityContextHolder.getContext();
    }


}
