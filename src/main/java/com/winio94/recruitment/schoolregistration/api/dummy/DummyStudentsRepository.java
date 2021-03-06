package com.winio94.recruitment.schoolregistration.api.dummy;

import com.winio94.recruitment.schoolregistration.api.NewStudent;
import com.winio94.recruitment.schoolregistration.api.Student;
import com.winio94.recruitment.schoolregistration.api.StudentsRepository;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class DummyStudentsRepository implements StudentsRepository {

    private final Map<String, Student> dummyStudents;

    public DummyStudentsRepository() {
        dummyStudents = new HashMap<>();
    }

    @Override
    public Set<Student> getAll() {
        return new HashSet<>(dummyStudents.values());
    }

    @Override
    public Optional<Student> getOne(String uuid) {
        return Optional.ofNullable(dummyStudents.get(uuid));
    }

    @Override
    public Optional<Student> findByPersonalId(String personalId) {
        return dummyStudents.values()
                            .stream()
                            .filter(student -> student.getPersonalId()
                                                      .equals(personalId))
                            .findFirst();
    }

    @Override
    public Student create(NewStudent newStudent) {
        String uuid = UUID.randomUUID()
                          .toString();
        Student student = Student.from(newStudent, uuid);
        dummyStudents.put(uuid, student);
        return student;
    }

    @Override
    public void delete(String uuid) {
        dummyStudents.remove(uuid);
    }
}
