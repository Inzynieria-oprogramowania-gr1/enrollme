package com.company.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.company.project.entity.Student;
import com.company.project.repository.StudentRepository;
import com.company.project.repository.TimeslotRepository;

@RestController
@RequestMapping(path="/demo")
public class HomeController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TimeslotRepository timeslotRepository;

    
    @PostMapping(path="/add") // Map ONLY POST Requests
    public @ResponseBody String addNewUser (@RequestParam String email) {

        Student s = new Student(email);
        studentRepository.save(s);
        return "Saved";
    }

    @GetMapping(path="/hi")
    public @ResponseBody String hello(){
        return "Hello";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Student> getStudents() {
        return studentRepository.findAll();
    }

}
