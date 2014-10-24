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

package com.bearchoke.platform.inmemory.security;

/**
 * Created by Bjorn Harvold
 * Date: 1/7/14
 * Time: 11:36 PM
 * Responsibility:
 */

import com.bearchoke.platform.platform.base.security.UserDetailsWithId;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Simple little User model.
 * Just stores the user's id for simplicity.
 * @author Keith Donald
 */
public final class User extends org.springframework.security.core.userdetails.User implements UserDetailsWithId {

    private final String id;

    public User(String userId, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = userId;
    }

    public User(String userId, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = userId;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getIdAsString() {
        return id;
    }
}

