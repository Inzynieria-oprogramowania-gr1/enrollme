package com.company.project.service;

import com.company.project.entity.Timeslot;
import com.company.project.repository.TimeslotRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimetableService {
    private final TimeslotRepository timeslotRepository;

    public TimetableService(TimeslotRepository timeslotRepository) {
        this.timeslotRepository = timeslotRepository;
    }


    public List<Timeslot> getInitialTimetable() {
        System.out.println(timeslotRepository.findAll());
        return timeslotRepository.findAll();
    }
}
