package com.winio94.recruitment.schoolregistration.api;

public class CreateNewStudent {
    private final String firstName;
    private final String lastName;

    public CreateNewStudent(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
