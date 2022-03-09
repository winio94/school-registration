package com.winio94.recruitment.schoolregistration.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "REGISTRATION")
public class DbRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(name = "STUDENT_UUID")
    private String studentUuid;

    @Column(name = "COURSE_UUID")
    private String courseUuid;

    public DbRegistration() {
    }

    public DbRegistration(String studentUuid, String courseUuid) {
        this.studentUuid = studentUuid;
        this.courseUuid = courseUuid;
    }

    public Long getId() {
        return id;
    }

    public String getStudentUuid() {
        return studentUuid;
    }

    public String getCourseUuid() {
        return courseUuid;
    }
}
