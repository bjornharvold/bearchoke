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
    private DateTime cdt;

    /** Last update */
    private DateTime ldt;

    public AbstractDocument() {

    }

    public AbstractDocument(AbstractDocument doc) {
        id = doc.id;
        cdt = doc.cdt;
        ldt = doc.ldt;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public DateTime getCdt() {
        return cdt;
    }

    public void setCdt(DateTime cdt) {
        this.cdt = cdt;
    }

    public DateTime getLdt() {
        return ldt;
    }

    public void setLdt(DateTime ldt) {
        this.ldt = ldt;
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
        return getIdAsString();
    }


}
