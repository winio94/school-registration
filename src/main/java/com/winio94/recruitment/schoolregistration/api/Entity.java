package com.winio94.recruitment.schoolregistration.api;

public enum Entity {
    STUDENT("Student"), COURSE("Course");

    private final String type;

    Entity(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
