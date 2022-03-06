package com.winio94.recruitment.schoolregistration.service;

import com.winio94.recruitment.schoolregistration.api.Course;
import com.winio94.recruitment.schoolregistration.api.Student;
import com.winio94.recruitment.schoolregistration.api.StudentsAndCoursesRepository;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class StudentsAndCoursesServiceImpl implements StudentsAndCoursesService {

    private static final Logger log = LoggerFactory.getLogger(StudentsAndCoursesServiceImpl.class);
    private final StudentsAndCoursesRepository studentsAndCoursesRepository;
    private final StudentsService studentsService;
    private final CoursesService coursesService;

    public StudentsAndCoursesServiceImpl(StudentsAndCoursesRepository studentsAndCoursesRepository,
                                         StudentsService studentsService,
                                         CoursesService coursesService) {
        this.studentsAndCoursesRepository = studentsAndCoursesRepository;
        this.studentsService = studentsService;
        this.coursesService = coursesService;
    }

    @Override
    public Set<Student> getAllStudentsFiltered(String courseUuid,
                                               boolean notRegisteredToAnyCourse) {
        if (Objects.nonNull(courseUuid) && notRegisteredToAnyCourse) {
            //todo test this
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                              "Unable to fetch students that match given criteria. "
                                                  + "Please remove one of provided filtering conditions");
        } else if (Objects.nonNull(courseUuid)) {
            log.info("Fetching students that are registered for a course with uuid = {}",
                     courseUuid);
            return studentsAndCoursesRepository.getAllStudentsForCourse(courseUuid);
        } else if (notRegisteredToAnyCourse) {
            log.info("Fetching courses that are not assigned to any student");
            return studentsAndCoursesRepository.getAllStudentsNotRegisteredToAnyCourse();
        } else {
            return studentsService.getAll();
        }
    }

    @Override
    public Set<Course> getAllCoursesFiltered(String studentUuid, boolean withoutAnyStudent) {
        if (Objects.nonNull(studentUuid) && withoutAnyStudent) {
            //todo test this
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                              "Unable to fetch courses that match given criteria. "
                                                  + "Please remove one of provided filtering conditions");
        } else if (Objects.nonNull(studentUuid)) {
            log.info("Fetching courses that are assigned to the student with uuid = {}",
                     studentUuid);
            return studentsAndCoursesRepository.getAllCoursesForStudent(studentUuid);
        } else if (withoutAnyStudent) {
            log.info("Fetching courses that are not assigned to any student");
            return studentsAndCoursesRepository.getAllCoursesWithoutAnyStudent();
        } else {
            return coursesService.getAll();
        }
    }
}
