package com.winio94.recruitment.schoolregistration.api;

import java.util.List;
import java.util.Optional;

public interface StudentsRepository {

    List<Student> getAll();

    Optional<Student> getOne();

    Student create(Student newStudent);

    void delete(Student newStudent);
}
