package com.winio94.recruitment.schoolregistration.service;

import com.winio94.recruitment.schoolregistration.api.Student;
import com.winio94.recruitment.schoolregistration.api.StudentsRepository;
import com.winio94.recruitment.schoolregistration.api.CreateNewStudent;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class StudentsServiceImpl implements StudentsService {

    private final Logger log = LoggerFactory.getLogger(StudentsServiceImpl.class);
    private final StudentsRepository studentsRepository;

    public StudentsServiceImpl(StudentsRepository studentsRepository) {
        this.studentsRepository = studentsRepository;
    }

    @Override
    public List<Student> getAll() {
        log.info("Fetching all students");
        return studentsRepository.getAll();
    }

    @Override
    public Student getOne(String uuid) {
        log.info("Fetching student by uuid = {}", uuid);
        return studentsRepository.getOne(uuid).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(
                "Student with uuid = %s does not exists", uuid)));
    }

    @Override
    public Student create(CreateNewStudent newStudent) {
        log.info("Creating new student with first name = {} and last name = {}", newStudent.getFirstName(), newStudent.getLastName());
        return studentsRepository.create(newStudent);
    }

    @Override
    public void delete(Student newStudent) {

    }
}
