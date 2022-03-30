package com.winio94.recruitment.schoolregistration.service;

import com.winio94.recruitment.schoolregistration.api.NewStudent;
import com.winio94.recruitment.schoolregistration.api.Student;
import io.vavr.control.Either;
import java.util.Set;

public interface StudentsService {

    Set<Student> getAll();

    Student getOne(String uuid);

    Either<StudentError, Student> create(NewStudent newStudent);

    void delete(String uuid);
}
