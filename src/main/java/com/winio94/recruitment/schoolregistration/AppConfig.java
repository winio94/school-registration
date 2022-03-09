package com.winio94.recruitment.schoolregistration;

import com.winio94.recruitment.schoolregistration.api.CoursesRepository;
import com.winio94.recruitment.schoolregistration.api.StudentsAndCoursesRepository;
import com.winio94.recruitment.schoolregistration.api.StudentsRepository;
import com.winio94.recruitment.schoolregistration.db.CoursesRepositoryImpl;
import com.winio94.recruitment.schoolregistration.db.DbCourseRepository;
import com.winio94.recruitment.schoolregistration.db.DbRegistrationRepository;
import com.winio94.recruitment.schoolregistration.db.DbStudentsRepository;
import com.winio94.recruitment.schoolregistration.db.StudentsAndCoursesRepositoryImpl;
import com.winio94.recruitment.schoolregistration.db.StudentsRepositoryImpl;
import com.winio94.recruitment.schoolregistration.service.CoursesService;
import com.winio94.recruitment.schoolregistration.service.CoursesServiceImpl;
import com.winio94.recruitment.schoolregistration.service.RegistrationService;
import com.winio94.recruitment.schoolregistration.service.RegistrationServiceImpl;
import com.winio94.recruitment.schoolregistration.service.StudentsAndCoursesService;
import com.winio94.recruitment.schoolregistration.service.StudentsAndCoursesServiceImpl;
import com.winio94.recruitment.schoolregistration.service.StudentsService;
import com.winio94.recruitment.schoolregistration.service.StudentsServiceImpl;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class AppConfig {

    @Autowired
    DbStudentsRepository dbStudentsRepository;

    @Autowired
    DbCourseRepository dbCourseRepository;

    @Autowired
    DbRegistrationRepository dbRegistrationRepository;

    @Bean
    public StudentsRepository studentsRepository() {
        return new StudentsRepositoryImpl(dbStudentsRepository);
    }

    @Bean
    public CoursesRepository coursesRepository() {
        return new CoursesRepositoryImpl(dbCourseRepository);
    }

    @Bean
    public StudentsAndCoursesRepository studentsAndCoursesRepository() {
        return new StudentsAndCoursesRepositoryImpl(dbRegistrationRepository, dbStudentsRepository,
                                                    dbCourseRepository);
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
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.ENGLISH);
        return slr;
    }
}