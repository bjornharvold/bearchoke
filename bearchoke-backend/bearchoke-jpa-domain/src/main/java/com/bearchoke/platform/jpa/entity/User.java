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

package com.bearchoke.platform.jpa.entity;

import com.bearchoke.platform.jpa.domain.AbstractEntity;
import com.bearchoke.platform.platform.base.security.UserDetailsWithId;
import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Transient;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.UserProfile;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Bjorn Harvold
 * Date: 1/18/14
 * Time: 1:32 PM
 * Responsibility:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class User extends AbstractEntity implements UserDetails, UserDetailsWithId, Serializable {
    private static final long serialVersionUID = -2552772948365503444L;

    @Transient
    private final static EthernetAddress nic = EthernetAddress.fromInterface();

    @Transient
    private final static TimeBasedGenerator uuidGenerator = Generators.timeBasedGenerator(nic);


    //~--- fields -------------------------------------------------------------

    /**
     * Account Not Expired
     */
    private Boolean accountNonExpired = true;

    /**
     * Account not Locked
     */
    private Boolean accountNonLocked = true;

    /**
     * Enabled
     */
    private Boolean accountEnabled = true;

    /**
     * Credentials Non expired
     */
    private Boolean credentialsNonExpired = true;

    /**
     * Email
     */
    @NotNull
    @Column(unique = true)
    private String email;

    /**
     * Email
     */
    @NotNull
    @Column(unique = true)
    private String username;

    /**
     * Password
     */
    @NotNull
    private String password;

    /**
     * User roles
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Role> roles;

    /** First name */
    private String firstName;

    /** Last name */
    private String lastName;

    public User(UserProfile profile) {
        this.email = profile.getEmail();
        this.firstName = profile.getFirstName();
        this.lastName = profile.getLastName();
        this.username = profile.getUsername();
    }

    /**
     * Static method to generate a User Code value
     * @return
     */
    public static String generateUserCode() {
        return uuidGenerator.generate().toString();
    }

    //~--- fields -------------------------------------------------------------

    /**
     * Checksum that we can use to uniquely identify user with using something public like email
     * This will be overwritten by stored database user.userCode
     */
    private String userCode = generateUserCode();

    public void addRole(Role role) {
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }

        boolean dupe = false;

        for (Role existingRole : this.roles) {
            if (StringUtils.equals(existingRole.getUrlName(), role.getUrlName())) {
                dupe = true;
                break;
            }
        }

        if (!dupe) {
            this.roles.add(role);
        }
    }

    public void removeRole(Role role) {
        if (this.roles != null) {
            Role toRemove = null;

            for (Role existingRole : roles) {
                if (StringUtils.equals(role.getUrlName(), existingRole.getUrlName())) {
                    toRemove = existingRole;
                    break;
                }
            }

            if (toRemove != null) {
                this.roles.remove(toRemove);
            }
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> result = null;

        if (this.getRoles() != null && !this.getRoles().isEmpty()) {
            result = new ArrayList<>();

            for (Role ur : this.getRoles()) {
                for (Right right : ur.getRights()) {
                    result.add(new SimpleGrantedAuthority(right.getRight()));
                }
            }
        }

        return result;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.getAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.getAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.getCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.getAccountEnabled();
    }
}
