/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.api.user;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsExtended extends UserDetails {

    String getId();

    String getUserIdentifier();

    String getUsername();

    String getName();

    String getFirstName();

    String getLastName();

    String getProfilePictureUrl();
}
