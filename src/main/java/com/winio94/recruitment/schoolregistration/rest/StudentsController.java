package com.winio94.recruitment.schoolregistration.rest;

import com.winio94.recruitment.schoolregistration.api.Course;
import com.winio94.recruitment.schoolregistration.api.NewStudent;
import com.winio94.recruitment.schoolregistration.api.Student;
import com.winio94.recruitment.schoolregistration.api.Uuid;
import com.winio94.recruitment.schoolregistration.service.StudentsAndCoursesService;
import com.winio94.recruitment.schoolregistration.service.StudentsService;
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
@RequestMapping("/students")
@Validated
public class StudentsController {

    private final StudentsService studentsService;
    private final StudentsAndCoursesService studentsAndCoursesService;

    StudentsController(StudentsService studentsService,
                       StudentsAndCoursesService studentsAndCoursesService) {
        this.studentsService = studentsService;
        this.studentsAndCoursesService = studentsAndCoursesService;
    }

    @GetMapping
    public Set<Student> getAll(
        @RequestParam(name = "course", required = false) @Uuid String courseUuid,
        @RequestParam(required = false) boolean notRegisteredToAnyCourse) {
        return studentsAndCoursesService.getAllStudentsFiltered(courseUuid,
                                                                notRegisteredToAnyCourse);
    }

    @GetMapping("/{uuid}")
    public Student getOne(@PathVariable @Uuid String uuid) {
        return studentsService.getOne(uuid);
    }

    @GetMapping("/{uuid}/courses")
    public Set<Course> courses(@PathVariable @Uuid String uuid) {
        return studentsAndCoursesService.getAllCoursesFiltered(uuid, false);
    }

    @PostMapping
    public ResponseEntity<Student> create(@RequestBody @Valid NewStudent studentDto) {
        Student newStudent = studentsService.create(studentDto);
        return new ResponseEntity<>(newStudent, HttpStatus.CREATED);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Object> delete(@PathVariable @Uuid String uuid) {
        studentsService.delete(uuid);
        return ResponseEntity.noContent().build();
    }

}
