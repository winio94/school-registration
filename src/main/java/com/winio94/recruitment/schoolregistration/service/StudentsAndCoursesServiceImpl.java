package com.winio94.recruitment.schoolregistration.service;

import com.winio94.recruitment.schoolregistration.api.Course;
import com.winio94.recruitment.schoolregistration.api.Student;
import com.winio94.recruitment.schoolregistration.api.StudentsAndCoursesRepository;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StudentsAndCoursesServiceImpl implements StudentsAndCoursesService {

    private static final Logger log = LoggerFactory.getLogger(StudentsAndCoursesServiceImpl.class);
    private final StudentsAndCoursesRepository studentsAndCoursesRepository;
    private final StudentsService studentsService;

    public StudentsAndCoursesServiceImpl(StudentsAndCoursesRepository studentsAndCoursesRepository,
                                         StudentsService studentsService) {
        this.studentsAndCoursesRepository = studentsAndCoursesRepository;
        this.studentsService = studentsService;
    }

    @Override
    public Set<Student> getAllStudentsForCourse(String courseUuid) {
        if (Objects.nonNull(courseUuid)) {
            log.info("Fetching students that are registered for a course with uuid = {}",
                     courseUuid);
            return studentsAndCoursesRepository.getAllStudentsForCourse(courseUuid);
        } else {
            return studentsService.getAll();
        }
    }

    @Override
    public Set<Course> getAllCoursesForStudent(String studentUuid) {
        log.info("Fetching courses that are assigned to the student with uuid = {}", studentUuid);
        return studentsAndCoursesRepository.getAllCoursesForStudent(studentUuid);
    }
}
