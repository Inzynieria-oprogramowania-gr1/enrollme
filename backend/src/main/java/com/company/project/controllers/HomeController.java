package com.company.project.controllers;


import com.company.project.dto.StudentDto;
import com.company.project.mailService.EmailServiceImpl;
import com.company.project.repository.TimeslotRepository;
import com.company.project.service.StudentService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "/demo")
public class HomeController {

    private final StudentService studentService;
    private final EmailServiceImpl emailService;

    public HomeController(StudentService studentService, TimeslotRepository timeslotRepository, EmailServiceImpl emailService) {
        this.studentService = studentService;
        this.emailService = emailService;
        // this.timeslotRepository = timeslotRepository;
    }

    @GetMapping(path = "/hi")
    public @ResponseBody String hello() {
        return "Hello";
    }

    @GetMapping(path = "/send")
    public @ResponseBody String emailSending() {
        try{
            emailService.sendEmail("info.enrollme@gmail.com", "Test", "Hi, world!");
            for(StudentDto student : studentService.getAllStudents()){
                emailService.sendEmail(student.email(), "Enroll is closing in 1 day. Test message", "Please fill your preferences in enroll. Test message");
            }
            return "E-mails have been sent";
        }
        catch (Exception e){
            return e.getMessage();
        }
    }
}
