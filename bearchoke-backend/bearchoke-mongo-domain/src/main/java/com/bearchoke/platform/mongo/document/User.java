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

package com.bearchoke.platform.mongo.document;

//~--- non-JDK imports --------------------------------------------------------

import com.bearchoke.platform.platform.base.security.UserDetailsWithId;
import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.UserProfile;

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
public class User extends AbstractDocument implements UserDetails, UserDetailsWithId, Serializable {

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
    private Boolean nxprd = true;

    /**
     * Account not Locked
     */
    private Boolean nlckd = true;

    /**
     * Enabled
     */
    private Boolean nbld = true;

    /**
     * Credentials Non expired
     */
    private Boolean crdnxprd = true;

    /**
     * Email
     */
    @NotNull
    @Indexed(unique = true)
    private String ml;

    /**
     * Email
     */
    @NotNull
    @Indexed(unique = true)
    private String srnm;

    /**
     * Password
     */
    @NotNull
    private String psswrd;

    /**
     * User roles
     */
    private List<Role> rls = new ArrayList<Role>();

    /** First name */
    private String frstnm;

    /** Last name */
    private String lstnm;

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

    /**
     * Constructs ...
     *
     * @param profile profile
     */
    public User(UserProfile profile) {
        this.ml = profile.getEmail();
        this.frstnm = profile.getFirstName();
        this.lstnm = profile.getLastName();
    }

    //~--- methods ------------------------------------------------------------

    //~--- get methods --------------------------------------------------------



    public void addRole(Role role) {
        if (this.rls == null) {
            this.rls = new ArrayList<>();
        }

        boolean dupe = false;

        for (Role existingRole : this.rls) {
            if (StringUtils.equals(existingRole.getRlnm(), role.getRlnm())) {
                dupe = true;
                break;
            }
        }

        if (!dupe) {
            this.rls.add(role);
        }
    }

    public void removeRole(Role role) {
        if (this.rls != null) {
            Role toRemove = null;

            for (Role existingRole : rls) {
                if (StringUtils.equals(role.getRlnm(), existingRole.getRlnm())) {
                    toRemove = existingRole;
                    break;
                }
            }

            if (toRemove != null) {
                this.rls.remove(toRemove);
            }
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> result = null;

        if (this.getRls() != null && !this.getRls().isEmpty()) {
            result = new ArrayList<>();

            for (Role ur : this.getRls()) {
                for (String right : ur.getRghts()) {
                    result.add(new SimpleGrantedAuthority(right));
                }
            }
        }

        return result;
    }

    @Override
    public String getPassword() {
        return this.getPsswrd();
    }

    @Override
    public String getUsername() {
        return this.getMl();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.getNxprd();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.getNlckd();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.getCrdnxprd();
    }

    @Override
    public boolean isEnabled() {
        return this.getNbld();
    }
}
