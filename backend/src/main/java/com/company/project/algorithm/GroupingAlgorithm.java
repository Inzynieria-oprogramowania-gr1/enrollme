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
import com.company.project.dto.preferences.PreferredTimeslot;
import com.company.project.dto.preferences.StudentPreferencesDto;
import com.company.project.dto.timetable.TimeslotDto;
import com.company.project.dto.timetable.TimetableDayDto;
import com.company.project.service.EnrollmentService;
import com.company.project.service.StudentService;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupingAlgorithm {
    private final StudentService studentService;
    private final List<StudentDto> studentsList;
    private final List<TimetableDayDto> timetableList;
    @Getter
    Map<TimetableDayDto, List<StudentDto>> slotAssignments = new HashMap<>();

    public GroupingAlgorithm(StudentService studentService, EnrollmentService enrollmentService) {
        this.studentService = studentService;
        this.studentsList = studentService.getAllStudents();
        this.timetableList = enrollmentService.getTimetable()
                .stream()
                .map(e -> {
                    List<TimeslotDto> dto = e.timeslots().stream().filter(TimeslotDto::isSelected).toList();
                    return new TimetableDayDto(e.weekday(), dto);
                }).toList();
    }

    public Map<TimetableDayDto, List<StudentDto>> groupStudents() {
        List<StudentDto> withoutPreferences = new ArrayList<>();
        for (StudentDto student : studentsList) {
            StudentPreferencesDto preferences = studentService.getPreferences(student.id());
            if (preferences.preferences().isEmpty()) {
                withoutPreferences.add(student);
            } else {
                TimetableDayDto assignedSlot = assignSlot(preferences);
                if (assignedSlot != null) {
                    slotAssignments.putIfAbsent(assignedSlot, new ArrayList<>());
                    slotAssignments.get(assignedSlot).add(student);

                } else {
                    withoutPreferences.add(student);
                }
            }
        }
        if (slotAssignments.isEmpty()) {
            TimetableDayDto td = timetableList.get(0);
            TimetableDayDto replaced = new TimetableDayDto(td.weekday(), List.of(td.timeslots().get(0)));
            slotAssignments.put(replaced, withoutPreferences);
            return getSlotAssignments();
        }
        for (StudentDto student : withoutPreferences) {
            TimetableDayDto assignedSlot = assignRestStudents();
            slotAssignments.putIfAbsent(assignedSlot, new ArrayList<>());
            slotAssignments.get(assignedSlot).add(student);
        }

        return getSlotAssignments();
    }

    public TimetableDayDto assignSlot(StudentPreferencesDto preferences) {

        // TODO fix, it is just a temporary implementation so it can at least run...
        PreferredTimeslot tim = preferences.preferences().get(0).timeslot();
        TimeslotDto timeslotDto = new TimeslotDto(tim.startTime(), tim.endTime(), true);
        return new TimetableDayDto(tim.weekday(), List.of(timeslotDto));

        // pierwszy for w pierwszej iteracji weźmie jakiś dzień, np. Poniedziałek
        // i listę slotów, które w pon. pasują studentowi

        // dalej filtrujemy cały timetable po tym dniu ^
        // drugi for w pierwszej iteracji sprawdzi, czy pierwszy slot wybrany przez studenta w poniedziałek
        // jest w poniedziałek ... ?

//        for (TimetableDayDto slots : preferences.timetableDays()) {
//            List<TimeslotDto> filteredSlots = timetableList.stream()
//                    .filter(t -> t.weekday().equals(slots.weekday()))
//                    .flatMap(t -> t.timeslots().stream())
//                    .toList();
//            for (TimeslotDto slot : slots.timeslots()) {
//                if (filteredSlots.contains(slot)) {
//                    return new TimetableDayDto(slots.weekday(), List.of(slot));
//                }
//            }
//        }
//        return null;
    }

    public TimetableDayDto assignRestStudents() {
        TimetableDayDto minSlot = null;
        int minStudents = Integer.MAX_VALUE;
        for (TimetableDayDto slot : slotAssignments.keySet()) {
            if (slotAssignments.get(slot).size() < minStudents) {
                minStudents = slotAssignments.get(slot).size();
                minSlot = slot;
            }
        }
        return minSlot;
    }


}
