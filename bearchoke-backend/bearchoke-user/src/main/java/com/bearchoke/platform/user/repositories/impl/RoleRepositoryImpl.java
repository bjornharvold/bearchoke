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

package com.bearchoke.platform.user.repositories.impl;

import com.bearchoke.platform.user.document.Role;
import com.bearchoke.platform.user.repositories.RoleRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.BasicMongoPersistentEntity;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.support.MappingMongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by Bjorn Harvold
 * Date: 1/9/14
 * Time: 8:05 PM
 * Responsibility:
 */
@Repository("roleRepository")
public class RoleRepositoryImpl extends SimpleMongoRepository<Role, ObjectId> implements RoleRepository {

    @Autowired
    public RoleRepositoryImpl(MongoTemplate mongoTemplate) {
        super(new MappingMongoEntityInformation<>(new BasicMongoPersistentEntity<>(ClassTypeInformation.from(Role.class))), mongoTemplate);

    }

    @Override
    public Role findByName(String name) {
        Role result = null;

        Query q = query(where("name").is(name));

        List<Role> list = getMongoOperations().find(q, Role.class);

        if (list != null && !list.isEmpty()) {
            result = list.get(0);
        }

        return result;
    }
}
