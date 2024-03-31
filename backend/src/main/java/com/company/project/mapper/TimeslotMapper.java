package com.company.project.mapper;

import com.company.project.dto.timetable.SpecifiedTimeslotDto;
import com.company.project.dto.timetable.TimeslotDto;
import com.company.project.dto.timetable.TimetableDto;
import com.company.project.entity.Timeslot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public abstract class TimeslotMapper {
    @Mapping(source = "weekday", target = "weekday")
    @Mapping(source = "startTime", target = "startTime")
    @Mapping(source = "endTime", target = "endTime")
    @Mapping(source = "selected", target = "isSelected")
    public abstract SpecifiedTimeslotDto mapToSpecifiedTimeslotDto(Timeslot timeslot);

    public List<TimetableDto> mapToTimetableList(List<Timeslot> timetableEntities) {
        return timetableEntities
                .stream()
                .collect(Collectors.groupingBy(Timeslot::getWeekday, Collectors
                        .mapping(timeslot ->
                                        new TimeslotDto(
                                                timeslot.getStartTime(),
                                                timeslot.getEndTime(),
                                                timeslot.isSelected()),
                                Collectors.toList())))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey()) // Sort by weekday ordinal
                .map(entry -> new TimetableDto(entry.getKey(), entry
                        .getValue()
                        .stream()
                        .sorted(Comparator.comparing(TimeslotDto::startTime)) // Sort time slots by start date
                        .toList()))
                .toList();
    }

    public List<Timeslot> mapToTimeslotList(List<TimetableDto> timetableDtos) {
        return timetableDtos
                .stream()
                .flatMap(e -> e.timeslots()
                        .stream()
                        .map(
                                a -> new Timeslot(
                                        e.weekday(),
                                        a.startTime(),
                                        a.endTime(),
                                        a.isSelected()
                                )
                        )
                )
                .toList();
    }

    public Set<Timeslot> mapToTimeslotSet(List<TimetableDto> timetableDtos) {
        return Set.copyOf(this.mapToTimeslotList(timetableDtos));
    }

    public List<TimetableDto> mapToTimetableList(Set<Timeslot> timetableDtos) {
        return this.mapToTimetableList(List.copyOf(timetableDtos));
    }


}
