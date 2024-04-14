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
import com.company.project.dto.preferences.PreferredTimeslot;
import com.company.project.dto.preferences.SinglePreference;
import com.company.project.dto.preferences.StudentPreferencesDto;
import com.company.project.dto.timetable.TimeslotDto;
import com.company.project.dto.timetable.TimetableDayDto;
import com.company.project.service.EnrollmentService;
import com.company.project.service.StudentService;
import lombok.Getter;

import java.util.*;

public class GroupingAlgorithm {
    private final StudentService studentService;
    private final List<StudentDto> studentsList;
    private final List<TimetableDayDto> timetableList;
    private final int groupAmount;
    private final int maxStudentsPerGroup;
  
    @Getter
    Map<PreferredTimeslot, List<StudentDto>> slotAssignments = new HashMap<>();


    public GroupingAlgorithm(StudentService studentService, EnrollmentService enrollmentService) {
        this.studentService = studentService;
        this.studentsList = studentService.getAllStudents();
        this.timetableList = enrollmentService.getSelectedTimetable();
        this.groupAmount = Math.min(enrollmentService.getGroupAmount(), timetableList.size());
        if (groupAmount == 0) {
            this.maxStudentsPerGroup = 0;
        } else {
            this.maxStudentsPerGroup = studentsList.size() / groupAmount;
        }
    }


