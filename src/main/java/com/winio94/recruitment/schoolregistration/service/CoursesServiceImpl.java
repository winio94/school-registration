package com.winio94.recruitment.schoolregistration.service;

import com.winio94.recruitment.schoolregistration.api.Course;
import com.winio94.recruitment.schoolregistration.api.CoursesRepository;
import com.winio94.recruitment.schoolregistration.api.Entity;
import com.winio94.recruitment.schoolregistration.api.NewCourse;
import io.vavr.control.Either;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoursesServiceImpl implements CoursesService {

    private final Logger log = LoggerFactory.getLogger(CoursesServiceImpl.class);
    private final CoursesRepository coursesRepository;

    public CoursesServiceImpl(CoursesRepository coursesRepository) {
        this.coursesRepository = coursesRepository;
    }

    @Override
    public Set<Course> getAll() {
        log.info("Fetching all courses");
        return coursesRepository.getAll();
    }

    @Override
    public Course getOneByUuid(String uuid) {
        log.info("Fetching course by uuid = {}", uuid);
        return coursesRepository.getOneByUuid(uuid)
                                .orElseThrow(Errors.notFoundError(uuid, Entity.COURSE));
    }

    @Override
    public Either<SchoolError, Course> create(NewCourse newCourse) {
        log.info("Creating new course with name = {} and code = {}", newCourse.getName(), newCourse.getCode());
        Optional<Course> course = coursesRepository.getOneByCode(newCourse.getCode());
        if (course.isPresent()) {
            return Either.left(SchoolError.CODE_ALREADY_USED);
        }
        return Either.right(coursesRepository.create(newCourse));
    }

    @Override
    public void delete(String uuid) {
        log.info("Deleting course with uuid = {}", uuid);
        coursesRepository.getOneByUuid(uuid)
                         .orElseThrow(Errors.notFoundError(uuid, Entity.COURSE));
        coursesRepository.delete(uuid);
    }
}
