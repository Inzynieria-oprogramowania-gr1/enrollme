package com.company.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timetable_id", nullable = false, unique = true)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private Weekday weekday;

    @Temporal(TemporalType.TIME)
    private LocalTime startTime;

    @Temporal(TemporalType.TIME)
    private LocalTime endTime;

    private boolean isSelected;


    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "slot_preference",
            joinColumns = @JoinColumn(name = "students_id"),
            inverseJoinColumns = @JoinColumn(name = "timetable_id"))
    private Set<Student> preferences = new HashSet<>();


    @OneToMany(mappedBy = "result")
    private Set<Student> result = new HashSet<>();


    public Timeslot(Weekday weekday, LocalTime startTime, LocalTime endTime, boolean isSelected) {
        this.weekday = weekday;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isSelected = isSelected;
    }
}


