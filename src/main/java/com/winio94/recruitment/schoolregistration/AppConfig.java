package com.winio94.recruitment.schoolregistration;

import com.winio94.recruitment.schoolregistration.api.CoursesRepository;
import com.winio94.recruitment.schoolregistration.api.DummyCoursesRepository;
import com.winio94.recruitment.schoolregistration.api.DummyStudentsAndCoursesRepository;
import com.winio94.recruitment.schoolregistration.api.DummyStudentsRepository;
import com.winio94.recruitment.schoolregistration.api.StudentsAndCoursesRepository;
import com.winio94.recruitment.schoolregistration.api.StudentsRepository;
import com.winio94.recruitment.schoolregistration.service.CoursesService;
import com.winio94.recruitment.schoolregistration.service.CoursesServiceImpl;
import com.winio94.recruitment.schoolregistration.service.RegistrationService;
import com.winio94.recruitment.schoolregistration.service.RegistrationServiceImpl;
import com.winio94.recruitment.schoolregistration.service.StudentsAndCoursesService;
import com.winio94.recruitment.schoolregistration.service.StudentsAndCoursesServiceImpl;
import com.winio94.recruitment.schoolregistration.service.StudentsService;
import com.winio94.recruitment.schoolregistration.service.StudentsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class AppConfig {

    @Bean
    public StudentsRepository studentsRepository() {
        return new DummyStudentsRepository();
    }

    @Bean
    public CoursesRepository coursesRepository() {
        return new DummyCoursesRepository();
    }

    @Bean
    public StudentsService studentsService(StudentsRepository studentsRepository) {
        return new StudentsServiceImpl(studentsRepository);
    }

    @Bean
    public CoursesService coursesService(CoursesRepository coursesRepository) {
        return new CoursesServiceImpl(coursesRepository);
    }

    @Bean
    public RegistrationService registrationService(CoursesRepository coursesRepository,
                                                   StudentsRepository studentsRepository,
                                                   StudentsAndCoursesRepository studentsAndCoursesService,
                                                   Environment environment) {
        int maxNumberOfStudentsPerCourse = Integer.parseInt(
            environment.getProperty(Environments.MAX_NUMBER_OF_STUDENTS_PER_COURSE.name(), "50"));
        int maxNumberOfCoursesPerStudent = Integer.parseInt(
            environment.getProperty(Environments.MAX_NUMBER_OF_COURSES_PER_STUDENT.name(), "5"));
        return new RegistrationServiceImpl(coursesRepository, studentsRepository,
                                           studentsAndCoursesService, maxNumberOfStudentsPerCourse,
                                           maxNumberOfCoursesPerStudent);
    }

    @Bean
    public StudentsAndCoursesService studentsAndCoursesService(
        StudentsAndCoursesRepository studentsAndCoursesRepository, StudentsService studentsService,
        CoursesService coursesService) {
        return new StudentsAndCoursesServiceImpl(studentsAndCoursesRepository, studentsService,
                                                 coursesService);
    }

    @Bean
    public StudentsAndCoursesRepository studentsAndCoursesRepository(
        StudentsRepository studentsRepository, CoursesRepository coursesRepository) {
        return new DummyStudentsAndCoursesRepository(studentsRepository, coursesRepository);
    }
}