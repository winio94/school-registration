package com.winio94.recruitment.schoolregistration.service;

import com.winio94.recruitment.schoolregistration.api.Course;
import com.winio94.recruitment.schoolregistration.api.CoursesRepository;
import com.winio94.recruitment.schoolregistration.api.Entity;
import com.winio94.recruitment.schoolregistration.api.NewCourse;
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
    public Course getOne(String uuid) {
        log.info("Fetching course by uuid = {}", uuid);
        return coursesRepository.getOne(uuid)
                                .orElseThrow(Errors.notFoundError(uuid, Entity.COURSE));
    }

    @Override
    public Course create(NewCourse newCourse) {
        log.info("Creating new course with name = {} and code = {}", newCourse.getName(),
                 newCourse.getCode());
        return coursesRepository.create(newCourse);
    }

    @Override
    public void delete(String uuid) {
        log.info("Deleting course with uuid = {}", uuid);
        coursesRepository.getOne(uuid).orElseThrow(Errors.notFoundError(uuid, Entity.COURSE));
        coursesRepository.delete(uuid);
    }
}
