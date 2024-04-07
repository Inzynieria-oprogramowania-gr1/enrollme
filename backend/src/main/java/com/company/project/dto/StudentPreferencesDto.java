package com.company.project.dto;

import com.company.project.dto.timetable.TimetableDto;

import java.util.List;

public record StudentPreferencesDto(Long id, String email, List<TimetableDto> timetables) {

}
