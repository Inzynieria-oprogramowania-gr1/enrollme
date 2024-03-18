package com.company.project.controllers;


import com.company.project.dto.timetable.ShareLinkDto;
import com.company.project.dto.timetable.TimetableDto;
import com.company.project.exception.implementations.ResourceNotFoundException;
import com.company.project.service.TimetableService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/teacher/timetable")
public class TimetableController {

    private final TimetableService timetableService;


    public TimetableController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @GetMapping
    @ResponseBody
    public List<TimetableDto> showTimetable() {
        return timetableService.getTimetable();
    }

    @PutMapping
    @ResponseBody
    public List<TimetableDto> saveSelectedTimeSlots(@RequestBody List<TimetableDto> timetableDto) {
        return timetableService.updateTimetable(timetableDto);
    }


    @PostMapping("/share")
    @ResponseBody
    public ShareLinkDto createShareLink(HttpServletRequest request) throws URISyntaxException {
        return timetableService.createShareLink(request);
    }

    @GetMapping("/share")
    @ResponseBody
    public ShareLinkDto getSharedLink() {
        return timetableService
                .getShareLink()
                .orElseThrow(() -> new ResourceNotFoundException("Share link not found. Try to generate it first"));

    }


}


