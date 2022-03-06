package com.winio94.recruitment.schoolregistration.api;

import java.util.List;
import java.util.Optional;

public interface CoursesRepository {

    List<Course> getAll();

    Optional<Course> getOne(String uuid);

    Course create(NewCourse newCourse);

    void delete(String uuid);
}
