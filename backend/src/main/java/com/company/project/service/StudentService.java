package com.company.project.service;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.project.dto.StudentDto;
import com.company.project.dto.StudentPreferencesDto;
import com.company.project.dto.timetable.TimetableDto;
import com.company.project.entity.Student;
import com.company.project.entity.Timeslot;
import com.company.project.exception.implementations.ResourceNotFoundException;
import com.company.project.mapper.StudentMapper;
import com.company.project.mapper.TimeslotMapper;
import com.company.project.repository.StudentRepository;
import com.company.project.repository.TimeslotRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentService {
  private final StudentRepository studentRepository;
  private final TimeslotRepository timeslotRepository;
  private final StudentMapper studentMapper;
  private final TimeslotMapper timeslotMapper;
  
  public List<StudentDto> getAllStudents(){
    return studentRepository.findAll().stream()
    .map(
      (student)->studentMapper.mapToStudentDto(student)
    )
    .toList();
  }

  public StudentDto getStudentByEmail(String email){
    return studentRepository.findByEmail(email).map(      
      (student)->studentMapper.mapToStudentDto(student)
    ).orElseThrow(()-> new ResourceNotFoundException("Student not found"));
  }
  public StudentDto getStudentById(Long id){
    return studentRepository.findById(id).map(      
      (student)->studentMapper.mapToStudentDto(student)
    ).orElseThrow(()-> new ResourceNotFoundException("Student not found"));
  }

  public StudentDto createStudent(String email){
    Student student = studentRepository.save(new Student(email));
    return studentMapper.mapToStudentDto(student);
  }

  public List<StudentDto> createStudent(List<String> emails){
    return emails.stream().map
      ((e)->{
        Student student = studentRepository.save(new Student(e));
        return studentMapper.mapToStudentDto(student);
      }
    )
    .toList();
  }

  public StudentPreferencesDto addPreferences(Long id, List<TimetableDto> timetable) throws RuntimeException{

    Student student = studentRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Student not found"));

    List<Timeslot> timeslots = timetable.stream().flatMap(singleTimetable->
      singleTimetable
      .timeSlots()
      .stream()
      .map(
        (ts)->
        timeslotRepository.findByWeekdayAndStartTimeAndEndTime(singleTimetable.weekday(),ts.start_date(),ts.end_date())
        .orElseThrow(()-> new ResourceNotFoundException("Slot "+singleTimetable.weekday()+" "+ts.start_date()+" "+ts.end_date()+" not found"))
        )
    ).toList();
    
    timeslots.stream().forEach((bSlot)->{
      Timeslot bSlotReal = bSlot;
      bSlotReal.getPreferences().add(student);
      student.getPreferences().add(bSlotReal);
      timeslotRepository.save(bSlotReal);
      studentRepository.save(student);
    });

    return studentMapper.mapToStudentPreferencesDto(student);
  }
  public StudentPreferencesDto getPreferences(Long id) throws RuntimeException{
    Student student = studentRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Student not found"));
    return studentMapper.mapToStudentPreferencesDto(student);
  }
}
