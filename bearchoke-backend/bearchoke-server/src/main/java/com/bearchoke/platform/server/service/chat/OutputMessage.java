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

package com.bearchoke.platform.server.service.chat;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Created by Bjorn Harvold
 * Date: 10/8/14
 * Time: 9:17 AM
 * Responsibility:
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OutputMessage extends Message {
    private Date time;

    public OutputMessage() {
        super();
    }

    public OutputMessage(Message original, Date time) {
        super(original.getId(), original.getMessage());
        this.time = time;
    }
}
