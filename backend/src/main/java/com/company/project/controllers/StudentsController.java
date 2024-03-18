package com.company.project.controllers;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.company.project.dto.StudentDto;
import com.company.project.dto.StudentPreferencesDto;
import com.company.project.dto.timetable.TimetableDto;
import com.company.project.service.StudentService;

import lombok.AllArgsConstructor;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping(path = "/students")
public class StudentsController {

    private final StudentService studentService;
    
    @PostMapping
    public @ResponseBody List<StudentDto> addStudentList(@RequestBody List<String> emails){
        return studentService.createStudent(emails);
    }
    @GetMapping(params = "id")
    public @ResponseBody StudentDto getStudentById(@RequestParam Long id) {
        return studentService.getStudentById(id);
    }
    @GetMapping(params = "email")
    public @ResponseBody StudentDto getStudentByEmail(@RequestParam String email) {
        return studentService.getStudentByEmail(email);
    }
    @GetMapping
    public @ResponseBody List<StudentDto> getStudents(){
        return studentService.getAllStudents();
    }

    @PostMapping(path = "/{id}/preferences")
    public StudentPreferencesDto addStudentPreferences(@PathVariable("id") Long id, @RequestBody List<TimetableDto> timetable){
        return studentService.addPreferences(id, timetable);
    }
    @GetMapping(path = "/{id}/preferences")
    public StudentPreferencesDto getStudentPreferences(@PathVariable("id") Long id, @RequestBody List<TimetableDto> timetable){
        return studentService.getPreferences(id);
    }
}
