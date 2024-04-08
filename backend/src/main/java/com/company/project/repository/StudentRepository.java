package com.company.project.repository;

import com.company.project.entity.users.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);


    void deleteAllByIdGreaterThan(Long id);
}
