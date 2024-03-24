/*
 * Algorithm for grouping students based on their preferences:
 * We iterate over all students. For each student:
 * - We check if they have defined preferences regarding class slots. If so, we assign them to the first available slot from their preferences.
 * - If the student does not have defined preferences, we add them to the list of students without preferences.
 * Next, for each student without defined preferences:
 * - We search for the slot with the fewest assigned students.
 * - We assign the student to that slot.
 * Finally, we return a map where the keys represent the slots (TimetableDto), and the values are lists of students (List<StudentDto>) assigned to those slots.
 */

package com.company.project.algorithm;

import com.company.project.dto.StudentDto;
import com.company.project.dto.StudentPreferencesDto;
import com.company.project.dto.timetable.TimeSLotDto;
import com.company.project.dto.timetable.TimetableDto;
import com.company.project.entity.UserRole;
import com.company.project.service.StudentService;
import com.company.project.service.TimetableService;

import jakarta.annotation.Resource;
import lombok.Getter;

import java.util.*;

import org.springframework.stereotype.Component;

public class GroupingAlgorithm {
    private final StudentService studentService;
    private final List<StudentDto> studentsList;
    private final List<TimetableDto> timetableList;
    @Getter
    Map<TimetableDto, List<StudentDto>> slotAssignments = new HashMap<>();


    public GroupingAlgorithm(StudentService  studentService, TimetableService timetableService) {
        this.studentService = studentService;
        this.studentsList = studentService.getAllStudents();
        this.timetableList = timetableService.getTimetable();
    }

    public Map<TimetableDto, List<StudentDto>> groupStudents() {
        List<StudentDto> withoutPreferences = new ArrayList<>();
        for (StudentDto student : studentsList) {
            if(student.role().equals(UserRole.TEACHER)){
                continue;
            }
            StudentPreferencesDto preferences = studentService.getPreferences(student.id());
            if (preferences.timetables().isEmpty()) {
                withoutPreferences.add(student);
            } else {
                TimetableDto assignedSlot = assignSlot(preferences);
                if (assignedSlot != null) {
                    slotAssignments.putIfAbsent(assignedSlot, new ArrayList<>());
                    slotAssignments.get(assignedSlot).add(student);
                    
                } else {
                    withoutPreferences.add(student);
                }
            }
        }
        if(slotAssignments.size()==0){
            TimetableDto td = timetableList.get(0);
            TimetableDto replaced = new TimetableDto(td.weekday(),List.of(td.timeSlots().get(0)));
            slotAssignments.put(replaced, withoutPreferences);
            return getSlotAssignments();
        }
        for (StudentDto student : withoutPreferences) {
            TimetableDto assignedSlot = assignRestStudents();
            slotAssignments.putIfAbsent(assignedSlot, new ArrayList<>());
            slotAssignments.get(assignedSlot).add(student);
        }

        return getSlotAssignments();
    }

    public TimetableDto assignSlot(StudentPreferencesDto preferences) {
        for (TimetableDto slots : preferences.timetables()) {
            List<TimeSLotDto> filteredSlots = timetableList.stream()
                    .filter(t -> t.weekday().equals(slots.weekday()))
                    .flatMap(t -> t.timeSlots().stream())
                    .toList();
            for (TimeSLotDto slot : slots.timeSlots()) {
                if (filteredSlots.contains(slot)) {
                    return new TimetableDto(slots.weekday(), List.of(slot));
                }
            }
        }
        return null;
    }

    public TimetableDto assignRestStudents() {
        TimetableDto minSlot = null;
        int minStudents = Integer.MAX_VALUE;
        for (TimetableDto slot : slotAssignments.keySet()) {
            if (slotAssignments.get(slot).size() < minStudents) {
                minStudents = slotAssignments.get(slot).size();
                minSlot = slot;
            }
        }
        return minSlot;
    }


}
