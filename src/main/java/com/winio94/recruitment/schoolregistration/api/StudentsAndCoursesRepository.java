package com.winio94.recruitment.schoolregistration.api;

import java.util.Set;

public interface StudentsAndCoursesRepository {

    void addStudentToTheCourse(String courseUuid, String studentUuid);

    Set<Student> getAllStudentsForCourse(String courseUuid);

    Set<Course> getAllCoursesForStudent(String studentUuid);
}
