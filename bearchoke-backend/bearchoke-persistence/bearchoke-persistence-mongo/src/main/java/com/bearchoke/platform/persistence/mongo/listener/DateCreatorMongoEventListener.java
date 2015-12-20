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

package com.bearchoke.platform.persistence.mongo.listener;

import com.bearchoke.platform.persistence.mongo.document.AbstractDocument;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;

import java.time.LocalDateTime;

/**
 * Created by Bjorn Harvold
 * Date: 10/28/11
 * Time: 12:38 PM
 * Responsibility:
 */
public class DateCreatorMongoEventListener extends AbstractMongoEventListener<AbstractDocument> {

    @Override
    public void onBeforeConvert(BeforeConvertEvent<AbstractDocument> event) {
        super.onBeforeConvert(event);

        AbstractDocument document = event.getSource();

        LocalDateTime d = LocalDateTime.now();
        // set created date if necessary
        if (document.getCreatedDate() == null) {
            document.setCreatedDate(d);
        }

        // update last updated date
        document.setLastUpdate(d);
    }

}
