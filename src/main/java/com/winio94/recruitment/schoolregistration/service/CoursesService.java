package com.winio94.recruitment.schoolregistration.service;

import com.winio94.recruitment.schoolregistration.api.Course;
import com.winio94.recruitment.schoolregistration.api.NewCourse;
import java.util.List;

public interface CoursesService {

    List<Course> getAll();

    Course getOne(String uuid);

    Course create(NewCourse newCourse);

    void delete(String uuid);
}
