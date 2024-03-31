package com.company.project.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "enrollments")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id", unique = true, nullable = false)
    private Long id;

    @Column(name = "group_amount", nullable = false)
    private Integer groupAmount;

    @Column(name = "deadline")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime deadline;

    @Column(name = "state")
    @Enumerated(EnumType.ORDINAL)
    private EnrolmentState state;

    @OneToMany(mappedBy = "enrollment")
    private List<Timeslot> timeslots;


}
