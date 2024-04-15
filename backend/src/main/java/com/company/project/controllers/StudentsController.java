package com.company.project.controllers;

import com.company.project.dto.StudentDto;
import com.company.project.dto.preferences.StudentPreferencesDto;
import com.company.project.dto.timetable.TimetableDayDto;
import com.company.project.service.EnrollmentService;
import com.company.project.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = "http://81.28.6.141:3000")
@AllArgsConstructor
@RequestMapping(path = "/students")
public class StudentsController {

    private final StudentService studentService;
    private final EnrollmentService enrollmentService;

    @PostMapping
    public @ResponseBody List<StudentDto> addStudentList(@RequestBody List<String> emails) {
        return studentService.createStudent(emails);
    }


    @GetMapping
    public @ResponseBody ResponseEntity<?> getStudents(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String email,
            HttpServletRequest httpServletRequest
    ) throws BadRequestException {
        if (httpServletRequest.getParameterMap().size() > 1)
            throw new BadRequestException("Bad request");
        if (id != null)
            return ResponseEntity.ok(studentService.getStudentById(id));
        else if (email != null)
            return ResponseEntity.ok(studentService.getStudentByEmail(email));
        else
            return ResponseEntity.ok(studentService.getAllStudents());
    }


    @PutMapping(path = "/{id}/preferences")
    public StudentPreferencesDto addStudentPreferences(@PathVariable("id") Long id, @RequestBody StudentPreferencesDto updatedPreferences) {
        return studentService.updatePreferences(id, updatedPreferences);
    }

    @GetMapping(path = "/{id}/preferences")
    public StudentPreferencesDto getStudentPreferences(@PathVariable("id") Long id) {
        return studentService.getPreferences(id);
    }

    @GetMapping(path = "/timetable")
    @ResponseBody
    public List<TimetableDayDto> getTimeslots() {
        return enrollmentService.getTimetable();
    }
}
