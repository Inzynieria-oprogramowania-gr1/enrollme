package com.company.project.service;

import com.company.project.dto.enrollment.EnrollmentConfigDto;
import com.company.project.dto.enrollment.EnrollmentDto;
import com.company.project.dto.timetable.TimetableDto;
import com.company.project.entity.Enrollment;
import com.company.project.entity.Timeslot;
import com.company.project.exception.implementations.ResourceNotFoundException;
import com.company.project.mapper.TimeslotMapper;
import com.company.project.repository.EnrollmentRepository;
import com.company.project.repository.TimeslotRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class EnrollmentService {
    public final EnrollmentRepository repository;
    public final TimeslotRepository timeslotRepository;
    public final TimeslotMapper timeslotMapper;


    public EnrollmentDto getEnrollment() {
        Enrollment enrollment = repository
                .findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));



        List<Timeslot> timeslots = enrollment.getTimeslots();
        List<TimetableDto> timetableDto = timeslotMapper.mapToTimetableList(timeslots);


        return new EnrollmentDto(
                enrollment.getId(),
                enrollment.getGroupAmount(),
                enrollment.getDeadline(),
                enrollment.getState(),
                timetableDto
        );
    }

    public EnrollmentConfigDto configureEnrollment(Long id, EnrollmentConfigDto configDto) {
        Enrollment enrollment = repository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment with id: " + id + "does not exist"));

        enrollment.setGroupAmount(configDto.groupAmount());
        enrollment.setDeadline(configDto.deadline());
        repository.save(enrollment);
        return configDto;
    }

    public List<TimetableDto> updateTimetable(List<TimetableDto> timetableDto) {
        return updateTimeslots(timeslotMapper.mapToTimeslotList(timetableDto));
    }

    public List<TimetableDto> getTimetable() {
        List<Timeslot> timetableEntities = timeslotRepository.findAll();
        return timeslotMapper.mapToTimetableList(timetableEntities);
    }

    public List<TimetableDto> getSelectedTimetable() {
        List<Timeslot> t = timeslotRepository.findAll().stream()
        .filter(timeslot->timeslot.isSelected()).toList();
        return timeslotMapper.mapToTimetableList(t);
    }

    private List<TimetableDto> updateTimeslots(List<Timeslot> timeslotDtos) {
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
