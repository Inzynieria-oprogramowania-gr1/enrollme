package com.company.project.dto;

import com.company.project.entity.UserRole;

public record StudentDto(Long id, String email, UserRole role) {
}
