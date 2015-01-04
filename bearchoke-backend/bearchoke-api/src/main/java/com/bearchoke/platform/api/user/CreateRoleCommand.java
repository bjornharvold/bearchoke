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

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;
import org.axonframework.common.Assert;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 8/13/14
 * Time: 5:07 PM
 * Responsibility: 
 */
public class CreateRoleCommand {

    @TargetAggregateIdentifier
    private final RoleIdentifier roleId;

    @NotNull
    private final String name;

    @NotNull
    private final List<String> rights;

    public CreateRoleCommand(RoleIdentifier roleId, String name, List<String> rights) {

        Assert.notNull(roleId, "Identifier cannot be null");
        Assert.notNull(name, "Name cannot be null");
        Assert.notNull(rights, "Rights cannot be null");

        this.roleId = roleId;
        this.name = name;
        this.rights = rights;
    }

    public RoleIdentifier getRoleId() {
        return roleId;
    }

    public String getName() {
        return name;
    }

    public List<String> getRights() {
        return rights;
    }
}
