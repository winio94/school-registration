package com.winio94.recruitment.schoolregistration;

import com.winio94.recruitment.schoolregistration.api.CoursesRepository;
import com.winio94.recruitment.schoolregistration.api.DummyCoursesRepository;
import com.winio94.recruitment.schoolregistration.api.DummyStudentsRepository;
import com.winio94.recruitment.schoolregistration.api.StudentsRepository;
import com.winio94.recruitment.schoolregistration.service.CoursesService;
import com.winio94.recruitment.schoolregistration.service.CoursesServiceImpl;
import com.winio94.recruitment.schoolregistration.service.RegistrationService;
import com.winio94.recruitment.schoolregistration.service.RegistrationServiceImpl;
import com.winio94.recruitment.schoolregistration.service.StudentsService;
import com.winio94.recruitment.schoolregistration.service.StudentsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
                                                   StudentsRepository studentsRepository) {
        return new RegistrationServiceImpl(coursesRepository, studentsRepository);
    }
}