package com.winio94.recruitment.schoolregistration.service;

import com.winio94.recruitment.schoolregistration.api.Entity;
import java.util.function.Supplier;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class HttpErrors {

    static Supplier<ResponseStatusException> notFoundError(String uuid, Entity entity) {
        return () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                 String.format("%s with uuid = %s does not exists",
                                                               entity.getType(),
                                                               uuid));
    }
}
