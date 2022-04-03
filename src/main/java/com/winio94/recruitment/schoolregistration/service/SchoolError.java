package com.winio94.recruitment.schoolregistration.service;

import io.vavr.collection.HashMap;
import java.util.Map;
import org.apache.commons.lang3.NotImplementedException;

public enum SchoolError {
    PERSONAL_ID_ALREADY_USED() {
        @Override
        public Map<String, String> errorDetails() {
            return HashMap.of("personalId", "is already used by other student")
                          .toJavaMap();
        }
    }, CODE_ALREADY_USED() {
        @Override
        public Map<String, String> errorDetails() {
            return HashMap.of("code", "is already used by other course")
                          .toJavaMap();
        }
    };

    public Map<String, String> errorDetails() {
        throw new NotImplementedException();
    }
}
