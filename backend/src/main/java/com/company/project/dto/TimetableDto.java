package com.company.project.dto;

import com.company.project.entity.Weekday;

import java.util.List;

public record TimetableDto(Weekday weekday, List<TimeSLotDto> timeSlots) {
}
