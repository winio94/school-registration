package com.winio94.recruitment.schoolregistration.api;

public class NewStudent {

    private final String firstName;
    private final String lastName;
    private final String personalId;

    public NewStudent(String firstName, String lastName, String personalId) {
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

    public String getPersonalId() {
        return personalId;
    }
}
