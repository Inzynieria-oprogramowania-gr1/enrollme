package com.company.project.controllers;


import com.company.project.entity.Timeslot;
import com.company.project.service.TimetableService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/teacher/timetable")
public class TimetableController {

    private TimetableService timetableService;

    public TimetableController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @GetMapping
    public @ResponseBody List<Timeslot> showTimetable() {
        System.out.println(timetableService.getInitialTimetable());
        return timetableService.getInitialTimetable();
    }
}
