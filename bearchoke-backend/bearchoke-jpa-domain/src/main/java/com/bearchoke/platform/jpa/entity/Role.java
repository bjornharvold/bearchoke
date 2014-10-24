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

package com.bearchoke.platform.jpa.entity;

//~--- non-JDK imports --------------------------------------------------------

import com.bearchoke.platform.jpa.domain.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.Set;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 6/19/11
 * Time: 1:04 PM
 * Responsibility:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Role extends AbstractEntity implements Serializable {

    /** Field description */
    private static final long serialVersionUID = -3836008580458749250L;

    //~--- fields -------------------------------------------------------------

    /** Rights */
    @OneToMany
    private Set<Right> rights;

    /** Name */
    private String name;

    /** Field description */
    private String urlName;

}
