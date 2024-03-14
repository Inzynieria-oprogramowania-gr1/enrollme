package com.company.project.service;

import com.company.project.dto.TimeSLotDto;
import com.company.project.dto.TimetableDto;
import com.company.project.entity.Timeslot;
import com.company.project.repository.TimeslotRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimetableService {
    private final TimeslotRepository timeslotRepository;

    public TimetableService(TimeslotRepository timeslotRepository) {
        this.timeslotRepository = timeslotRepository;
    }

    public List<TimetableDto> getTimetable() {
        List<Timeslot> timetableEntities = timeslotRepository.findAll();
        return timetableEntities.stream()
                .collect(Collectors.groupingBy(Timeslot::getWeekday))
                .entrySet()
                .stream()
                .map(entry -> new TimetableDto(entry.getKey(), entry.getValue().stream().map(this::convertToTimeSlotDTO).collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    private TimeSLotDto convertToTimeSlotDTO(Timeslot entity) {
        return new TimeSLotDto(entity.getStart_time(), entity.getEnd_time(), entity.isSelected());
    }
}
