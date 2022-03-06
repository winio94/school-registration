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
    private final int maxNumberOfStudentsPerCourse;
    private final int maxNumberOfCoursesPerStudent;

    public RegistrationServiceImpl(CoursesRepository coursesRepository,
                                   StudentsRepository studentsRepository,
                                   StudentsAndCoursesRepository studentsAndCoursesRepository,
                                   int maxNumberOfStudentsPerCourse,
                                   int maxNumberOfCoursesPerStudent) {
        this.coursesRepository = coursesRepository;
        this.studentsRepository = studentsRepository;
        this.studentsAndCoursesRepository = studentsAndCoursesRepository;
        this.maxNumberOfStudentsPerCourse = maxNumberOfStudentsPerCourse;
        this.maxNumberOfCoursesPerStudent = maxNumberOfCoursesPerStudent;
    }

    @Override
    public RegistrationResult register(String courseUuid,
                                       RegisterStudentToCourse registerStudentToCourse) {
        log.info("Registering student with uuid = {} for a course with uuid = {}",
                 registerStudentToCourse.getUuid(), courseUuid);
        coursesRepository.getOne(courseUuid)
                         .orElseThrow(Errors.notFoundError(courseUuid, Entity.COURSE));
        studentsRepository.getOne(registerStudentToCourse.getUuid())
                          .orElseThrow(Errors.notFoundError(courseUuid, Entity.STUDENT));
        if (isNumberOfStudentsExceeded(courseUuid)) {
            return numberOfStudentsExceededError();
        }
        if (isNumberOfCoursesExceeded(registerStudentToCourse)) {
            return numberOfCoursesExceededError();
        }
        studentsAndCoursesRepository.addStudentToTheCourse(courseUuid,
                                                           registerStudentToCourse.getUuid());
        return RegistrationResult.SUCCESS;
    }

    private boolean isNumberOfStudentsExceeded(String courseUuid) {
        int numberOfStudentsRegisteredToCourse = studentsAndCoursesRepository.getNumberOfStudentsRegistered(
            courseUuid);
        return numberOfStudentsRegisteredToCourse >= maxNumberOfStudentsPerCourse;
    }

    private boolean isNumberOfCoursesExceeded(RegisterStudentToCourse registerStudentToCourse) {
        int numberOfCoursesAssignedToStudent = studentsAndCoursesRepository.getNumberOfCoursesForStudent(
            registerStudentToCourse.getUuid());
        return numberOfCoursesAssignedToStudent >= maxNumberOfCoursesPerStudent;
    }

    private RegistrationResult numberOfStudentsExceededError() {
        log.error(
            "Unable to register student to this course, because the maximum number of students ({}) for given course would be exceeded",
            maxNumberOfStudentsPerCourse);
        return RegistrationResult.NUMBER_OF_STUDENTS_EXCEEDED;
    }

    private RegistrationResult numberOfCoursesExceededError() {
        log.error(
            "Unable to register student to this course, because the maximum number of courses ({}) for given student would be exceeded",
            maxNumberOfCoursesPerStudent);
        return RegistrationResult.NUMBER_OF_COURSES_EXCEEDED;
    }

}
