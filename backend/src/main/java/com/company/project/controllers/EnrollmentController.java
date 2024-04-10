package com.company.project.controllers;


import com.company.project.dto.StudentDto;
import com.company.project.dto.enrollment.EnrollmentConfigDto;
import com.company.project.dto.enrollment.EnrollmentDto;
import com.company.project.dto.enrollment.EnrollmentResultsDto;
import com.company.project.dto.timetable.ShareLinkDto;
import com.company.project.dto.timetable.ShareLinkPutDto;
import com.company.project.dto.timetable.TimetableDto;
import com.company.project.exception.implementations.ResourceNotFoundException;
import com.company.project.mailService.EmailServiceImpl;
import com.company.project.service.EnrollmentService;
import com.company.project.service.ShareLinkService;
import com.company.project.service.StudentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/enrollment")
@CrossOrigin
@SecurityRequirement(name = "basicAuth")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;
    // TODO maybe move some of share link methods to enrollment
    private final ShareLinkService shareLinkService;
    private final StudentService studentService;
    private final EmailServiceImpl emailService;

    public EnrollmentController(StudentService studentService, EmailServiceImpl emailService,
                                EnrollmentService enrollmentService, ShareLinkService shareLinkService) {
        this.studentService = studentService;
        this.emailService = emailService;
        this.enrollmentService = enrollmentService;
        this.shareLinkService = shareLinkService;
    }

    private static class DeadlineHandler extends Thread {
        EnrollmentController controller;
        LocalDateTime deadline;

        public DeadlineHandler(LocalDateTime deadline, EnrollmentController controller) {
            this.deadline = deadline.minusDays(1);
            this.controller = controller;
        }

        public void run() {
            LocalDateTime localDateTime;
            while (true) {
                try {
                    localDateTime = (LocalDateTime.now()).plusHours(2);
                    if (localDateTime.isAfter(deadline)) {
                        controller.emailSending();
                        break;
                    }
                    sleep(3600000);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    @GetMapping(path = "/send")
    @ResponseBody
    public String emailSending() {
        try {
            emailService.sendEmail("info.enrollme@gmail.com", "Enroll deadline warning has been sent", "");
            for (StudentDto student : studentService.getAllStudents()) {
                // TODO commented to prevent email spam
                // emailService.sendEmail(student.email(), "Enroll is closing in 1 day. Test message", "Please fill your preferences in enroll. Test message");
            }
            return "E-mails have been sent";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    // Gets all data - timetable + config
    @GetMapping
    @ResponseBody
    public EnrollmentDto getEnrollment() {
        return enrollmentService.getEnrollment();
    }

    @PutMapping("/timetable")
    @ResponseBody
    public List<TimetableDto> saveSelectedTimeSlots(@RequestBody List<TimetableDto> timetableDto) {
        return enrollmentService.updateTimetable(timetableDto);
    }

    @PutMapping("/config")
    @ResponseBody
    public EnrollmentConfigDto configureEnrollment(@RequestBody EnrollmentConfigDto configDto) {
        // TODO simplify to just one call to service!
        EnrollmentConfigDto enrollmentConfigDto = enrollmentService.configureEnrollment(configDto.id(), configDto, shareLinkService);


        if (enrollmentService.getEnrollment().deadline() != null) {
            try {
                DeadlineHandler deadlineHandler = new DeadlineHandler(enrollmentService.getEnrollment().deadline(), this);
                deadlineHandler.start();
            } catch (Exception e) {
                System.out.println("Deadline handler hasn't been started. Exception: " + e.getMessage());
            }
        }
        return enrollmentConfigDto;
    }


    @PostMapping("/share")
    @ResponseBody
    public ShareLinkDto createShareLink() {
        return shareLinkService.createShareLink();
    }


    @PatchMapping("/share")
    @ResponseBody
    public ShareLinkDto changeShareLinkState(@RequestBody ShareLinkPutDto requiredState) throws RuntimeException {
        return shareLinkService.updateShareLink(requiredState.state());
    }

    @GetMapping("/share")
    @ResponseBody
    public ShareLinkDto getShareLink() {
        return shareLinkService
                .getShareLink()
                .orElseThrow(() -> new ResourceNotFoundException("Share link not found. Try to generate it first"));
    }


    // TODO Czy studentService powinien odpowiadać za results??? Ja bym to przeniósł do EnrollmentService
    @GetMapping("/results")
    @ResponseBody
    public List<EnrollmentResultsDto> getResults() {
        return studentService.getResults();
    }


    @GetMapping("/reset/{id}")
    public void resetEnrollment(@PathVariable Long id) {
        enrollmentService.resetEnrollment(id);
    }

}
