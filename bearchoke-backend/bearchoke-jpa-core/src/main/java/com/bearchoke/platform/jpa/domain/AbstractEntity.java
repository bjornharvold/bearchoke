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

package com.bearchoke.platform.jpa.domain;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.persistence.Version;
import java.util.UUID;

/**
 * Created by Bjorn Harvold
 * Date: 1/18/14
 * Time: 1:33 PM
 * Responsibility:
 */
@MappedSuperclass
@Data
public class AbstractEntity {

    @Transient
    private final static EthernetAddress nic = EthernetAddress.fromInterface();

    @Transient
    private final static TimeBasedGenerator uuidGenerator = Generators.timeBasedGenerator(nic);

    @Id
    @Column(name = "id")
    private String id;

    @Version
    @Column(name = "version")
    protected Integer version;

    @Column(nullable = false)
    @Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
    @DateTimeFormat(style = "S-")
    private DateTime createdDate;

    @Column(nullable = false)
    @Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
    @DateTimeFormat(style = "S-")
    private DateTime lastUpdate;


    @PrePersist
    public void prePersist() {
        DateTime d = new DateTime();
        if (this.createdDate == null) {
            this.createdDate = d;
        }
        if (this.lastUpdate == null) {
            this.lastUpdate = d;
        }

        if (StringUtils.isBlank(this.id)) {
            // let the app and not the db generate primary keys
            UUID uuid = uuidGenerator.generate();
            this.id = uuid.toString();
        }
    }

    @PreUpdate
    public void preUpdate() {
        lastUpdate = new DateTime();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (id != null) {
            sb.append("id=").append(id).append(", ");
        }
        if (version != null) {
            sb.append("version=").append(version).append(", ");
        }
        if (createdDate != null) {
            sb.append("createdDate=").append(createdDate).append(", ");
        }
        if (lastUpdate != null) {
            sb.append("lastUpdate=").append(lastUpdate).append(", ");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof AbstractEntity)) return false;

        AbstractEntity that = (AbstractEntity) o;

        if (createdDate != null ? !createdDate.equals(that.createdDate) : that.createdDate != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (lastUpdate != null ? !lastUpdate.equals(that.lastUpdate) : that.lastUpdate != null) return false;
        if (version != null ? !version.equals(that.version) : that.version != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (lastUpdate != null ? lastUpdate.hashCode() : 0);
        return result;
    }

    public String getIdAsString() {
        return id;
    }
}
