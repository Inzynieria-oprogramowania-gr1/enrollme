package com.company.project.service;

import com.company.project.dto.enrollment.EnrollmentConfigDto;
import com.company.project.dto.enrollment.EnrollmentDto;
import com.company.project.dto.timetable.TimetableDto;
import com.company.project.entity.Enrollment;
import com.company.project.entity.EnrolmentState;
import com.company.project.entity.Timeslot;
import com.company.project.exception.implementations.ForbiddenActionException;
import com.company.project.exception.implementations.ResourceNotFoundException;
import com.company.project.mapper.TimeslotMapper;
import com.company.project.repository.EnrollmentRepository;
import com.company.project.repository.StudentRepository;
import com.company.project.repository.TimeslotRepository;
import com.company.project.schedulers.ScheduledTasks;
import com.company.project.schedulers.TaskType;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class EnrollmentService {
    public final EnrollmentRepository enrollmentRepository;
    public final TimeslotRepository timeslotRepository;
    public final TimeslotMapper timeslotMapper;
    private final StudentRepository studentRepository;
    private final ShareLinkService shareLinkService;
    public final ScheduledTasks scheduledTasks;
    private final ThreadPoolTaskScheduler taskScheduler;


    public EnrollmentDto getEnrollment() {
        Enrollment enrollment = enrollmentRepository
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

    public EnrollmentConfigDto configureEnrollment(Long id, EnrollmentConfigDto configDto, ShareLinkService shareLinkService) {
        LocalDateTime deadline = configDto.deadline();
        int groupAmount = configDto.groupAmount();
        boolean isFormatException = false;

        // TODO extract to validation method (maybe Validator class)

        StringBuilder stringBuilder = new StringBuilder();

        if (groupAmount < 0) {
            isFormatException = true;
            String message = "GroupAmount must be greater than zero";
            stringBuilder.append(message);
        }

        if (deadline != null && deadline.isBefore(LocalDateTime.now())) {
            isFormatException = true;
            String message = "Deadline is before current time";
            stringBuilder.append("\n");
            stringBuilder.append(message);
        }


        if (isFormatException)
            throw new ForbiddenActionException(stringBuilder.toString());


        Enrollment enrollment = enrollmentRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment with id: " + id + "does not exist"));

        updateCloseEnrollmentTask(shareLinkService, deadline, enrollment);


        validateGroupAmount(configDto, enrollment);

        enrollment.setGroupAmount(configDto.groupAmount());
        enrollment.setDeadline(configDto.deadline());
        enrollmentRepository.save(enrollment);
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
                .filter(Timeslot::isSelected).toList();
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

    public void resetEnrollment(Long id) {
        Optional<Enrollment> byId = enrollmentRepository.findById(id);
        byId.ifPresent(enrollment -> {
            enrollment.setState(EnrolmentState.INACTIVE);
            enrollment.setGroupAmount(0);
            enrollment.setDeadline(null);
            enrollment.getTimeslots().forEach(timeslot -> {
                timeslot.setSelected(false);
                timeslot.getResult().forEach(
                        student -> student.setResult(null)
                );
                timeslot.getResult().clear();
                timeslot.getPreferences().clear();
            });

            enrollment.setState(EnrolmentState.ACTIVE);
            enrollmentRepository.save(enrollment);
        });

        shareLinkService.removeAll();

        studentRepository.deleteAllByIdGreaterThan(7L);
    }

    private void updateCloseEnrollmentTask(ShareLinkService shareLinkService, LocalDateTime deadline, Enrollment enrollment) {
        if (deadline != null) {
            if (enrollment.getDeadline() == null) {
                Instant instant = deadline.atZone(ZoneId.of("Europe/Warsaw")).toInstant();
                scheduledTasks.put(TaskType.CLOSE_ENROLLMENT, instant, shareLinkService);

            } else if (!enrollment.getDeadline().isEqual(deadline)) {
                Instant instant = deadline.atZone(ZoneId.of("Europe/Warsaw")).toInstant();
                scheduledTasks.cancelCurrent(TaskType.CLOSE_ENROLLMENT);
                scheduledTasks.put(TaskType.CLOSE_ENROLLMENT, instant, shareLinkService);
            }
        } else if (scheduledTasks.isScheduled(TaskType.CLOSE_ENROLLMENT)) {
            scheduledTasks.cancelCurrent(TaskType.CLOSE_ENROLLMENT);
            scheduledTasks.remove(TaskType.CLOSE_ENROLLMENT);
        }
    }

    private void validateGroupAmount(EnrollmentConfigDto configDto, Enrollment enrollment) {
        boolean isSelectedAny = false;
        for (Timeslot timeslot : enrollment.getTimeslots()) {
            if (timeslot.isSelected()) {
                isSelectedAny = true;
                break;
            }
        }

        if (isSelectedAny && configDto.groupAmount() <= 0)
            throw new ForbiddenActionException("GroupAmount must be greater than zero");
    }
}
