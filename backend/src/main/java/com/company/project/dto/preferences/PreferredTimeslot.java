package com.company.project.dto.preferences;

import com.company.project.entity.Weekday;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public record PreferredTimeslot(
        Weekday weekday,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime startTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime endTime
) {
}
