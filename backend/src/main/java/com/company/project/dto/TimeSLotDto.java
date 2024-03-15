package com.company.project.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public record TimeSLotDto(LocalTime start_date, LocalTime end_date, boolean is_selected) {
}
