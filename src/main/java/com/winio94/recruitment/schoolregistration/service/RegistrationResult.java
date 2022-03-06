package com.winio94.recruitment.schoolregistration.service;

public enum RegistrationResult {
    NUMBER_OF_STUDENTS_EXCEEDED(false), NUMBER_OF_COURSES_EXCEEDED(false), SUCCESS(true);

    private final boolean isSuccessful;

    RegistrationResult(boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }
}
