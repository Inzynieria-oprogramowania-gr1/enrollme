package com.company.project.service;

import com.company.project.dto.StudentDto;
import com.company.project.dto.StudentPreferencesDto;
import com.company.project.dto.enrollment.EnrollmentResultsDto;
import com.company.project.dto.timetable.SpecifiedTimeslotDto;
import com.company.project.dto.timetable.TimeslotDto;
import com.company.project.dto.timetable.TimetableDto;
import com.company.project.entity.Student;
import com.company.project.entity.Timeslot;
import com.company.project.entity.UserRole;
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

    public StudentDto createStudent(String email) {
        Student student = studentRepository.save(new Student(email));
        return studentMapper.mapToStudentDto(student);
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

    public StudentPreferencesDto addPreferences(Long id, List<TimetableDto> timetable) throws RuntimeException {

        Student student = studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        List<Timeslot> timeslots = timetable.stream().flatMap(singleTimetable ->
                singleTimetable
                        .timeslots()
                        .stream()
                        .filter(TimeslotDto::isSelected)
                        .map(
                                (ts) ->
                                        timeslotRepository.findByWeekdayAndStartTimeAndEndTime(singleTimetable.weekday(), ts.startTime(), ts.endTime())
                                                .orElseThrow(() -> new ResourceNotFoundException("Slot " + singleTimetable.weekday() + " " + ts.startTime() + " " + ts.endTime() + " not found"))
                        )
        ).toList();
        timeslots.forEach((bSlot) -> {
            bSlot.getPreferences().add(student);
            student.getPreferences().add(bSlot);
            timeslotRepository.save(bSlot);
            studentRepository.save(student);
        });

        return studentMapper.mapToStudentPreferencesDto(student);
    }


    public StudentPreferencesDto getPreferences(Long id) throws RuntimeException {
        Student student = studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        return studentMapper.mapToStudentPreferencesDto(student);
    }

    public void setResults(Map<TimetableDto, List<StudentDto>> results) {
        for (Entry<TimetableDto, List<StudentDto>> entry : results.entrySet()) {
            TimetableDto timetableDto = entry.getKey();
            TimeslotDto timeSLotDto = timetableDto.timeslots().get(0);
            Timeslot timeslot = timeslotRepository.findByWeekdayAndStartTimeAndEndTime(timetableDto.weekday(), timeSLotDto.startTime(), timeSLotDto.endTime()).orElseThrow(() -> new ResourceNotFoundException("Timeslot not found"));
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
            if (student.getRole().equals(UserRole.TEACHER)) {
                continue;
            }
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