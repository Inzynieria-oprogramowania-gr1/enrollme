package com.company.project.repository;

import com.company.project.entity.Timeslot;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeslotRepository extends CrudRepository<Timeslot, Integer> {
}