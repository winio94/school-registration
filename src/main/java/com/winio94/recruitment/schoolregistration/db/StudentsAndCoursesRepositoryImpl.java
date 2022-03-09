package com.winio94.recruitment.schoolregistration.db;

import com.winio94.recruitment.schoolregistration.api.Course;
import com.winio94.recruitment.schoolregistration.api.Student;
import com.winio94.recruitment.schoolregistration.api.StudentsAndCoursesRepository;
import java.util.Set;
import java.util.stream.Collectors;

public class StudentsAndCoursesRepositoryImpl implements StudentsAndCoursesRepository {

    private final DbRegistrationRepository dbRegistrationRepository;
    private final DbStudentsRepository dbStudentsRepository;
    private final DbCourseRepository dbCourseRepository;

    public StudentsAndCoursesRepositoryImpl(DbRegistrationRepository dbRegistrationRepository,
                                            DbStudentsRepository dbStudentsRepository,
                                            DbCourseRepository dbCourseRepository) {
        this.dbRegistrationRepository = dbRegistrationRepository;
        this.dbStudentsRepository = dbStudentsRepository;
        this.dbCourseRepository = dbCourseRepository;
    }

    @Override
    public void addStudentToTheCourse(String courseUuid, String studentUuid) {
        dbRegistrationRepository.save(new DbRegistration(studentUuid, courseUuid));
    }

    @Override
    public Set<Student> getAllStudentsForCourse(String courseUuid) {
        return dbStudentsRepository.findAllByCourse(courseUuid)
                                   .stream()
                                   .map(DbStudent::toDomainModel)
                                   .collect(Collectors.toSet());
    }

    @Override
    public Set<Course> getAllCoursesForStudent(String studentUuid) {
        return dbCourseRepository.findAllByStudent(studentUuid)
                                 .stream()
                                 .map(DbCourse::toDomainModel)
                                 .collect(Collectors.toSet());
    }

    @Override
    public Set<Course> getAllCoursesWithoutAnyStudent() {
        return dbCourseRepository.findAllWithoutStudent()
                                 .stream()
                                 .map(DbCourse::toDomainModel)
                                 .collect(Collectors.toSet());
    }

    @Override
    public Set<Student> getAllStudentsNotRegisteredToAnyCourse() {
        return dbStudentsRepository.findAllWithoutCourse()
                                   .stream()
                                   .map(DbStudent::toDomainModel)
                                   .collect(Collectors.toSet());
    }

    @Override
    public int getNumberOfStudentsRegistered(String courseUuid) {
        return dbRegistrationRepository.getNumberOfStudentsRegistered(courseUuid);
    }

    @Override
    public int getNumberOfCoursesForStudent(String studentUuid) {
        return dbRegistrationRepository.getNumberOfCoursesForStudent(studentUuid);
    }
}
