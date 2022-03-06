package com.winio94.recruitment.schoolregistration.service;

import com.winio94.recruitment.schoolregistration.api.NewStudent;
import com.winio94.recruitment.schoolregistration.api.Student;
import java.util.Set;

public interface StudentsService {

    Set<Student> getAll();

    Student getOne(String uuid);

    Student create(NewStudent newStudent);

    void delete(String uuid);
}
