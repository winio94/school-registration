package com.winio94.recruitment.schoolregistration.api;

import java.util.Optional;
import java.util.Set;

public interface StudentsRepository {

    Set<Student> getAll();

    Optional<Student> getOne(String uuid);

    Optional<Student> findByPersonalId(String personalId);

    Student create(NewStudent newStudent);

    void delete(String uuid);
}
