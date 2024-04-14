package com.company.project.dto.timetable;

import com.company.project.entity.Weekday;

import java.util.List;

public record TimetableDayDto(Weekday weekday, List<TimeslotDto> timeslots) {
}
