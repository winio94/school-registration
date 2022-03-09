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

    public DbStudent() {
    }

    public DbStudent(String uuid, String firstName, String lastName) {
        super(uuid);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Student toDomainModel() {
        return new Student(this.uuid, this.firstName, this.lastName);
    }
}
