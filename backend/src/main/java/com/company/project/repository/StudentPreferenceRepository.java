package com.company.project.repository;

import com.company.project.entity.StudentPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentPreferenceRepository extends JpaRepository<StudentPreference, Long> {
}
