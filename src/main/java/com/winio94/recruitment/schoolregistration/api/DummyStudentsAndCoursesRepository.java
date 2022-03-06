package com.winio94.recruitment.schoolregistration.api;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public void addStudentToTheCourse(String courseUuid, String studentUuid) {
        Set<Student> students = Optional.ofNullable(studentsPerCourse.get(courseUuid))
                                        .orElse(new HashSet<>());
        studentsRepository.getOne(studentUuid).ifPresent(students::add);
        studentsPerCourse.put(courseUuid, students);
    }

    @Override
    public Set<Student> getAllStudentsForCourse(String courseUuid) {
        return studentsPerCourse.getOrDefault(courseUuid, Collections.emptySet());
    }

    @Override
    public Set<Course> getAllCoursesForStudent(String studentUuid) {
        return studentsPerCourse.entrySet()
                                .stream()
                                .filter(isStudentAssignedToTheCourse(studentUuid))
                                .map(Entry::getKey)
                                .map(coursesRepository::getOne)
                                .flatMap(c -> c.map(Stream::of).orElseGet(Stream::empty))
                                .collect(Collectors.toSet());

    }

    private Predicate<Entry<String, Set<Student>>> isStudentAssignedToTheCourse(
        String studentUuid) {
        return (entry) -> {
            Set<Student> studentsAssignedToGivenCourse = entry.getValue();
            return studentsAssignedToGivenCourse.stream()
                                                .anyMatch(s -> s.getUuid().equals(studentUuid));
        };
    }
}
