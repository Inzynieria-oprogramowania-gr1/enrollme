package com.company.project.dto.timetable;

import java.time.LocalTime;

import com.company.project.entity.Weekday;

public record SpecifiedTimeslotDto(LocalTime start_date, LocalTime end_date, boolean is_selected, Weekday weekday) {
    
}
