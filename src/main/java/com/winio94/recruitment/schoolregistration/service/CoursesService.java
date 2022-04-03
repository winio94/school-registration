package com.winio94.recruitment.schoolregistration.service;

import com.winio94.recruitment.schoolregistration.api.Course;
import com.winio94.recruitment.schoolregistration.api.NewCourse;
import io.vavr.control.Either;
import java.util.Set;

public interface CoursesService {

    Set<Course> getAll();

    Course getOneByUuid(String uuid);

    Either<SchoolError, Course> create(NewCourse newCourse);

    void delete(String uuid);
}
