package com.company.project.dto;

import java.time.LocalTime;

public record TimeSLotDto(LocalTime start_date, LocalTime end_date, boolean is_selected) {
}
