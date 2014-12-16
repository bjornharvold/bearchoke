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

package com.bearchoke.platform.api.user;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 8/13/14
 * Time: 5:07 PM
 * Responsibility:
 */
public class RoleCreatedEvent {
    private RoleIdentifier roleId;
    private String name;
    private String urlName;
    private List<String> rights;

    public RoleCreatedEvent(RoleIdentifier roleId, String name, String urlName, List<String> rights) {
        this.roleId = roleId;
        this.name = name;
        this.urlName = urlName;
        this.rights = rights;
    }

    public RoleIdentifier getRoleId() {
        return roleId;
    }

    public String getName() {
        return name;
    }

    public String getUrlName() {
        return urlName;
    }

    public List<String> getRights() {
        return rights;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("roleId", roleId)
                .append("name", name)
                .append("urlName", urlName)
                .append("rights", rights)
                .toString();
    }
}
