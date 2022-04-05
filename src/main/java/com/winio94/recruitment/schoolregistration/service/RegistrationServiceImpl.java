package com.winio94.recruitment.schoolregistration.service;

import com.winio94.recruitment.schoolregistration.api.Course;
import com.winio94.recruitment.schoolregistration.api.CoursesRepository;
import com.winio94.recruitment.schoolregistration.api.Registration;
import com.winio94.recruitment.schoolregistration.api.Student;
import com.winio94.recruitment.schoolregistration.api.StudentsAndCoursesRepository;
import com.winio94.recruitment.schoolregistration.api.StudentsRepository;
import io.vavr.control.Either;
import java.util.Optional;
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
    public Either<SchoolError, Void> register(String courseUuid, Registration registration) {
        log.info("Registering student with uuid = {} for a course with uuid = {}",
                 registration.getUuid(),
                 courseUuid);
        Optional<Course> course = coursesRepository.getOneByUuid(courseUuid);
        if (!course.isPresent()) {
            return Either.left(SchoolError.COURSE_DOES_NOT_EXISTS);
        }
        Optional<Student> student = studentsRepository.getOne(registration.getUuid());
        if (!student.isPresent()) {
            return Either.left(SchoolError.STUDENT_DOES_NOT_EXISTS);
        }
        if (studentsAndCoursesRepository.isStudentRegisteredToCourse(student.get(), course.get())) {
            return Either.left(SchoolError.STUDENT_ALREADY_REGISTERED_TO_COURSE);
        }
        if (isNumberOfStudentsExceeded(courseUuid)) {
            return Either.left(numberOfStudentsExceededError());
        }
        if (isNumberOfCoursesExceeded(registration)) {
            return Either.left(numberOfCoursesExceededError());
        }
        studentsAndCoursesRepository.addStudentToTheCourse(courseUuid, registration.getUuid());
        return Either.right(null);
    }

    private boolean isNumberOfStudentsExceeded(String courseUuid) {
        int numberOfStudentsRegisteredToCourse = studentsAndCoursesRepository.getNumberOfStudentsRegistered(
            courseUuid);
        return numberOfStudentsRegisteredToCourse >= maxNumberOfStudentsPerCourse;
    }

    private boolean isNumberOfCoursesExceeded(Registration registration) {
        int numberOfCoursesAssignedToStudent = studentsAndCoursesRepository.getNumberOfCoursesForStudent(
            registration.getUuid());
        return numberOfCoursesAssignedToStudent >= maxNumberOfCoursesPerStudent;
    }

    private SchoolError numberOfStudentsExceededError() {
        log.error(
            "Unable to register student to this course, because the maximum number of students ({}) for given course would be exceeded",
            maxNumberOfStudentsPerCourse);
        return SchoolError.NUMBER_OF_STUDENTS_EXCEEDED;
    }

    private SchoolError numberOfCoursesExceededError() {
        log.error(
            "Unable to register student to this course, because the maximum number of courses ({}) for given student would be exceeded",
            maxNumberOfCoursesPerStudent);
        return SchoolError.NUMBER_OF_COURSES_EXCEEDED;
    }

}
