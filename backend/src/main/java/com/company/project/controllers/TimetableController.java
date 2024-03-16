package com.company.project.controllers;


import com.company.project.dto.timetable.ShareLinkDto;
import com.company.project.dto.timetable.TimetableDto;
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
    public ResponseEntity<ShareLinkDto> createShareLink(HttpServletRequest request) {
        try {
            ShareLinkDto link = timetableService.createShareLink(request);
            return ResponseEntity.created(new URI(link.link())).body(link);
        } catch (URISyntaxException e) {
            // Btw this exception is unlikely to occur
            return ResponseEntity.internalServerError().body(new ShareLinkDto("")); // TODO any other ideas what to return?
        }
    }

    @GetMapping("/share")
    @ResponseBody
    public ResponseEntity<ShareLinkDto> getSharedLink() {
        return timetableService
                .getShareLink()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }


}


