package com.winio94.recruitment.schoolregistration.db;

import com.winio94.recruitment.schoolregistration.api.Student;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "STUDENT")
public class DbStudent extends DbEntity {

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "PERSONAL_ID", unique = true)
    private String personalId;

    public DbStudent() {
    }

    public DbStudent(String uuid, String firstName, String lastName, String personalId) {
        super(uuid);
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Student toDomainModel() {
        return new Student(this.uuid, this.firstName, this.lastName, this.personalId);
    }
}
