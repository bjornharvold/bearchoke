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

package com.bearchoke.platform.api.user.dto;

import lombok.Data;

/**
 * Created by Bjorn Harvold
 * Date: 12/30/14
 * Time: 2:59 PM
 * Responsibility:
 */
@Data
public class FacebookUserDto {
    private String email;
    private String first_name;
    private String last_name;
    private String name;
    private String gender;
    private String id;
    private String locale;
    private String timezone;
    private Boolean verified;
    private String link;
    private String[] roles;
    private String password;
    private FacebookProfilePicture picture;
}
