package com.company.project.entity;

import com.company.project.entity.users.Student;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @Column(name = "is_selected")
    private boolean isSelected;


    @OneToMany(mappedBy = "result")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Student> result = new HashSet<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @OneToMany(mappedBy = "timeslot")
    private List<StudentPreference> preferences = new ArrayList<>();

    public Timeslot(Weekday weekday, LocalTime startTime, LocalTime endTime, boolean isSelected) {
        this.weekday = weekday;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isSelected = isSelected;
    }
}
