package com.company.project.dto.timetable;

import java.time.LocalTime;

import com.company.project.entity.Weekday;
import com.fasterxml.jackson.annotation.JsonFormat;

public record SpecifiedTimeslotDto(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime startTime,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm" )
        LocalTime endTime,
        boolean isSelected,
        Weekday weekday
) {
    
}
