package com.winio94.recruitment.schoolregistration.rest;

import com.winio94.recruitment.schoolregistration.api.Course;
import com.winio94.recruitment.schoolregistration.api.NewCourse;
import com.winio94.recruitment.schoolregistration.api.RegisterStudentToCourse;
import com.winio94.recruitment.schoolregistration.service.CoursesService;
import com.winio94.recruitment.schoolregistration.service.RegistrationService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courses")
public class CoursesController {

    private final CoursesService coursesService;
    private final RegistrationService registrationService;

    CoursesController(CoursesService coursesService, RegistrationService registrationService) {
        this.coursesService = coursesService;
        this.registrationService = registrationService;
    }

    @GetMapping
    public List<Course> getAll() {
        return coursesService.getAll();
    }

    @GetMapping("/{uuid}")
    public Course getOne(@PathVariable String uuid) {
        return coursesService.getOne(uuid);
    }

    @PostMapping
    public ResponseEntity<Course> create(@RequestBody NewCourse newCourse) {
        Course course = coursesService.create(newCourse);
        return new ResponseEntity<>(course, HttpStatus.CREATED);
    }

    @PostMapping("/{uuid}/register")
    public ResponseEntity<Object> register(@PathVariable String uuid,
                                           @RequestBody RegisterStudentToCourse registerStudentToCourse) {
        registrationService.register(uuid, registerStudentToCourse);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Object> delete(@PathVariable String uuid) {
        coursesService.delete(uuid);
        return ResponseEntity.noContent().build();
    }

}