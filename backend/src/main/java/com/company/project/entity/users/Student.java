package com.company.project.entity.users;

import com.company.project.entity.StudentPreference;
import com.company.project.entity.Timeslot;
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
    private Long id;
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id")
    private Timeslot result;

    @OneToMany(mappedBy = "student")
    private Set<StudentPreference> preferences = new HashSet<>();


    public Student(String email) {
        this.email = email;
    }

}
