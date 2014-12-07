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

package com.bearchoke.platform.server.web.api;

import com.bearchoke.platform.api.user.ExtendedUserDetailsManager;
import com.bearchoke.platform.api.user.dto.UniqueResult;
import com.bearchoke.platform.server.web.ApplicationMediaType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserController {

    private final ExtendedUserDetailsManager extendedUserDetailsManager;

    @Autowired
    public UserController(ExtendedUserDetailsManager extendedUserDetailsManager) {
        this.extendedUserDetailsManager = extendedUserDetailsManager;
    }

    /**
     * Checks to see if email already exists in the system
     * @param email email
     * @return
     */
    @RequestMapping(value = "/api/user/uniqueemail", method = { RequestMethod.GET }, produces = ApplicationMediaType.APPLICATION_BEARCHOKE_V1_JSON_VALUE)
	public UniqueResult isEmailUnique(@RequestParam(value = "email", required = true) String email) {
        return new UniqueResult(!extendedUserDetailsManager.emailExists(email));
	}

    /**
     * Checks to see if username already exists in the system
     * @param username username
     * @return
     */
    @RequestMapping(value = "/api/user/uniqueusername", method = { RequestMethod.GET }, produces = ApplicationMediaType.APPLICATION_BEARCHOKE_V1_JSON_VALUE)
	public UniqueResult isUsernameUnique(@RequestParam(value = "username", required = true)String username) {
        return new UniqueResult(!extendedUserDetailsManager.userExists(username));
	}
}
