package com.winio94.recruitment.schoolregistration.api;

public class Student {

    private final String uuid;
    private final String firstName;
    private final String lastName;

    public Student(String uuid, String firstName, String lastName) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static Student from(CreateNewStudent newStudent, String uuid) {
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
}
