package com.winio94.recruitment.schoolregistration.service;

import com.winio94.recruitment.schoolregistration.api.Course;
import com.winio94.recruitment.schoolregistration.api.CoursesRepository;
import com.winio94.recruitment.schoolregistration.api.NewCourse;
import com.winio94.recruitment.schoolregistration.api.RegisterStudentToCourse;
import java.util.List;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CoursesServiceImpl implements CoursesService {

    private final Logger log = LoggerFactory.getLogger(CoursesServiceImpl.class);
    private final CoursesRepository coursesRepository;

    public CoursesServiceImpl(CoursesRepository coursesRepository) {
        this.coursesRepository = coursesRepository;
    }

    @Override
    public List<Course> getAll() {
        log.info("Fetching all courses");
        return coursesRepository.getAll();
    }

    @Override
    public Course getOne(String uuid) {
        log.info("Fetching course by uuid = {}", uuid);
        return coursesRepository.getOne(uuid).orElseThrow(notFoundError(uuid));
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
        coursesRepository.getOne(uuid).orElseThrow(notFoundError(uuid));
        coursesRepository.delete(uuid);
    }

    @Override
    public void register(String uuid, RegisterStudentToCourse registerStudentToCourse) {

    }

    private Supplier<ResponseStatusException> notFoundError(String uuid) {
        return () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(
            "Course with uuid = %s does not exists", uuid));
    }
}
