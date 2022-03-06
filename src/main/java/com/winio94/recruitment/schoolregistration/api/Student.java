package com.winio94.recruitment.schoolregistration.api;

import java.util.Objects;

public class Student implements WithUuid {

    private final String uuid;
    private final String firstName;
    private final String lastName;

    public Student(String uuid, String firstName, String lastName) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static Student from(NewStudent newStudent, String uuid) {
        return new Student(uuid, newStudent.getFirstName(), newStudent.getLastName());
    }

    public String getUuid() {
        return uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student student = (Student) o;
        return uuid.equals(student.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
