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

package com.bearchoke.platform.user.document;

//~--- non-JDK imports --------------------------------------------------------

import com.bearchoke.platform.api.user.UserAccount;
import com.bearchoke.platform.api.user.UserCreatedEvent;
import com.bearchoke.platform.mongo.document.AbstractDocument;
import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 6/17/11
 * Time: 4:28 PM
 * Responsibility:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Document
public class User extends AbstractDocument implements UserDetails, UserAccount, Serializable {

    /**
     * Field description
     */
    private static final long serialVersionUID = -5850171832971023139L;

    @Transient
    private final static EthernetAddress nic = EthernetAddress.fromInterface();

    @Transient
    private final static TimeBasedGenerator uuidGenerator = Generators.timeBasedGenerator(nic);

    //~--- fields -------------------------------------------------------------

    /**
     * Account Not Expired
     */
    private Boolean nonExpired = true;

    /**
     * Account not Locked
     */
    private Boolean nonLocked = true;

    /**
     * Enabled
     */
    private Boolean enabled = true;

    /**
     * Credentials Non expired
     */
    private Boolean credentialsNonExpired = true;

    /**
     * Email
     */
    @NotNull
    @Indexed(unique = true)
    private String email;

    /**
     * Email
     */
    @NotNull
    @Indexed(unique = true)
    private String username;

    /**
     * Password
     */
    @NotNull
    private String password;

    /**
     * User roles
     */
    private List<Role> roles = new ArrayList<Role>();

    /** First name */
    private String firstName;

    /** Last name */
    private String lastName;

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
     * This will be overwritten by stored database user.cd
     */
    private String cd = generateUserCode();

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs ...
     */
    public User() {
    }

    public User(UserCreatedEvent event) {
        this.email = event.getEmail();
        this.username = event.getUsername();
        this.firstName = event.getFirstName();
        this.lastName = event.getLastName();
        this.password = event.getPassword();
        this.cd = event.getUserId().toString();
    }

    //~--- methods ------------------------------------------------------------

    public void addRole(Role role) {
        if (this.roles == null) {
            this.roles = new ArrayList<>();
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
                for (String right : ur.getRights()) {
                    result.add(new SimpleGrantedAuthority(right));
                }
            }
        }

        return result;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.getNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.getNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.getCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.getEnabled();
    }

    @Override
    public String getName() {
        return this.firstName + " " + this.lastName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .append("nonExpired", nonExpired)
                .append("nonLocked", nonLocked)
                .append("enabled", enabled)
                .append("credentialsNonExpired", credentialsNonExpired)
                .append("email", email)
                .append("username", username)
                .append("password", password)
                .append("roles", roles)
                .append("firstName", firstName)
                .append("lastName", lastName)
                .append("cd", cd)
                .toString();
    }
}
