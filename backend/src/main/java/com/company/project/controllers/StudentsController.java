package com.company.project.controllers;

import com.company.project.dto.StudentDto;
import com.company.project.dto.StudentPreferencesDto;
import com.company.project.dto.timetable.TimetableDto;
import com.company.project.service.EnrollmentService;
import com.company.project.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping(path = "/students")
public class StudentsController {

    private final StudentService studentService;
    private final EnrollmentService enrollmentService;
    @PostMapping
    public @ResponseBody List<StudentDto> addStudentList(@RequestBody List<String> emails) {
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
    public @ResponseBody List<StudentDto> getStudents() {
        return studentService.getAllStudents();
    }

    @PostMapping(path = "/{id}/preferences")
    public StudentPreferencesDto addStudentPreferences(@PathVariable("id") Long id, @RequestBody List<TimetableDto> timetable) {
        return studentService.addPreferences(id, timetable);
    }

    @GetMapping(path = "/{id}/preferences")
    public StudentPreferencesDto getStudentPreferences(@PathVariable("id") Long id) {
        return studentService.getPreferences(id);
    }


    @GetMapping(path = "/timetable")
    @ResponseBody
    public List<TimetableDto> getTimeslots() {
        return enrollmentService.getTimetable();
    }
}
