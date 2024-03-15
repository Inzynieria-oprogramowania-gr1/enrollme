package com.company.project.service;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.project.dto.StudentDto;
import com.company.project.entity.Student;
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
    return studentRepository.findStudentByEmail(email).map(      
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
}
