package com.winio94.recruitment.schoolregistration.api;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class DummyStudentsAndCoursesRepository implements StudentsAndCoursesRepository {

    private final Map<String, Set<Student>> studentsPerCourse;
    private final StudentsRepository studentsRepository;
    private final CoursesRepository coursesRepository;

    public DummyStudentsAndCoursesRepository(StudentsRepository studentsRepository,
                                             CoursesRepository coursesRepository) {
        this.studentsRepository = studentsRepository;
        this.coursesRepository = coursesRepository;
        this.studentsPerCourse = new HashMap<>();
    }

    @Override
    public Set<Student> getAllStudentsForCourse(String courseUuid) {
        return studentsPerCourse.getOrDefault(courseUuid, Collections.emptySet());
    }

    @Override
    public void addStudentToTheCourse(String courseUuid, String studentUuid) {
        Set<Student> students = Optional.ofNullable(studentsPerCourse.get(courseUuid))
                                        .orElse(new HashSet<>());
        studentsRepository.getOne(studentUuid).ifPresent(students::add);
        studentsPerCourse.put(courseUuid, students);
    }
}
