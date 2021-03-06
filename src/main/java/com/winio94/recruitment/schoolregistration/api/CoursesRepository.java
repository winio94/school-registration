package com.winio94.recruitment.schoolregistration.api;

import java.util.Optional;
import java.util.Set;

public interface CoursesRepository {

    Set<Course> getAll();

    Optional<Course> getOneByUuid(String uuid);

    Optional<Course> getOneByCode(String code);

    Course create(NewCourse newCourse);

    void delete(String uuid);
}
