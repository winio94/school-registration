package com.winio94.recruitment.schoolregistration.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class DummyCoursesRepository implements CoursesRepository {

    private final Map<String, Course> dummyCourses;

    public DummyCoursesRepository() {
        dummyCourses = new HashMap<>();
    }

    @Override
    public List<Course> getAll() {
        return new ArrayList<>(dummyCourses.values());
    }

    @Override
    public Optional<Course> getOne(String uuid) {
        return Optional.ofNullable(dummyCourses.get(uuid));
    }

    @Override
    public Course create(CreateNewCourse createNewCourse) {
        String uuid = UUID.randomUUID().toString();
        Course course = Course.from(createNewCourse, uuid);
        dummyCourses.put(uuid, course);
        return course;
    }

    @Override
    public void delete(String uuid) {
        dummyCourses.remove(uuid);
    }
}
