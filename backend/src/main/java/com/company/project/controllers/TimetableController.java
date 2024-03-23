package com.company.project.controllers;


import com.company.project.dto.timetable.ShareLinkDto;
import com.company.project.dto.timetable.ShareLinkPutDto;
import com.company.project.dto.timetable.TimetableDto;
import com.company.project.entity.EnrolmentState;
import com.company.project.exception.implementations.ForbiddenActionException;
import com.company.project.exception.implementations.ResourceNotFoundException;
import com.company.project.service.ShareLinkService;
import com.company.project.service.TimetableService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/teacher/timetable")
@AllArgsConstructor
public class TimetableController {

    private final TimetableService timetableService;
    private final ShareLinkService shareLinkService;



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
    public ShareLinkDto createShareLink() throws URISyntaxException {
        return shareLinkService.createShareLink();
    }

    @PatchMapping("/share")
    @ResponseBody
    public ShareLinkDto changeStateShareLink(@RequestBody ShareLinkPutDto requiredState) throws RuntimeException {
        System.out.println(requiredState);
        if(requiredState.state().equals(EnrolmentState.RESULTS_READY)){
            throw new ForbiddenActionException("Cannot change state to - "+requiredState);
        }
        return shareLinkService.updateShareLink(requiredState.state());
    }

    @GetMapping("/share")
    @ResponseBody
    public ShareLinkDto getSharedLink() {
        ShareLinkDto shareLinkDto = shareLinkService
                                    .getShareLink()
                                    .orElseThrow(() -> new ResourceNotFoundException("Share link not found. Try to generate it first"));
        return shareLinkDto;
    }


}


