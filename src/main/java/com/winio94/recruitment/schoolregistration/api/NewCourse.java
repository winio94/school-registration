package com.winio94.recruitment.schoolregistration.api;

//todo add validation (maybe xml)
public class NewCourse {

    private final String name;
    private final String code;

    public NewCourse(String name, String code) {
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
