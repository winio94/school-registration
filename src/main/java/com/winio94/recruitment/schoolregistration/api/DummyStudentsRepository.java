package com.winio94.recruitment.schoolregistration.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class DummyStudentsRepository implements StudentsRepository {

    private final Map<String, Student> dummyStudents;

    public DummyStudentsRepository() {
        dummyStudents = new HashMap<>(2);
        Student john = new Student("59ee2a28-24fd-49fb-9bd7-eb34f77c6dc1", "John", "Doe");
        Student karen = new Student("0992e41b-78e9-4cb2-83de-5816e7c59283", "Karen", "Simson");
        dummyStudents.put("59ee2a28-24fd-49fb-9bd7-eb34f77c6dc1", john);
        dummyStudents.put("0992e41b-78e9-4cb2-83de-5816e7c59283", karen);
    }

    @Override
    public List<Student> getAll() {
        return new ArrayList<>(dummyStudents.values());
    }

    @Override
    public Optional<Student> getOne(String uuid) {
        return Optional.ofNullable(dummyStudents.get(uuid));
    }

    @Override
    public Student create(CreateNewStudent newStudent) {
        String uuid = UUID.randomUUID().toString();
        Student createdStudent = Student.from(newStudent, uuid);
        dummyStudents.put(uuid, createdStudent);
        return createdStudent;
    }

    @Override
    public void delete(Student newStudent) {

    }
}
