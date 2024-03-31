package com.company.project.dto.enrollment;

import com.company.project.dto.StudentDto;
import com.company.project.dto.timetable.SpecifiedTimeslotDto;

import java.util.List;

public record EnrollmentResultsDto(SpecifiedTimeslotDto timeslotDto, List<StudentDto> studentDto) {
}
