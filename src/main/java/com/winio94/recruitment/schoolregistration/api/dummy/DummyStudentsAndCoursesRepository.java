package com.winio94.recruitment.schoolregistration.api.dummy;

import com.winio94.recruitment.schoolregistration.api.Course;
import com.winio94.recruitment.schoolregistration.api.CoursesRepository;
import com.winio94.recruitment.schoolregistration.api.Student;
import com.winio94.recruitment.schoolregistration.api.StudentsAndCoursesRepository;
import com.winio94.recruitment.schoolregistration.api.StudentsRepository;
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
        studentsRepository.getOne(studentUuid)
                          .ifPresent(students::add);
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
                                .flatMap(c -> c.map(Stream::of)
                                               .orElseGet(Stream::empty))
                                .collect(Collectors.toSet());

    }

    @Override
    public Set<Course> getAllCoursesWithoutAnyStudent() {
        return coursesRepository.getAll()
                                .stream()
                                .filter(c -> !studentsPerCourse.containsKey(c.getUuid()))
                                .collect(Collectors.toSet());
    }

    @Override
    public Set<Student> getAllStudentsNotRegisteredToAnyCourse() {
        Set<String> distinctStudentsUuids = studentsPerCourse.values()
                                                             .stream()
                                                             .flatMap(Set::stream)
                                                             .map(Student::getUuid)
                                                             .collect(Collectors.toSet());
        return studentsRepository.getAll()
                                 .stream()
                                 .filter(c -> !distinctStudentsUuids.contains(c.getUuid()))
                                 .collect(Collectors.toSet());
    }

    @Override
    public int getNumberOfStudentsRegistered(String courseUuid) {
        return Optional.ofNullable(studentsPerCourse.get(courseUuid))
                       .orElse(Collections.emptySet())
                       .size();
    }

    @Override
    public int getNumberOfCoursesForStudent(String studentUuid) {
        return Math.toIntExact(studentsPerCourse.values()
                                                .stream()
                                                .flatMap(Set::stream)
                                                .filter(s -> s.getUuid()
                                                              .equals(studentUuid))
                                                .count());
    }

    private Predicate<Entry<String, Set<Student>>> isStudentAssignedToTheCourse(String studentUuid) {
        return (entry) -> {
            Set<Student> studentsAssignedToGivenCourse = entry.getValue();
            return studentsAssignedToGivenCourse.stream()
                                                .anyMatch(s -> s.getUuid()
                                                                .equals(studentUuid));
        };
    }
}
