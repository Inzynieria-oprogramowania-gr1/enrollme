/*
 * Algorithm for grouping students based on their preferences:
 * First initialize slotsFrequency map to keep track of the frequency of each timetable slot.
 * Iterate over all students:
 * - If a student has defined preferences regarding class slots, add them to the list of students with preferences; otherwise, add them to the list of students without preferences.
 * While there are students with preferences:
 * - Get the slot with the highest frequency.
 * - Assign students to the slot until the maximum number of students per group is reached.
 * - Remove students from the list of students with preferences and reduce the frequency of the slot.
 * - Remove the slot from the slotsFrequency map.
 * - Remove slots with 0 or less frequency.
 * - Add the students to the slot.
 * If there are any groups left, add that many slots to the resulting map
 * Assign rest students to the slots with the lowest number of students.
 * Repair any incorrect assignments:
 * - Identify students assigned to slots not in their preferences.
 * - Swap students between slots to correct any mismatches.
 * Finally, return the slotAssignments map where the keys represent the slots (TimetableDto), and the values are lists of students (List<StudentDto>) assigned to those slots.
 */

package com.company.project.algorithm;

import com.company.project.dto.StudentDto;
import com.company.project.dto.StudentPreferencesDto;
import com.company.project.dto.timetable.TimeslotDto;
import com.company.project.dto.timetable.TimetableDto;
import com.company.project.service.EnrollmentService;
import com.company.project.service.StudentService;
import lombok.Getter;

import java.util.*;

public class GroupingAlgorithm {
    private final StudentService studentService;
    private final List<StudentDto> studentsList;
    private final List<TimetableDto> timetableList;
    private final int groupAmount;
    private final int maxStudentsPerGroup;
    @Getter
    Map<TimetableDto, List<StudentDto>> slotAssignments = new HashMap<>();


    public GroupingAlgorithm(StudentService studentService, EnrollmentService enrollmentService) {
        this.studentService = studentService;
        this.studentsList = studentService.getAllStudents();
        this.timetableList = enrollmentService.getTimetable()
                .stream()
                .map(e -> {
                    List<TimeslotDto> dto = e.timeslots().stream().filter(TimeslotDto::isSelected).toList();
                    return new TimetableDto(e.weekday(), dto);
                }).toList();
        this.groupAmount = Math.min(enrollmentService.getGroupAmount(), timetableList.size());
        if (groupAmount == 0) {
            this.maxStudentsPerGroup = 0;
        } else {
            this.maxStudentsPerGroup = studentsList.size() / groupAmount;
        }
    }

    public Map<TimetableDto, List<StudentDto>> groupStudents() {
        Map<TimetableDto, Integer> slotsFrequency = getSlotsFrequency();
        List<StudentDto> withoutPreferences = new ArrayList<>();
        List<StudentDto> withPreferences = new ArrayList<>();
        for (StudentDto student : studentsList) {
            StudentPreferencesDto preferences = studentService.getPreferences(student.id());
            if (preferences.timetables().isEmpty()) {
                withoutPreferences.add(student);
            } else {
                withPreferences.add(student);
            }
        }

        Map<TimetableDto, Integer> slotsFrequencyCopy = new HashMap<>(slotsFrequency);

        while (!withPreferences.isEmpty()) {
            if (slotAssignments.size() == groupAmount) {
                break;
            }

            TimetableDto bestSlot = getBestSlot(slotsFrequency);
            if (bestSlot == null) {
                break;
            }
            ArrayList<StudentDto> students = new ArrayList<>();
            ArrayList<StudentDto> studentsCopy = new ArrayList<>(withPreferences);

            // assign students to the best slot
            for (StudentDto student : studentsCopy) {
                if (studentService.getPreferences(student.id()).timetables().contains(bestSlot) && slotsFrequency.containsKey(bestSlot)) {
                    students.add(student);
                    withPreferences.remove(student);
                    for (TimetableDto slot : studentService.getPreferences(student.id()).timetables()) {
                        if (slotsFrequency.containsKey(slot)) {
                            slotsFrequency.put(slot, slotsFrequency.get(slot) - 1);
                        }
                    }
                    if (students.size() >= maxStudentsPerGroup) {
                        slotsFrequency.remove(bestSlot);
                        break;
                    }
                }
            }

            // remove slots with 0 or less frequency
            for (TimetableDto slot : new ArrayList<>(slotsFrequency.keySet())) {
                if (slotsFrequency.get(slot) <= 0 || (slotAssignments.containsKey(slot) && slotAssignments.get(slot).size() >= maxStudentsPerGroup)) {
                    slotsFrequency.remove(slot);
                }
            }

            // add students to the slot
            if (!students.isEmpty()) {
                slotAssignments.put(bestSlot, students);
            }
        }
        int restGroups = groupAmount - slotAssignments.size();

        for (TimetableDto slot : slotsFrequencyCopy.keySet()) {
            if (!slotAssignments.containsKey(slot)) {
                slotAssignments.put(slot, new ArrayList<>());
                restGroups--;
                if (restGroups == 0) {
                    break;
                }
            }
            if (restGroups == 0) {
                break;
            }
        }

        // assign rest of the students with preferences
        if (!withPreferences.isEmpty()) {
            for (StudentDto student : withPreferences) {
                TimetableDto assignedSlot = assignRestStudents();
                slotAssignments.putIfAbsent(assignedSlot, new ArrayList<>());
                slotAssignments.get(assignedSlot).add(student);
            }
        }

        // assign students without preferences
        for (StudentDto student : withoutPreferences) {
            TimetableDto assignedSlot = assignRestStudents();
            slotAssignments.putIfAbsent(assignedSlot, new ArrayList<>());
            slotAssignments.get(assignedSlot).add(student);
        }


        repairAssignments();

        return getSlotAssignments();
    }

