package com.winio94.recruitment.schoolregistration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(AppConfig.class)
public class SchoolRegistrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchoolRegistrationApplication.class, args);
    }

}
