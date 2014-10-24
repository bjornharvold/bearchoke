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

package com.bearchoke.platform.mongo.repository;

import com.bearchoke.platform.mongo.document.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * Created by Bjorn Harvold
 * Date: 1/9/14
 * Time: 8:03 PM
 * Responsibility:
 */
public interface MongoDbUserRepository extends MongoRepository<User, ObjectId>, MongoDbUserRepositoryCustom {

    @Query("{ 'id' : ?0 }")
    User findUserById(ObjectId userId);

    @Query("{ 'srnm' : ?0 }")
    User findUserByUsername(String username);

    @Query("{ 'ml' : ?0 }")
    User findUserByEmail(String email);


}
