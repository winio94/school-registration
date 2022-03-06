package com.winio94.recruitment.schoolregistration.api;

import java.util.Optional;
import java.util.Set;

public interface CoursesRepository {

    Set<Course> getAll();

    Optional<Course> getOne(String uuid);

    Course create(NewCourse newCourse);

    void delete(String uuid);
}
