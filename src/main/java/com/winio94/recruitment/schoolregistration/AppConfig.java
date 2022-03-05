package com.winio94.recruitment.schoolregistration;

import com.winio94.recruitment.schoolregistration.api.DummyCoursesRepository;
import com.winio94.recruitment.schoolregistration.api.DummyStudentsRepository;
import com.winio94.recruitment.schoolregistration.service.CoursesService;
import com.winio94.recruitment.schoolregistration.service.CoursesServiceImpl;
import com.winio94.recruitment.schoolregistration.service.StudentsService;
import com.winio94.recruitment.schoolregistration.service.StudentsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public StudentsService studentsService() {
        return new StudentsServiceImpl(new DummyStudentsRepository());
    }

    @Bean
    public CoursesService coursesService() {
        return new CoursesServiceImpl(new DummyCoursesRepository());
    }
}