package com.winio94.recruitment.schoolregistration.rest;

import com.winio94.recruitment.schoolregistration.api.Student;
import com.winio94.recruitment.schoolregistration.service.StudentsService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students")
public class StudentsController {

    private final StudentsService service;

    StudentsController(StudentsService service) {
        this.service = service;
    }

    @GetMapping
    public List<Student> getAll() {
        return service.getAll();
    }

    @GetMapping("/{uuid}")
    public Student getOne(@PathVariable String uuid) {
        return service.getOne(uuid);
    }

}
