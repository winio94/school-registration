package com.winio94.recruitment.schoolregistration.api;

public class Course {

    private final String uuid;
    private final String name;
    private final String code;

    public Course(String uuid, String name, String code) {
        this.uuid = uuid;
        this.name = name;
        this.code = code;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public static Course from(CreateNewCourse createNewCourse, String uuid) {
        return new Course(uuid, createNewCourse.getName(), createNewCourse.getCode());
    }
}
