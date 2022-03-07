package com.winio94.recruitment.schoolregistration.service;

import com.winio94.recruitment.schoolregistration.api.Registration;

public interface RegistrationService {

    RegistrationResult register(String uuid, Registration registration);
}
