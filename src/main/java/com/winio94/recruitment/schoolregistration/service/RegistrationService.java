package com.winio94.recruitment.schoolregistration.service;

import com.winio94.recruitment.schoolregistration.api.Registration;
import io.vavr.control.Either;

public interface RegistrationService {

    Either<SchoolError, Void> register(String uuid, Registration registration);
}
