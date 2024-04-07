package com.company.project.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "timeslots")
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
    @JoinTable(name = "slot_preference", joinColumns = @JoinColumn(name = "students_id"), inverseJoinColumns = @JoinColumn(name = "timetable_id"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Student> preferences = new HashSet<>();

    @OneToMany(mappedBy = "result")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Student> result = new HashSet<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    public Timeslot(Weekday weekday, LocalTime startTime, LocalTime endTime, boolean isSelected) {
        this.weekday = weekday;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isSelected = isSelected;
    }
}
