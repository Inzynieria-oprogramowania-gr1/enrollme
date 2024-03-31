package com.company.project.controllers;


import com.company.project.dto.enrollment.EnrollmentConfigDto;
import com.company.project.dto.enrollment.EnrollmentDto;
import com.company.project.dto.enrollment.EnrollmentResultsDto;
import com.company.project.dto.timetable.ShareLinkDto;
import com.company.project.dto.timetable.ShareLinkPutDto;
import com.company.project.dto.timetable.TimetableDto;
import com.company.project.entity.EnrolmentState;
import com.company.project.exception.implementations.ForbiddenActionException;
import com.company.project.exception.implementations.ResourceNotFoundException;
import com.company.project.service.EnrollmentService;
import com.company.project.service.ShareLinkService;
import com.company.project.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enrollment")
@CrossOrigin
@AllArgsConstructor
public class EnrollmentController {
    private final EnrollmentService enrollmentService;
    private final ShareLinkService shareLinkService;
    private final StudentService studentService;


    // Gets all data - timetable + config
    @GetMapping
    @ResponseBody
    public EnrollmentDto getEnrollmentById() {
        return enrollmentService.getEnrollmentById(1L);
    }

    @PutMapping("/timetable")
    @ResponseBody
    public List<TimetableDto> saveSelectedTimeSlots(@RequestBody List<TimetableDto> timetableDto) {
        return enrollmentService.updateTimetable(timetableDto);
    }

    @PutMapping("/config")
    @ResponseBody
    public EnrollmentConfigDto configureEnrollment(@RequestBody EnrollmentConfigDto configDto) {
        return enrollmentService.configureEnrollment(1L, configDto);
    }


    @PostMapping("/share")
    @ResponseBody
    public ShareLinkDto createShareLink() {
        return shareLinkService.createShareLink();
    }


    @PatchMapping("/share")
    @ResponseBody
    public ShareLinkDto changeShareLinkState(@RequestBody ShareLinkPutDto requiredState) throws RuntimeException {
        if (requiredState.state() == EnrolmentState.RESULTS_READY) {
            throw new ForbiddenActionException("Cannot change state to - " + requiredState);
        }
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


}
