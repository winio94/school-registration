package com.winio94.recruitment.schoolregistration.db;

import com.winio94.recruitment.schoolregistration.api.Course;
import com.winio94.recruitment.schoolregistration.api.CoursesRepository;
import com.winio94.recruitment.schoolregistration.api.NewCourse;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

public class CoursesRepositoryImpl implements CoursesRepository {

    private final DbCourseRepository dbCourseRepository;

    public CoursesRepositoryImpl(DbCourseRepository dbCourseRepository) {
        this.dbCourseRepository = dbCourseRepository;
    }

    @Override
    public Set<Course> getAll() {
        return dbCourseRepository.findAll()
                                 .stream()
                                 .map(DbCourse::toDomainModel)
                                 .collect(Collectors.toSet());
    }

    @Override
    public Optional<Course> getOneByUuid(String uuid) {
        return dbCourseRepository.findByUuid(uuid)
                                 .map(DbCourse::toDomainModel);
    }

    @Override
    public Optional<Course> getOneByCode(String code) {
        return dbCourseRepository.findByCode(code)
                                 .map(DbCourse::toDomainModel);
    }

    @Override
    public Course create(NewCourse newCourse) {
        DbCourse dbCourse = dbCourseRepository.save(new DbCourse(UUID.randomUUID()
                                                                     .toString(),
                                                                 newCourse.getName(),
                                                                 newCourse.getCode()));
        return dbCourse.toDomainModel();
    }

    @Transactional
    @Override
    public void delete(String uuid) {
        dbCourseRepository.deleteByUuid(uuid);
    }
}
