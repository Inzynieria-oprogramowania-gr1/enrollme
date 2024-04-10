package com.company.project.repository;

import com.company.project.entity.Timeslot;
import com.company.project.entity.Weekday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeslotRepository extends JpaRepository<Timeslot, Long> {
    Optional<Timeslot> findByWeekdayAndStartTimeAndEndTime(Weekday weekday, LocalTime startTime, LocalTime endTime);

    List<Timeslot> findAllBySelected(boolean selected);
}
