/*
 * Copyright (c) 2015. Traveliko
 */

package com.bearchoke.platform.server.frontend.web.controller;

import com.bearchoke.platform.api.user.dto.Principal;
import com.bearchoke.platform.api.user.enums.Gender;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 1/4/15
 * Time: 12:13 PM
 * Responsibility:
 */
public abstract class AbstractControllerTest {
    private static final String PASSWORD = "password";
    private static final List<GrantedAuthority> adminCredentials = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    private static final List<GrantedAuthority> userCredentials = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    private static final List<GrantedAuthority> managerCredentials = Collections.singletonList(new SimpleGrantedAuthority("ROLE_MANAGER"));
    private static final Principal administrator = new Principal("id1", "id1", "administrator@bearchoke.com", "Bjorn Harvold", "Bjorn", "Harvold", "/img/profile/god.png", Gender.Male, PASSWORD, adminCredentials, true, true, true, true);
    private static final Principal manager = new Principal("id2", "id2", "manager@bearchoke.com", "Jack Clayborne", "Jack", "Clayborne", "/img/profile/jack.png", Gender.Male, PASSWORD, managerCredentials, true, true, true, true);
    private static final Principal user = new Principal("id3", "id3", "user@bearchoke.com", "Tom Boy", "Tom", "Boy", "/img/profile/tomboy.png", Gender.Male, PASSWORD, userCredentials, true, true, true, true);

    @Inject
    private ObjectMapper objectMapper;

    protected static RequestPostProcessor regularUser() {
        return SecurityMockMvcRequestPostProcessors.user(user);
    }

    protected static RequestPostProcessor adminUser() {
        return SecurityMockMvcRequestPostProcessors.user(administrator);
    }

    protected static RequestPostProcessor anonymousUser() {
        return SecurityMockMvcRequestPostProcessors.user("anonymous");
    }

    protected static RequestPostProcessor managerUser() {
        return SecurityMockMvcRequestPostProcessors.user(manager);
    }
//
//    protected static RequestPostProcessor regularUserAuthentication() {
//        return SecurityMockMvcRequestPostProcessors.authentication(new TestingAuthenticationToken(user, PASSWORD, userCredentials));
//    }
//
//    protected static RequestPostProcessor adminUserAuthentication() {
//        return SecurityMockMvcRequestPostProcessors.authentication(new TestingAuthenticationToken(administrator, PASSWORD, adminCredentials));
//    }
//
//    protected static RequestPostProcessor managerUserAuthentication() {
//        return SecurityMockMvcRequestPostProcessors.authentication(new TestingAuthenticationToken(manager, PASSWORD, managerCredentials));
//    }

    protected byte[] convertObjectToJsonBytes(Object object) throws IOException {

        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return objectMapper.writeValueAsBytes(object);
    }

    protected String convertObjectToJsonString(Object object) throws IOException {

        return objectMapper.writeValueAsString(object);
    }

    protected String createStringWithLength(int length) {
        StringBuilder builder = new StringBuilder();

        for (int index = 0; index < length; index++) {
            builder.append("a");
        }

        return builder.toString();
    }
}
