package com.company.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "students")
@NoArgsConstructor
@AllArgsConstructor
@Data

public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "students_id", nullable = false, unique = true)
    private Long id;

    private String email;


    public Student(String email) {
        this.email = email;
    }


   @ManyToMany(mappedBy = "preferences")
   private Set<Timeslot> preferences = new HashSet<>();

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "slot_id")
   private Timeslot result;
}
