package com.winio94.recruitment.schoolregistration.service;

import com.winio94.recruitment.schoolregistration.api.Course;
import com.winio94.recruitment.schoolregistration.api.Student;
import com.winio94.recruitment.schoolregistration.api.StudentsAndCoursesRepository;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StudentsAndCoursesServiceImpl implements StudentsAndCoursesService {

    private static final Logger log = LoggerFactory.getLogger(StudentsAndCoursesServiceImpl.class);
    private final StudentsAndCoursesRepository studentsAndCoursesRepository;

    public StudentsAndCoursesServiceImpl(
        StudentsAndCoursesRepository studentsAndCoursesRepository) {
        this.studentsAndCoursesRepository = studentsAndCoursesRepository;
    }

    @Override
    public Set<Student> getAllStudentsForCourse(String courseUuid) {
        log.info("Fetching students that are registered for a course with uuid = {}", courseUuid);
        return studentsAndCoursesRepository.getAllStudentsForCourse(courseUuid);
    }

    @Override
    public Set<Course> getAllCoursesForStudent(String studentUuid) {
        log.info("Fetching courses that are assigned to the student with uuid = {}", studentUuid);
        return studentsAndCoursesRepository.getAllCoursesForStudent(studentUuid);
    }
}
