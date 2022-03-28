package com.winio94.recruitment.schoolregistration.api.dummy;

import com.winio94.recruitment.schoolregistration.api.Course;
import com.winio94.recruitment.schoolregistration.api.CoursesRepository;
import com.winio94.recruitment.schoolregistration.api.NewCourse;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class DummyCoursesRepository implements CoursesRepository {

    private final Map<String, Course> dummyCourses;

    public DummyCoursesRepository() {
        dummyCourses = new HashMap<>();
    }

    @Override
    public Set<Course> getAll() {
        return new HashSet<>(dummyCourses.values());
    }

    @Override
    public Optional<Course> getOne(String uuid) {
        return Optional.ofNullable(dummyCourses.get(uuid));
    }

    @Override
    public Course create(NewCourse newCourse) {
        String uuid = UUID.randomUUID()
                          .toString();
        Course course = Course.from(newCourse, uuid);
        dummyCourses.put(uuid, course);
        return course;
    }

    @Override
    public void delete(String uuid) {
        dummyCourses.remove(uuid);
    }
}
