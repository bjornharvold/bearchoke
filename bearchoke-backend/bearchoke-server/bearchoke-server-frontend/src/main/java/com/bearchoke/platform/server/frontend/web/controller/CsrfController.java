package com.bearchoke.platform.server.frontend.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Bjorn Harvold
 * Date: 3/5/15
 * Time: 12:48 PM
 * Responsibility:
 */
@RestController
@Slf4j
public class CsrfController {

    @RequestMapping(value = "/api/csrf", method = RequestMethod.GET)
    public CsrfToken csrf(CsrfToken token) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("CSRF Token - Name: %s, Token: %s", token.getHeaderName(), token.getToken()));
        }
        return token;
    }
}
