package com.company.project.dto.preferences;

import com.company.project.entity.Weekday;

import java.time.LocalTime;

public record PreferredTimeslot(Weekday weekday, LocalTime startTime, LocalTime endTime) {
}
