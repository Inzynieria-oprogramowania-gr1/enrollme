package com.company.project.service;

import com.company.project.dto.timetable.ShareLinkDto;
import com.company.project.dto.timetable.TimeSLotDto;
import com.company.project.dto.timetable.TimetableDto;
import com.company.project.entity.ShareLink;
import com.company.project.entity.Timeslot;
import com.company.project.repository.ActiveLinkRepository;
import com.company.project.repository.TimeslotRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TimetableService {
    private final TimeslotRepository timeslotRepository;
    private final ActiveLinkRepository activeLinkRepository;

    public TimetableService(
            TimeslotRepository timeslotRepository,
            ActiveLinkRepository activeLinkRepository
    ) {
        this.timeslotRepository = timeslotRepository;
        this.activeLinkRepository = activeLinkRepository;
    }


    public List<TimetableDto> getTimetable() {
        List<Timeslot> timetableEntities = timeslotRepository.findAll();

        return timeSlotListToTimetableList(timetableEntities);
    }

    private static List<TimetableDto> timeSlotListToTimetableList(List<Timeslot> timetableEntities) {
        return timetableEntities
                .stream()
                .collect(Collectors.groupingBy(Timeslot::getWeekday, Collectors
                        .mapping(timeslot ->
                                        new TimeSLotDto(
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
                        .sorted(Comparator.comparing(TimeSLotDto::start_date)) // Sort time slots by start date
                        .toList()))
                .toList();
    }

    public List<TimetableDto> updateTimetable(List<TimetableDto> timetableDto) {
        List<Timeslot> list = timetableDto
                .stream()
                .flatMap(e -> e.timeSlots()
                        .stream()
                        .map(
                                a -> new Timeslot(
                                        e.weekday(),
                                        a.start_date(),
                                        a.end_date(),
                                        a.is_selected()
                                )
                        )
                )
                .toList();
        return updateTimeslots(list);

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
        return timeSlotListToTimetableList(updatedTimeslots);
    }


    public ShareLinkDto createShareLink(HttpServletRequest request) throws URISyntaxException {
        String host = request.getRequestURL().toString();
        String link = host.substring(0, host.lastIndexOf(new URI(host).getPath()));

        ShareLink savedLink = activeLinkRepository.save(new ShareLink(link + "/students/timetable"));
        return new ShareLinkDto(savedLink.getShareLink());
    }

    public Optional<ShareLinkDto> getShareLink() {
        // Change to findByTimetableUUID if sessions are present
        return activeLinkRepository
                .findAll()
                .stream()
                .findFirst()
                .map(e -> new ShareLinkDto(e.getShareLink()));
    }

}
