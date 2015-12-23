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

package com.bearchoke.platform.server.frontend.web.controller;

import com.bearchoke.platform.server.frontend.service.chat.Message;
import com.bearchoke.platform.server.frontend.service.chat.OutputMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.Date;

/**
 * Created by Bjorn Harvold
 * <p/>
 * Date: 1/5/14
 * <p/>
 * Time: 3:32 PM
 * <p/>
 * Responsibility:
 */

@Controller
@Log4j2
public class MessageController {

    /**
     * WebSocket example
     * @param message
     * @return
     */
    @MessageMapping("chat")
    @SendTo("/topic/message") // if @SendTo is not specified, the default url will be '/topic/' + message mapping url
    public OutputMessage sendMessage(Message message) {
        log.info(message.toString());
        return new OutputMessage(message, new Date());
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}

