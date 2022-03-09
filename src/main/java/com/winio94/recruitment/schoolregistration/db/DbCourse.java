package com.winio94.recruitment.schoolregistration.db;

import com.winio94.recruitment.schoolregistration.api.Course;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "COURSE")
public class DbCourse extends DbEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "CODE")
    private String code;

    public DbCourse() {
    }

    public DbCourse(String uuid, String name, String code) {
        super(uuid);
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public Course toDomainModel() {
        return new Course(this.uuid, this.name, this.code);
    }
}
