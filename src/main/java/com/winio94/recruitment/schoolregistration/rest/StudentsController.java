package com.winio94.recruitment.schoolregistration.rest;

import com.winio94.recruitment.schoolregistration.api.Course;
import com.winio94.recruitment.schoolregistration.api.NewStudent;
import com.winio94.recruitment.schoolregistration.api.Student;
import com.winio94.recruitment.schoolregistration.service.StudentsAndCoursesService;
import com.winio94.recruitment.schoolregistration.service.StudentsService;
import java.util.List;
import java.util.Set;
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
@RequestMapping("/students")
public class StudentsController {

    private final StudentsService service;
    private final StudentsAndCoursesService studentsAndCoursesService;

    StudentsController(StudentsService service,
                       StudentsAndCoursesService studentsAndCoursesService) {
        this.service = service;
        this.studentsAndCoursesService = studentsAndCoursesService;
    }

    @GetMapping
    public List<Student> getAll() {
        return service.getAll();
    }

    @GetMapping("/{uuid}")
    public Student getOne(@PathVariable String uuid) {
        return service.getOne(uuid);
    }

    @GetMapping("/{uuid}/courses")
    public Set<Course> courses(@PathVariable String uuid) {
        return studentsAndCoursesService.getAllCoursesForStudent(uuid);
    }

    @PostMapping
    public ResponseEntity<Student> create(@RequestBody NewStudent studentDto) {
        Student newStudent = service.create(studentDto);
        return new ResponseEntity<>(newStudent, HttpStatus.CREATED);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Object> delete(@PathVariable String uuid) {
        service.delete(uuid);
        return ResponseEntity.noContent().build();
    }

}
