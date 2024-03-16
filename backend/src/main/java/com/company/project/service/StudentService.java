package com.company.project.service;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.project.dto.StudentDto;
import com.company.project.dto.StudentPreferencesDto;
import com.company.project.dto.TimeSLotDto;
import com.company.project.dto.TimetableDto;
import com.company.project.entity.Student;
import com.company.project.entity.Timeslot;
import com.company.project.entity.Weekday;
import com.company.project.repository.StudentRepository;
import com.company.project.repository.TimeslotRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentService {
  private final StudentRepository studentRepository;
  private final TimeslotRepository timeslotRepository;
  
  public List<StudentDto> getAllStudents(){
    return studentRepository.findAll().stream()
    .map(
      (student)->new StudentDto(student.getId(), student.getEmail())
    )
    .toList();
  }

  public Optional<StudentDto> getStudentByEmail(String email){
    return studentRepository.findByEmail(email).map(      
      (student)->new StudentDto(student.getId(), student.getEmail())
    );
  }
  public Optional<StudentDto> getStudentById(Long id){
    return studentRepository.findById(id).map(      
      (student)->new StudentDto(student.getId(), student.getEmail())
    );
  }

  public StudentDto createStudent(String email){
    Student student = studentRepository.save(new Student(email));
    return new StudentDto(student.getId(), student.getEmail());
  }

  public List<StudentDto> createStudent(List<String> emails){
    return emails.stream().map
      ((e)->{
        Student student = studentRepository.save(new Student(e));
        return new StudentDto(student.getId(), student.getEmail());
      }
    )
    .toList();
  }

  public StudentPreferencesDto addPreferences(Long id, List<TimetableDto> timetable) throws RuntimeException{

    Student student = studentRepository.findById(id).get();

    timetable.stream().flatMap(singleTimetable->
      singleTimetable
      .timeSlots()
      .stream()
      .map(
        (ts)->
        timeslotRepository.findByWeekdayAndStartTimeAndEndTime(singleTimetable.weekday(),ts.start_date(),ts.end_date())
        )
    ).forEach((bSlot)->{
      Timeslot bSlotReal = bSlot.orElseThrow(()->new RuntimeException("Tu nic nie ma 1d10t0"));
      bSlotReal.getPreferences().add(student);
      student.getPreferences().add(bSlotReal);
      timeslotRepository.save(bSlotReal);
      studentRepository.save(student);
    });

    return new StudentPreferencesDto(id, student.getEmail(), timetable);
  }
  public StudentPreferencesDto getPreferences(Long id) throws RuntimeException{
    Student student = studentRepository.findById(id).get();
    List<TimetableDto> timetables = TimetableService.timeSlotListToTimetableList(student.getPreferences().stream().toList());
    return new StudentPreferencesDto(student.getId(),student.getEmail(),timetables);
  }
}
