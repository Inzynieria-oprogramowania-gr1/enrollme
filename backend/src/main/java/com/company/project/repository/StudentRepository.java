package com.company.project.repository;

import com.company.project.entity.Student;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository

public interface StudentRepository extends JpaRepository<Student, Long> {
  Optional<Student> findByEmail(String email);
}