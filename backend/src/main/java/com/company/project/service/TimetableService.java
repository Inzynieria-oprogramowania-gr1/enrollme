package com.company.project.service;

import com.company.project.dto.timetable.ShareLinkDto;
import com.company.project.dto.timetable.TimetableDto;
import com.company.project.entity.ShareLink;
import com.company.project.entity.Timeslot;
import com.company.project.mapper.ShareLinkMapper;
import com.company.project.mapper.TimeslotMapper;
import com.company.project.repository.ActiveLinkRepository;
import com.company.project.repository.TimeslotRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TimetableService {
    private final TimeslotRepository timeslotRepository;
    private final ActiveLinkRepository activeLinkRepository;
    private final TimeslotMapper timeslotMapper;
    private final ShareLinkMapper shareLinkMapper;

    public List<TimetableDto> getTimetable() {
        List<Timeslot> timetableEntities = timeslotRepository.findAll();
        return timeslotMapper.mapToTimetableList(timetableEntities);
    }

    public List<TimetableDto> updateTimetable(List<TimetableDto> timetableDto) {
        return updateTimeslots(timeslotMapper.mapToTimeslotList(timetableDto));
    }


    public List<TimetableDto> updateTimeslots(List<Timeslot> timeslotDtos) {
        List<Timeslot> timeslots = timeslotRepository.findAll();
        List<Timeslot> updatedTimeslots = timeslotDtos.stream()
                .flatMap(timeslotDto -> timeslots.stream()
                        .filter(timeslot -> timeslot.getWeekday() == timeslotDto.getWeekday() &&
                                timeslot.getStartTime().equals(timeslotDto.getStartTime()) &&
                                timeslot.getEndTime().equals(timeslotDto.getEndTime()))
                        .peek(timeslot -> timeslot.setSelected(timeslotDto.isSelected())))
                .collect(Collectors.toList());

        timeslotRepository.saveAll(updatedTimeslots);
        return timeslotMapper.mapToTimetableList(updatedTimeslots);
    }



}
