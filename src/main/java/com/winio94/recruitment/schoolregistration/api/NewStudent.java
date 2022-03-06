package com.winio94.recruitment.schoolregistration.api;

//todo add validation (maybe xml)
public class NewStudent {

    private final String firstName;
    private final String lastName;

    public NewStudent(String firstName, String lastName) {
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
