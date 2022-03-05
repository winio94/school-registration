package com.winio94.recruitment.schoolregistration.api;

public class CreateNewCourse {

    private final String name;
    private final String code;

    public CreateNewCourse(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
