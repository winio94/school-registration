package com.winio94.recruitment.schoolregistration.service;

import com.winio94.recruitment.schoolregistration.api.CoursesRepository;
import com.winio94.recruitment.schoolregistration.api.Entity;
import com.winio94.recruitment.schoolregistration.api.RegisterStudentToCourse;
import com.winio94.recruitment.schoolregistration.api.StudentsAndCoursesRepository;
import com.winio94.recruitment.schoolregistration.api.StudentsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegistrationServiceImpl implements RegistrationService {

    private final Logger log = LoggerFactory.getLogger(RegistrationServiceImpl.class);
    private final CoursesRepository coursesRepository;
    private final StudentsRepository studentsRepository;
    private final StudentsAndCoursesRepository studentsAndCoursesRepository;

    public RegistrationServiceImpl(CoursesRepository coursesRepository,
                                   StudentsRepository studentsRepository,
                                   StudentsAndCoursesRepository studentsAndCoursesRepository) {
        this.coursesRepository = coursesRepository;
        this.studentsRepository = studentsRepository;
        this.studentsAndCoursesRepository = studentsAndCoursesRepository;
    }

    @Override
    public void register(String courseUuid, RegisterStudentToCourse registerStudentToCourse) {
        log.info("Registering student with uuid = {} for a course with uuid = {}",
                 registerStudentToCourse.getUuid(), courseUuid);
        coursesRepository.getOne(courseUuid)
                         .orElseThrow(Errors.notFoundError(courseUuid, Entity.COURSE));
        studentsRepository.getOne(registerStudentToCourse.getUuid())
                          .orElseThrow(Errors.notFoundError(courseUuid, Entity.STUDENT));
        studentsAndCoursesRepository.addStudentToTheCourse(courseUuid,
                                                           registerStudentToCourse.getUuid());
    }
}
