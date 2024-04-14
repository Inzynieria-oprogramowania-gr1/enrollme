package com.company.project.entity.users;

import com.company.project.entity.StudentPreference;
import com.company.project.entity.Timeslot;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.HashCodeExclude;

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

    @OneToMany(mappedBy = "student", orphanRemoval = true, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private Set<StudentPreference> preferences = new HashSet<>();


    public void addPreference(StudentPreference preference) {
        preferences.add(preference);
        preference.setStudent(this);
    }

    public void removePreference(StudentPreference preference) {
        preferences.remove(preference);
        preference.setStudent(null);
    }

    public void removeAllPreferences() {
        preferences.forEach(preference -> preference.setStudent(null));
        preferences.clear();
    }


    public Student(String email) {
        this.email = email;
    }

}
