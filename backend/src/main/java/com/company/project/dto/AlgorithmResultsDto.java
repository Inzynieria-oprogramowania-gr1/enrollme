package com.company.project.dto;

import java.util.List;
import java.util.Map;

import com.company.project.dto.timetable.SpecifiedTimeslotDto;
import com.company.project.dto.timetable.TimetableDto;

public record AlgorithmResultsDto(SpecifiedTimeslotDto timeslotDto, List<StudentDto> studentDto) {
}
