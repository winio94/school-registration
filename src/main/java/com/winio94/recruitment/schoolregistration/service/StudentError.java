package com.winio94.recruitment.schoolregistration.service;

import io.vavr.collection.HashMap;
import java.util.Map;
import org.apache.commons.lang3.NotImplementedException;

public enum StudentError {
    PERSONAL_ID_ALREADY_USED() {
        @Override
        public Map<String, String> errorDetails() {
            return HashMap.of("personalId", "is already used by other student")
                          .toJavaMap();
        }
    };

    public Map<String, String> errorDetails() {
        throw new NotImplementedException();
    }
}
