package com.winio94.recruitment.schoolregistration.service;

import com.winio94.recruitment.schoolregistration.api.RegisterStudentToCourse;

public interface RegistrationService {

    RegistrationResult register(String uuid, RegisterStudentToCourse registerStudentToCourse);
}
