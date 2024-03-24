package com.company.project.service;

import com.company.project.dto.StudentDto;
import com.company.project.dto.StudentPreferencesDto;
import com.company.project.dto.timetable.TimetableDto;
import com.company.project.entity.Student;
import com.company.project.entity.Timeslot;
import com.company.project.exception.implementations.ResourceNotFoundException;
import com.company.project.mapper.StudentMapper;
import com.company.project.repository.StudentRepository;
import com.company.project.repository.TimeslotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final TimeslotRepository timeslotRepository;
    private final StudentMapper studentMapper;

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
                        .timeSlots()
                        .stream()
                        .map(
                                (ts) ->
                                        timeslotRepository.findByWeekdayAndStartTimeAndEndTime(singleTimetable.weekday(), ts.start_date(), ts.end_date())
                                                .orElseThrow(() -> new ResourceNotFoundException("Slot " + singleTimetable.weekday() + " " + ts.start_date() + " " + ts.end_date() + " not found"))
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
}
