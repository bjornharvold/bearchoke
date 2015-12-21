/*
 * Copyright (c) 2015. Traveliko
 */

package com.bearchoke.platform.api.user.dto;

import com.bearchoke.platform.api.user.UserDetailsExtended;
import com.bearchoke.platform.api.user.enums.Gender;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by Bjorn Harvold
 * Date: 5/13/15
 * Time: 17:18
 * Responsibility:
 */
@Data
public class Principal implements UserDetailsExtended, Serializable {
    private static final long serialVersionUID = -7648823938529412385L;
    private String id;
    private String userIdentifier;
    private String username;
    private String name;
    private String firstName;
    private String lastName;
    private String password;
    private Gender gender;
    private String profilePictureUrl;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;
    
    public Principal(){}
    
    public Principal(String id, String userIdentifier, String username, String name, String firstName, String lastName, String profilePictureUrl, Gender gender,
                     String password, Collection<? extends GrantedAuthority> authorities, boolean isAccountNonExpired,
                     boolean isAccountNonLocked, boolean isCredentialsNonExpired, boolean isEnabled) {
        this.id = id;
        this.userIdentifier = userIdentifier;
        this.username = username;
        this.authorities = authorities;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePictureUrl = profilePictureUrl;
        this.password = password;
        this.gender = gender;
    }

}
