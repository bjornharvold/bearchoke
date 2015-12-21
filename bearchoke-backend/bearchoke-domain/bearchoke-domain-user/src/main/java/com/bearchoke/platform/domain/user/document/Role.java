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

package com.bearchoke.platform.domain.user.document;

//~--- non-JDK imports --------------------------------------------------------

import com.bearchoke.platform.api.user.event.RoleCreatedEvent;
import com.bearchoke.platform.persistence.mongo.document.AbstractDocument;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 6/19/11
 * Time: 1:04 PM
 * Responsibility:
 */
@Data
@Document
@EqualsAndHashCode(callSuper = false)
public class Role extends AbstractDocument implements Serializable {

    /** Field description */
    private static final long serialVersionUID = -3836008580458749250L;

    //~--- fields -------------------------------------------------------------

    /** Rights */
    private List<String> rights = new ArrayList<String>();

    /** Name */
    private String name;

    public Role() {}

    public Role(String name, List<String> rights) {
        this.name = name;
        this.rights = rights;
    }

    public Role(RoleCreatedEvent event) {
        this.name = event.getName();
        this.rights = event.getRights();
    }
}