    public Map<PreferredTimeslot, List<StudentDto>> groupStudents() {
        Map<PreferredTimeslot, Integer> slotsFrequency = getSlotsFrequency();
        List<StudentDto> withoutPreferences = new ArrayList<>();
        List<StudentDto> withPreferences = new ArrayList<>();
        for (StudentDto student : studentsList) {
            StudentPreferencesDto preferences = studentService.getPreferences(student.id());
            if (preferences.preferences().isEmpty()) {
                withoutPreferences.add(student);
            } else {
                withPreferences.add(student);
            }
        }

        Map<PreferredTimeslot, Integer> slotsFrequencyCopy = new HashMap<>(slotsFrequency);

        while (!withPreferences.isEmpty()) {
            if (slotAssignments.size() == groupAmount) {
                break;
            }

            PreferredTimeslot bestSlot = getBestSlot(slotsFrequency);

            if (bestSlot == null) {
                break;
            }
            ArrayList<StudentDto> students = new ArrayList<>();
            ArrayList<StudentDto> studentsCopy = new ArrayList<>(withPreferences);

            // assign students to the best slot
            for (StudentDto student : studentsCopy) {
                List<PreferredTimeslot> studentSlots = studentService.getPreferences(student.id()).preferences().stream().map(SinglePreference::timeslot).toList();
                if (studentSlots.contains(bestSlot) && slotsFrequency.containsKey(bestSlot)) {
                    students.add(student);
                    withPreferences.remove(student);
                    for (SinglePreference slot : studentService.getPreferences(student.id()).preferences()) {
                        PreferredTimeslot timeslot = new PreferredTimeslot(slot.timeslot().weekday(), slot.timeslot().startTime(), slot.timeslot().endTime());
                        if (slotsFrequency.containsKey(timeslot)) {
                            slotsFrequency.put(timeslot, slotsFrequency.get(timeslot) - 1);
                        }
                    }
                    if (students.size() >= maxStudentsPerGroup) {
                        slotsFrequency.remove(bestSlot);
                        break;
                    }
                }
            }

            // remove slots with 0 or less frequency
            for (PreferredTimeslot slot : new ArrayList<>(slotsFrequency.keySet())) {
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

        for (PreferredTimeslot slot : slotsFrequencyCopy.keySet()) {
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
                PreferredTimeslot assignedSlot = assignRestStudents();
                slotAssignments.putIfAbsent(assignedSlot, new ArrayList<>());
                slotAssignments.get(assignedSlot).add(student);
            }
        }

        // assign students without preferences
        for (StudentDto student : withoutPreferences) {
            PreferredTimeslot assignedSlot = assignRestStudents();
            slotAssignments.putIfAbsent(assignedSlot, new ArrayList<>());
            slotAssignments.get(assignedSlot).add(student);
        }

        repairAssignments();

        return getSlotAssignments();
    }

    public Map<PreferredTimeslot, Integer> getSlotsFrequency() {
        Map<PreferredTimeslot, Integer> slotsFrequency = new HashMap<>();
        timetableList.forEach(timetableDayDto -> {
            List<TimeslotDto> timeslots = timetableDayDto.timeslots();
            timeslots.forEach(timeslot -> {
                PreferredTimeslot preferredTimeslot = new PreferredTimeslot(timetableDayDto.weekday(), timeslot.startTime(), timeslot.endTime());
                slotsFrequency.put(preferredTimeslot, 0);
            });
        });

        for (StudentDto student : studentsList) {
            StudentPreferencesDto preferences = studentService.getPreferences(student.id());
            preferences.preferences().forEach(preference -> {
                slotsFrequency.put(preference.timeslot(), slotsFrequency.get(preference.timeslot()) + 1);
            });

        }
        return slotsFrequency;
    }

    public PreferredTimeslot getBestSlot(Map<PreferredTimeslot, Integer> slotsFrequency) {
        PreferredTimeslot bestSlot = null;
        int maxFrequency = Integer.MIN_VALUE;
        for (PreferredTimeslot slot : slotsFrequency.keySet()) {
            if (slotsFrequency.get(slot) > maxFrequency && slotsFrequency.get(slot) > 0) {
                maxFrequency = slotsFrequency.get(slot);
                bestSlot = new PreferredTimeslot(slot.weekday(), slot.startTime(), slot.endTime());
            }
        }
        return bestSlot;
    }

    public PreferredTimeslot assignRestStudents() {
        PreferredTimeslot minSlot = null;
        int minStudents = Integer.MAX_VALUE;
        for (PreferredTimeslot slot : slotAssignments.keySet()) {
            if (slotAssignments.get(slot).size() < minStudents) {
                minStudents = slotAssignments.get(slot).size();
                minSlot = slot;
            }
        }
        return minSlot;
    }

    public void repairAssignments() {
        Map<StudentDto, PreferredTimeslot> wrongAssigned = new HashMap<>();
        for (PreferredTimeslot slot : slotAssignments.keySet()) {
            for (StudentDto student : slotAssignments.get(slot)) {
                List<PreferredTimeslot> studentSlots = studentService.getPreferences(student.id()).preferences().stream().map(SinglePreference::timeslot).toList();
                if (!studentSlots.contains(slot)) {
                    wrongAssigned.put(student, slot);
                }
            }
        }

        outerLoop:
        for (StudentDto student : wrongAssigned.keySet()) {
            StudentPreferencesDto preferences = studentService.getPreferences(student.id());
            PreferredTimeslot wrongSlot = wrongAssigned.get(student);
            for (SinglePreference slot : preferences.preferences()) {
                PreferredTimeslot timeslot = new PreferredTimeslot(slot.timeslot().weekday(), slot.timeslot().startTime(), slot.timeslot().endTime());
                if (slotAssignments.containsKey(timeslot)) {
                    Iterator<StudentDto> iterator = slotAssignments.get(timeslot).iterator();
                    while (iterator.hasNext()) {
                        StudentDto studentToChange = iterator.next();
                        List<PreferredTimeslot> studentSlots = studentService.getPreferences(studentToChange.id()).preferences().stream().map(SinglePreference::timeslot).toList();
                        if (studentSlots.isEmpty() || studentSlots.contains(wrongSlot)) {
                            PreferredTimeslot slotToChange = null;
                            for (PreferredTimeslot s : slotAssignments.keySet()) {
                                if (slotAssignments.get(s).contains(student)) {
                                    slotToChange = s;
                                    break;
                                }
                            }
                            iterator.remove();
                            slotAssignments.get(timeslot).add(student);
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
