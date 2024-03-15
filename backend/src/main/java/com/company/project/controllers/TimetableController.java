package com.company.project.controllers;


import com.company.project.dto.TimetableDto;
import com.company.project.service.TimetableService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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
}
