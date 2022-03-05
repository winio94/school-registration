package com.winio94.recruitment.schoolregistration.service;

import com.winio94.recruitment.schoolregistration.api.Student;
import com.winio94.recruitment.schoolregistration.api.CreateNewStudent;
import java.util.List;

public interface StudentsService {

    List<Student> getAll();

    Student getOne(String uuid);

    Student create(CreateNewStudent newStudent);

    void delete(Student newStudent);
}
