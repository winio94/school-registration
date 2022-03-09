package com.winio94.recruitment.schoolregistration.db;

import com.winio94.recruitment.schoolregistration.api.WithUuid;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;

@MappedSuperclass
public class DbEntity implements WithUuid {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Size(min = 36, max = 36)
    @Column(unique = true, name = "UUID")
    protected String uuid;

    public DbEntity(String uuid) {
        this.uuid = uuid;
    }

    public DbEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }
}