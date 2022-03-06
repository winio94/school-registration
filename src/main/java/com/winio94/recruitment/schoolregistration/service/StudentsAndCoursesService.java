package com.winio94.recruitment.schoolregistration.service;

import com.winio94.recruitment.schoolregistration.api.Student;
import java.util.Set;

public interface StudentsAndCoursesService {

    Set<Student> getAllStudentsForCourse(String courseUuid);
}
