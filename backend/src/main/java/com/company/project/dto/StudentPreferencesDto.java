package com.company.project.dto;

import java.util.List;

public record StudentPreferencesDto(Long id, String email, List<TimetableDto> timetables) {
  
}
