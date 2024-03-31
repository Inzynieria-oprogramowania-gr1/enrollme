package com.company.project.dto.enrollment;

import java.time.LocalDateTime;

public record EnrollmentConfigDto(int groupAmount, LocalDateTime deadline) {
}
