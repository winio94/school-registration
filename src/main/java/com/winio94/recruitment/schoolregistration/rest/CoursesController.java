package com.winio94.recruitment.schoolregistration.rest;

import com.winio94.recruitment.schoolregistration.api.Course;
import com.winio94.recruitment.schoolregistration.api.NewCourse;
import com.winio94.recruitment.schoolregistration.api.Registration;
import com.winio94.recruitment.schoolregistration.api.Student;
import com.winio94.recruitment.schoolregistration.api.Uuid;
import com.winio94.recruitment.schoolregistration.service.CoursesService;
import com.winio94.recruitment.schoolregistration.service.RegistrationResult;
import com.winio94.recruitment.schoolregistration.service.RegistrationService;
import com.winio94.recruitment.schoolregistration.service.StudentsAndCoursesService;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courses")
@Validated
public class CoursesController {

    private final CoursesService coursesService;
    private final RegistrationService registrationService;
    private final StudentsAndCoursesService studentsAndCoursesService;

    CoursesController(CoursesService coursesService,
                      RegistrationService registrationService,
                      StudentsAndCoursesService studentsAndCoursesService) {
        this.coursesService = coursesService;
        this.registrationService = registrationService;
        this.studentsAndCoursesService = studentsAndCoursesService;
    }

    @GetMapping
    public Set<Course> getAll(@RequestParam(name = "student", required = false) @Uuid String studentUuid,
                              @RequestParam(required = false) boolean withoutAnyStudent) {
        return studentsAndCoursesService.getAllCoursesFiltered(studentUuid, withoutAnyStudent);
    }

    @GetMapping("/{uuid}")
    public Course getOne(@PathVariable @Uuid String uuid) {
        return coursesService.getOne(uuid);
    }

    @PostMapping
    public ResponseEntity<Course> create(@RequestBody @Valid NewCourse newCourse) {
        Course course = coursesService.create(newCourse);
        return new ResponseEntity<>(course, HttpStatus.CREATED);
    }

    @PostMapping("/{uuid}/register")
    public ResponseEntity<Object> register(@PathVariable @Uuid String uuid,
                                           @Valid @RequestBody Registration registration) {
        RegistrationResult registrationResult = registrationService.register(uuid, registration);
        return registrationResult.isSuccessful() ? ResponseEntity.ok()
                                                                 .build() : ResponseEntity.badRequest()
                                                                                          .build();
    }

    @GetMapping("/{uuid}/students")
    public Set<Student> students(@PathVariable @Uuid String uuid) {
        return studentsAndCoursesService.getAllStudentsFiltered(uuid, false);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Object> delete(@PathVariable @Uuid String uuid) {
        coursesService.delete(uuid);
        return ResponseEntity.noContent()
                             .build();
    }

}
