package com.company.project.repository;

import com.company.project.entity.Student;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends CrudRepository<Student, Integer> {
  Optional<Student> findStudentByEmail(String email);
}