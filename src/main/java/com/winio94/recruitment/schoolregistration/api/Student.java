package com.winio94.recruitment.schoolregistration.api;

import java.util.Objects;

public class Student extends NewStudent implements WithUuid {

    private final String uuid;

    public Student(String uuid, String firstName, String lastName, String personalId) {
        super(firstName, lastName, personalId);
        this.uuid = uuid;
    }

    public static Student from(NewStudent newStudent, String uuid) {
        return new Student(uuid, newStudent.getFirstName(), newStudent.getLastName(), newStudent.getPersonalId());
    }

    public String getUuid() {
        return uuid;
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
