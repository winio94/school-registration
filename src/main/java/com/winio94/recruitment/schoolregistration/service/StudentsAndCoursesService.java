package com.winio94.recruitment.schoolregistration.service;

import com.winio94.recruitment.schoolregistration.api.Course;
import com.winio94.recruitment.schoolregistration.api.Student;
import java.util.Set;

public interface StudentsAndCoursesService {

    Set<Student> getAllStudentsFiltered(String courseUuid, boolean notRegisteredToAnyCourse);

    Set<Course> getAllCoursesFiltered(String studentUuid, boolean withoutAnyStudent);
}
