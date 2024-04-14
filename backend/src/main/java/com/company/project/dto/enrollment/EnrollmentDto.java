package com.company.project.dto.enrollment;

import com.company.project.dto.timetable.TimetableDayDto;
import com.company.project.entity.EnrolmentState;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record EnrollmentDto(
        Long id,
        int groupAmount,
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
        LocalDateTime deadline,
        EnrolmentState state,
        List<TimetableDayDto> timeslots
) {
}
