package com.company.project.controllers;

import com.company.project.entity.Student;
import com.company.project.repository.StudentRepository;
import com.company.project.repository.TimeslotRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/demo")
public class HomeController {


    private final StudentRepository studentRepository;


    private final TimeslotRepository timeslotRepository;


    public HomeController(StudentRepository studentRepository, TimeslotRepository timeslotRepository) {
        this.studentRepository = studentRepository;
        this.timeslotRepository = timeslotRepository;
    }

    @PostMapping(path = "/add") // Map ONLY POST Requests
    public @ResponseBody String addNewUser(@RequestParam String email) {

        Student s = new Student(email);
        studentRepository.save(s);
        return "Saved";
    }

    @GetMapping(path = "/hi")
    public @ResponseBody String hello() {
        return "Hello";
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Student> getStudents() {
        return studentRepository.findAll();
    }

}
