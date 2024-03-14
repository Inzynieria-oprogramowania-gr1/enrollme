package com.company.project.controllers;

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

    @PostMapping(path = "/students/addSingle") // Map ONLY POST Requests
    public @ResponseBody Student addNewUser(@RequestParam String email) {
        return studentService.createStudent(email);
    }
    
    @PostMapping(path = "/students/addList")
    public @ResponseBody List<Student> addStudentList(@RequestBody List<String> emails){
        return studentService.createStudent(emails);
    }

    @GetMapping(path = "/hi")
    public @ResponseBody String hello() {
        return "Hello";
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Student> getStudents() {
        return studentService.getAllStudents();
    }

}
