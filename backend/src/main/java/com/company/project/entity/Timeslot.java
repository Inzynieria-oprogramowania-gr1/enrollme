package com.company.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "timetable")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Timeslot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idTimetable;


    //    @Temporal(TemporalType.TIME)
    Time start_time;

    @Temporal(TemporalType.TIME)
    private Date end_time;

    private boolean isSelected;

    private String weekDay;


    public Timeslot(Time start_time, Date end_time, String weekDay) {
        this.start_time = start_time;
        this.end_time = end_time;
        this.weekDay = weekDay;
    }

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "slot_preference",
            joinColumns = @JoinColumn(name = "students_id"),
            inverseJoinColumns = @JoinColumn(name = "timetable_id"))
    private Set<Student> preferences = new HashSet<>();


    @OneToMany(mappedBy = "result")
    private Set<Student> result = new HashSet<>();
}
