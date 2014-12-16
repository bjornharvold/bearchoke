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

package com.bearchoke.platform.mongo.document;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;

/**
 * Created by Bjorn Harvold
 * Date: 7/8/11
 * Time: 5:05 PM
 * Responsibility:
 */
public abstract class AbstractDocument {
    /** ID */
    @Id
    private ObjectId id;

    /** Created date */
    private DateTime createdDate;

    /** Last update */
    private DateTime lastUpdate;

    public AbstractDocument() {

    }

    public AbstractDocument(AbstractDocument doc) {
        id = doc.id;
        createdDate = doc.createdDate;
        lastUpdate = doc.lastUpdate;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }

    public DateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(DateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getIdAsString() {
        String result = null;

        if (id != null) {
            result = id.toString();
        }

        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("createdDate", createdDate)
                .append("lastUpdate", lastUpdate)
                .toString();
    }
}
