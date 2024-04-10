package com.company.project.entity;


import com.company.project.entity.users.Student;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class StudentPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, name = "preference_id")
    private Long id;


    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "timeslot_id", nullable = false)
    private Timeslot timeslot;

    @Column(name = "has_student_selected")
    private boolean isSelected;

    @Column(name = "preference_note")
    private String note;


    public StudentPreference(Student student, Timeslot timeslot, boolean isSelected, String note) {
        this.student = student;
        this.timeslot = timeslot;
        this.isSelected = isSelected;
        this.note = note;
    }


}
