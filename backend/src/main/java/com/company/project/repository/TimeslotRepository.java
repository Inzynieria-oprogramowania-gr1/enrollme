package com.company.project.repository;

import com.company.project.entity.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeslotRepository extends JpaRepository<Timeslot, Long> {
}