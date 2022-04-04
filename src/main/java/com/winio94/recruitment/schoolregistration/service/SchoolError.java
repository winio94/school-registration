package com.winio94.recruitment.schoolregistration.service;

import java.util.Collections;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.ResponseEntity;

public enum SchoolError {
    PERSONAL_ID_ALREADY_USED() {
        @Override
        public ResponseEntity<Errors> toResponse() {
            return ResponseEntity.badRequest()
                                 .body(new Errors(Collections.singletonList(
                                     "provided personal ID is already used by other student")));
        }
    }, CODE_ALREADY_USED() {
        @Override
        public ResponseEntity<Errors> toResponse() {
            return ResponseEntity.badRequest()
                                 .body(new Errors(Collections.singletonList(
                                     "provided code is already used by other course")));
        }
    }, COURSE_DOES_NOT_EXISTS() {
        @Override
        public ResponseEntity<Errors> toResponse() {
            return ResponseEntity.notFound()
                                 .build();
        }
    }, STUDENT_DOES_NOT_EXISTS() {
        @Override
        public ResponseEntity<Errors> toResponse() {
            return ResponseEntity.notFound()
                                 .build();
        }
    }, NUMBER_OF_STUDENTS_EXCEEDED() {
        @Override
        public ResponseEntity<Errors> toResponse() {
            return ResponseEntity.badRequest()
                                 .body(new Errors(Collections.singletonList(
                                     "maximum number of students for given course has been exceeded")));
        }
    }, NUMBER_OF_COURSES_EXCEEDED() {
        @Override
        public ResponseEntity<Errors> toResponse() {
            return ResponseEntity.badRequest()
                                 .body(new Errors(Collections.singletonList(
                                     "maximum number of courses for given student has been exceeded")));
        }
    }, STUDENT_ALREADY_REGISTERED_TO_COURSE() {
        @Override
        public ResponseEntity<Errors> toResponse() {
            return ResponseEntity.badRequest()
                                 .body(new Errors(Collections.singletonList(
                                     "student is already registered to this course")));
        }
    };

    public ResponseEntity<Errors> toResponse() {
        throw new NotImplementedException();
    }
}
