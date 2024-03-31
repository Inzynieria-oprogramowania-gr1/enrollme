package com.company.project.dto.timetable;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public record TimeslotDto(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")

        LocalTime startTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")

        LocalTime endTime,
        boolean isSelected
) {
}
