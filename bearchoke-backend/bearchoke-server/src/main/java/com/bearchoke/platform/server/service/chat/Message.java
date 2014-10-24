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

import java.util.Date;

/**
 * Created by Bjorn Harvold
 * Date: 10/8/14
 * Time: 9:16 AM
 * Responsibility:
 */
@Data
public class Message {
    private String message;
    private int id;
    private Date time;

    public Message() {

    }

    public Message(int id, String message) {
        this.id = id;
        this.message = message;
        this.time = new Date();
    }

}
