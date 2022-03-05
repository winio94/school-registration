package com.winio94.recruitment.schoolregistration.rest;

import com.winio94.recruitment.schoolregistration.api.Student;
import java.util.Arrays;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students")
public class StudentsController {

    @GetMapping
    public List<Student> getAll() {
        Student john = new Student("59ee2a28-24fd-49fb-9bd7-eb34f77c6dc1", "John", "Doe");
        Student karen = new Student("0992e41b-78e9-4cb2-83de-5816e7c59283", "Karen", "Simson");
        return Arrays.asList(john, karen);
    }

}
