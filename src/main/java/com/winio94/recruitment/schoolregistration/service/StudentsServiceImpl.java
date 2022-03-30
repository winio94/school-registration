package com.winio94.recruitment.schoolregistration.service;

import com.winio94.recruitment.schoolregistration.api.Entity;
import com.winio94.recruitment.schoolregistration.api.NewStudent;
import com.winio94.recruitment.schoolregistration.api.Student;
import com.winio94.recruitment.schoolregistration.api.StudentsRepository;
import io.vavr.control.Either;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StudentsServiceImpl implements StudentsService {

    private final Logger log = LoggerFactory.getLogger(StudentsServiceImpl.class);
    private final StudentsRepository studentsRepository;

    public StudentsServiceImpl(StudentsRepository studentsRepository) {
        this.studentsRepository = studentsRepository;
    }

    @Override
    public Set<Student> getAll() {
        log.info("Fetching all students");
        return studentsRepository.getAll();
    }

    @Override
    public Student getOne(String uuid) {
        log.info("Fetching student by uuid = {}", uuid);
        return studentsRepository.getOne(uuid)
                                 .orElseThrow(Errors.notFoundError(uuid, Entity.STUDENT));
    }

    @Override
    public Either<StudentError, Student> create(NewStudent newStudent) {
        log.info("Creating new student with first name = {} and last name = {}",
                 newStudent.getFirstName(),
                 newStudent.getLastName());
        Optional<Student> student = studentsRepository.findByPersonalId(newStudent.getPersonalId());
        if (student.isPresent()) {
            return Either.left(StudentError.PERSONAL_ID_ALREADY_USED);
        }
        return Either.right(studentsRepository.create(newStudent));
    }

    @Override
    public void delete(String uuid) {
        log.info("Deleting student with uuid = {}", uuid);
        studentsRepository.getOne(uuid)
                          .orElseThrow(Errors.notFoundError(uuid, Entity.STUDENT));
        studentsRepository.delete(uuid);
    }
}
