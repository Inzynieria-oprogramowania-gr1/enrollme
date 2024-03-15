package com.company.project.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.company.project.dto.StudentDto;
import com.company.project.service.StudentService;

import lombok.AllArgsConstructor;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping(path = "/students")
public class StudentsController {

    private final StudentService studentService;

    @PostMapping(path = "/addSingle") // Map ONLY POST Requests
    public @ResponseBody StudentDto addNewUser(@RequestParam String email) {
        return studentService.createStudent(email);
    }
    
    @PostMapping(path = "/addList")
    public @ResponseBody List<StudentDto> addStudentList(@RequestBody List<String> emails){
        return studentService.createStudent(emails);
    }
    @GetMapping(path = "/getAll")
    public @ResponseBody Iterable<StudentDto> getStudents() {
        return studentService.getAllStudents();
    }
}
