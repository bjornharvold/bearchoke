/*
 * Copyright (c) 2015. Traveliko
 */

package com.bearchoke.platform.api.user;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Object used to obtain information about an available UserAccount
 *
 * @author Jettro Coenradie
 */
public interface UserDetailsExtended extends UserDetails {

    String getId();

    String getUserIdentifier();

    String getUsername();

    String getName();

    String getFirstName();

    String getLastName();

    String getProfilePictureUrl();
}
