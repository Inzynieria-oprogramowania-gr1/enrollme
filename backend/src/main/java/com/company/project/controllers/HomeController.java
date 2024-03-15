package com.company.project.controllers;

import com.company.project.dto.StudentDto;
import com.company.project.entity.Student;
import com.company.project.repository.StudentRepository;
import com.company.project.repository.TimeslotRepository;
import com.company.project.service.StudentService;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "/demo")
public class HomeController {

    private final StudentService studentService;

    public HomeController(StudentService studentService, TimeslotRepository timeslotRepository) {
        this.studentService = studentService;
        // this.timeslotRepository = timeslotRepository;
    }

    @GetMapping(path = "/hi")
    public @ResponseBody String hello() {
        return "Hello";
    }

}
