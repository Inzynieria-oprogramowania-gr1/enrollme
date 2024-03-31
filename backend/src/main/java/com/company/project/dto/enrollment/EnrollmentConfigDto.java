package com.company.project.dto.enrollment;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record EnrollmentConfigDto
        (Long id,
         int groupAmount,
         @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
         LocalDateTime deadline
        ) {
}
