package com.company.project.dto;

import java.util.Date;

public record TimeSLotDto(Date start_date, Date end_date, boolean is_selected) {
}
