package com.company.project.service;

import com.company.project.dto.TimeSLotDto;
import com.company.project.dto.TimetableDto;
import com.company.project.entity.Timeslot;
import com.company.project.repository.TimeslotRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
                .collect(Collectors.groupingBy(
                        Timeslot::getWeekday,
                        Collectors.mapping(
                                timeslot -> new TimeSLotDto(
                                        timeslot.getStartTime(),
                                        timeslot.getEndTime(),
                                        timeslot.isSelected()
                                ),
                                Collectors.toList()
                        )
                ))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // Sort by weekday ordinal
                .map(entry -> new TimetableDto(
                        entry.getKey(),
                        entry.getValue().stream()
                                .sorted(Comparator.comparing(TimeSLotDto::start_date)) // Sort time slots by start date
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

}
