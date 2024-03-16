package com.company.project.dto;

import java.util.List;

import com.company.project.dto.timetable.TimetableDto;

public record StudentPreferencesDto(Long id, String email, List<TimetableDto> timetables) {
  
}
