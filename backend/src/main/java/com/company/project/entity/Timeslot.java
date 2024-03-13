package com.company.project.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.util.Date;
import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
//import javax.persistence.;

@Entity
@Table(name = "timetable")
public class Timeslot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idTimetable;

    public int getIdTimetable() {
        return idTimetable;
    }
    public void setIdTimetable(int idTimetable) {
        this.idTimetable = idTimetable;
    }
    //@Temporal(TemporalType.TIME)
    Time start_time;

    public Time getStart_time() {
        return start_time;
    }
    public void setStart_time(Time start_time) {
        this.start_time = start_time;
    }
    
    @Temporal(TemporalType.TIME)
    private Date end_time;

    public Date getEnd_time() {
        return end_time;
    }
    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }
    
    private boolean isSelected;
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
    private String weekDay;

    public String getWeekDay() {
        return weekDay;
    }
    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }
    public Timeslot(){}
    public Timeslot(Time start_time, Date end_time, String weekDay){
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
