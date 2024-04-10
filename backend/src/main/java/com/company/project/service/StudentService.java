package com.company.project.service;

import com.company.project.dto.StudentDto;
import com.company.project.dto.enrollment.EnrollmentResultsDto;
import com.company.project.dto.preferences.PreferredTimeslot;
import com.company.project.dto.preferences.SinglePreference;
import com.company.project.dto.preferences.StudentPreferencesDto;
import com.company.project.dto.timetable.SpecifiedTimeslotDto;
import com.company.project.dto.timetable.TimeslotDto;
import com.company.project.dto.timetable.TimetableDayDto;
import com.company.project.entity.StudentPreference;
import com.company.project.entity.Timeslot;
import com.company.project.entity.users.Student;
import com.company.project.exception.implementations.ForbiddenActionException;
import com.company.project.exception.implementations.ResourceNotFoundException;
import com.company.project.mapper.StudentMapper;
import com.company.project.mapper.TimeslotMapper;
import com.company.project.repository.StudentRepository;
import com.company.project.repository.TimeslotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Primary
@Service
@Transactional
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final TimeslotRepository timeslotRepository;
    private final StudentMapper studentMapper;
    private final TimeslotMapper timeslotMapper;

    public List<StudentDto> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(
                        studentMapper::mapToStudentDto
                )
                .toList();
    }

    public StudentDto getStudentByEmail(String email) {
        return studentRepository.findByEmail(email).map(
                studentMapper::mapToStudentDto
        ).orElseThrow(() -> new ResourceNotFoundException("Student not found"));
    }

    public StudentDto getStudentById(Long id) {
        return studentRepository.findById(id).map(
                studentMapper::mapToStudentDto
        ).orElseThrow(() -> new ResourceNotFoundException("Student not found"));
    }

    public List<StudentDto> createStudent(List<String> emails) {
        return emails.stream().map
                        ((e) -> {
                                    Student student = studentRepository.save(new Student(e));
                                    return studentMapper.mapToStudentDto(student);
                                }
                        )
                .toList();
    }


    public StudentPreferencesDto updatePreferences(Long id, StudentPreferencesDto updatedPreferences) throws RuntimeException {
        Student student = studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        List<Timeslot> timeslots = updatedPreferences
                .preferences()
                .stream()
                .flatMap(timetableDay ->
                        timetableDay
                                .timeslots()
                                .stream()
                                .map(
                                        ts -> {
                                            Timeslot timeslot = timeslotRepository.findByWeekdayAndStartTimeAndEndTime(timetableDay.weekday(), ts.startTime(), ts.endTime()).
                                                    orElseThrow(() -> new ResourceNotFoundException("Slot " + timetableDay.weekday() + " " + ts.startTime() + " " + ts.endTime() + " not found"));

                                            if (!timeslot.isSelected())
                                                throw new ForbiddenActionException("Slot " + timetableDay.weekday() + " " + ts.startTime() + " " + ts.endTime() + " has not been selected by teacher!");
                                            return timeslot;
                                        }

                                )
                ).toList();


        timeslots.forEach((timeslot) -> {
            timeslot.getPreferences().add(new StudentPreference(
                    student,
                    timeslot,
                    updatedPreferences.isSelected(),
                    updatedPreferences.note())
            );

            timeslotRepository.save(timeslot);
        });

        return updatedPreferences;
    }


    public StudentPreferencesDto getPreferences(Long id) throws RuntimeException {
        Student student = studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        List<SinglePreference> preferences = student.getPreferences()
                .stream()
                .map(
                        preference -> {
                            Timeslot timeslot = preference.getTimeslot();

                            PreferredTimeslot preferredTimeslot = new PreferredTimeslot(
                                    timeslot.getWeekday(),
                                    timeslot.getStartTime(),
                                    timeslot.getEndTime()
                            );


                            String note = preference.getNote();
                            boolean selected = preference.isSelected();

                            return new SinglePreference(preferredTimeslot, selected, note);
                        }
                ).toList();

        return new StudentPreferencesDto(id, student.getEmail(), preferences);
    }


    public void setResults(Map<TimetableDayDto, List<StudentDto>> results) {
        for (Entry<TimetableDayDto, List<StudentDto>> entry : results.entrySet()) {
            TimetableDayDto timetableDayDto = entry.getKey();
            TimeslotDto timeSLotDto = timetableDayDto.timeslots().get(0);
            Timeslot timeslot = timeslotRepository.findByWeekdayAndStartTimeAndEndTime(timetableDayDto.weekday(), timeSLotDto.startTime(), timeSLotDto.endTime()).orElseThrow(() -> new ResourceNotFoundException("Timeslot not found"));
            for (StudentDto s : entry.getValue()) {
                Student student = studentRepository.findById(s.id()).orElseThrow(() -> new ResourceNotFoundException("Student not found"));
                student.setResult(timeslot);
                studentRepository.save(student);
            }
            timeslotRepository.save(timeslot);
        }
    }


    public List<EnrollmentResultsDto> getResults() {
        List<EnrollmentResultsDto> resultsDtos = new LinkedList<>();
        List<SpecifiedTimeslotDto> timeslots = timeslotRepository
                .findAll()
                .stream()
                .map(timeslotMapper::mapToSpecifiedTimeslotDto)
                .toList();
        List<Student> studentDtos = studentRepository
                .findAll();
        Map<SpecifiedTimeslotDto, List<StudentDto>> mapTimeStudents = new HashMap<>();
        for (SpecifiedTimeslotDto dto : timeslots) {
            mapTimeStudents.put(dto, new LinkedList<>());
        }
        for (Student student : studentDtos) {
            SpecifiedTimeslotDto sp = timeslotMapper.mapToSpecifiedTimeslotDto(student.getResult());
            if (mapTimeStudents.get(sp) == null) {
                throw new ResourceNotFoundException("Results not set");
            }
            mapTimeStudents.get(sp).add(studentMapper.mapToStudentDto(student));
        }
        for (Entry<SpecifiedTimeslotDto, List<StudentDto>> l : mapTimeStudents.entrySet()) {
            if (!l.getValue().isEmpty()) {
                resultsDtos.add(new EnrollmentResultsDto(l.getKey(), l.getValue()));
            }


        }
        return resultsDtos;
    }
}