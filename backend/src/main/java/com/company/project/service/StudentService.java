package com.company.project.service;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  
  public List<Student> getAllStudents(){
    return Streamable.of(studentRepository.findAll()).toList();
  }

  public Optional<Student> getStudentByEmail(String email){
    return studentRepository.findStudentByEmail(email);
  }

  public Student createStudent(String email){
    return studentRepository.save(new Student(email));
  }

  public List<Student> createStudent(List<String> emails){
    return emails.stream().map
      ((e)->studentRepository.save(new Student(e)))
    .toList();
  }

}
