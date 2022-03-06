package com.winio94.recruitment.schoolregistration.api;

import java.util.Set;

public interface StudentsAndCoursesRepository {

    Set<Student> getAllStudentsForCourse(String courseUuid);

    void addStudentToTheCourse(String courseUuid, String studentUuid);
}
