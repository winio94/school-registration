package com.winio94.recruitment.schoolregistration.service;

import com.winio94.recruitment.schoolregistration.api.Course;
import com.winio94.recruitment.schoolregistration.api.CreateNewCourse;
import java.util.List;

public interface CoursesService {

    List<Course> getAll();

    Course getOne(String uuid);

    Course create(CreateNewCourse newCourse);

    void delete(String uuid);
}