    public Map<TimetableDto, Integer> getSlotsFrequency() {
        Map<TimetableDto, Integer> slotsFrequency = new HashMap<>();
        for (TimetableDto slot : timetableList) {
            slotsFrequency.put(slot, 0);
        }
        for (StudentDto student : studentsList) {
            StudentPreferencesDto preferences = studentService.getPreferences(student.id());
            for (TimetableDto slot : preferences.timetables()) {
                slotsFrequency.put(slot, slotsFrequency.get(slot) + 1);
            }
        }
        return slotsFrequency;
    }

    public TimetableDto getBestSlot(Map<TimetableDto, Integer> slotsFrequency) {
        TimetableDto bestSlot = null;
        int maxFrequency = Integer.MIN_VALUE;
        for (TimetableDto slot : slotsFrequency.keySet()) {
            if (slotsFrequency.get(slot) > maxFrequency && slotsFrequency.get(slot) > 0) {
                maxFrequency = slotsFrequency.get(slot);
                bestSlot = slot;
            }
        }
        return bestSlot;
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

    public void repairAssignments() {
        Map<StudentDto, TimetableDto> wrongAssigned = new HashMap<>();
        for (TimetableDto slot : slotAssignments.keySet()) {
            for (StudentDto student : slotAssignments.get(slot)) {
                if (!studentService.getPreferences(student.id()).timetables().contains(slot)) {
                    wrongAssigned.put(student, slot);
                }
            }
        }

        outerLoop:
        for (StudentDto student : wrongAssigned.keySet()) {
            StudentPreferencesDto preferences = studentService.getPreferences(student.id());
            TimetableDto wrongSlot = wrongAssigned.get(student);
            for (TimetableDto slot : preferences.timetables()) {
                if (slotAssignments.containsKey(slot)) {
                    Iterator<StudentDto> iterator = slotAssignments.get(slot).iterator();
                    while (iterator.hasNext()) {
                        StudentDto studentToChange = iterator.next();
                        if (studentService.getPreferences(studentToChange.id()).timetables().isEmpty() ||
                                studentService.getPreferences(studentToChange.id()).timetables().contains(wrongSlot)) {
                            TimetableDto slotToChange = null;
                            for (TimetableDto s : slotAssignments.keySet()) {
                                if (slotAssignments.get(s).contains(student)) {
                                    slotToChange = s;
                                    break;
                                }
                            }
                            iterator.remove();
                            slotAssignments.get(slot).add(student);
                            slotAssignments.get(slotToChange).remove(student);
                            slotAssignments.get(slotToChange).add(studentToChange);
                            continue outerLoop;
                        }
                    }
                }
            }
        }
    }
}
