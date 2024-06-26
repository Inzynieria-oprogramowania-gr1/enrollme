import com.company.project.algorithm.GroupingAlgorithm;
import com.company.project.dto.StudentDto;
import com.company.project.dto.preferences.PreferredTimeslot;
import com.company.project.dto.preferences.SinglePreference;
import com.company.project.dto.preferences.StudentPreferencesDto;
import com.company.project.dto.timetable.TimeslotDto;
import com.company.project.dto.timetable.TimetableDayDto;
import com.company.project.entity.Weekday;
import com.company.project.service.EnrollmentService;
import com.company.project.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = GroupingAlgorithm.class)
public class GroupingAlgorithmTest {

    @MockBean
    private StudentService studentService;

    @MockBean
    private EnrollmentService enrollmentService;

    private GroupingAlgorithm groupingAlgorithm;

    @BeforeEach
    public void setup() {
        List<StudentDto> students = Arrays.asList(
                new StudentDto(1L, "student1@example.com"),
                new StudentDto(2L, "student2@example.com"),
                new StudentDto(3L, "student3@example.com"),
                new StudentDto(4L, "student4@example.com"),
                new StudentDto(5L, "student5@example.com"),
                new StudentDto(6L, "student6@example.com")
        );
        when(studentService.getAllStudents()).thenReturn(students);


        when(studentService.getPreferences(1L)).thenReturn(new StudentPreferencesDto(1L, "student1@example.com", List.of(new SinglePreference(new PreferredTimeslot(Weekday.Monday, LocalTime.of(8, 0), LocalTime.of(9, 30)), true, ""))));
        when(studentService.getPreferences(2L)).thenReturn(new StudentPreferencesDto(2L, "student1@example.com", List.of(new SinglePreference(new PreferredTimeslot(Weekday.Monday, LocalTime.of(8, 0), LocalTime.of(9, 30)), true, ""), new SinglePreference(new PreferredTimeslot(Weekday.Tuesday, LocalTime.of(8, 0), LocalTime.of(9, 30)), true, ""))));
        when(studentService.getPreferences(3L)).thenReturn(new StudentPreferencesDto(3L, "student1@example.com", List.of(new SinglePreference(new PreferredTimeslot(Weekday.Monday, LocalTime.of(9, 45), LocalTime.of(11, 15)), true, ""), new SinglePreference(new PreferredTimeslot(Weekday.Tuesday, LocalTime.of(8, 0), LocalTime.of(9, 30)), true, ""))));
        when(studentService.getPreferences(4L)).thenReturn(new StudentPreferencesDto(4L, "student4@example.com", new ArrayList<>()));
        when(studentService.getPreferences(5L)).thenReturn(new StudentPreferencesDto(5L, "student5@example.com", List.of(new SinglePreference(new PreferredTimeslot(Weekday.Tuesday, LocalTime.of(8, 0), LocalTime.of(9, 30)), true, ""))));
        when(studentService.getPreferences(6L)).thenReturn(new StudentPreferencesDto(6L, "student6@example.com", new ArrayList<>()));

//        when(studentService.getPreferences(1L)).thenReturn(new StudentPreferencesDto(1L, "student1@example.com", new ArrayList<>()));
//        when(studentService.getPreferences(2L)).thenReturn(new StudentPreferencesDto(2L, "student2@example.com", new ArrayList<>()));
//        when(studentService.getPreferences(3L)).thenReturn(new StudentPreferencesDto(3L, "student3@example.com", new ArrayList<>()));
//        when(studentService.getPreferences(4L)).thenReturn(new StudentPreferencesDto(4L, "student4@example.com", new ArrayList<>()));
//        when(studentService.getPreferences(5L)).thenReturn(new StudentPreferencesDto(5L, "student5@example.com", new ArrayList<>()));
//        when(studentService.getPreferences(6L)).thenReturn(new StudentPreferencesDto(6L, "student6@example.com", new ArrayList<>()));

        List<TimetableDayDto> timetables = Arrays.asList(
                new TimetableDayDto(Weekday.Monday, List.of(new TimeslotDto(LocalTime.of(8, 0), LocalTime.of(9, 30), true))),
                new TimetableDayDto(Weekday.Monday, List.of(new TimeslotDto(LocalTime.of(9, 45), LocalTime.of(11, 15), true))),
                new TimetableDayDto(Weekday.Tuesday, List.of(new TimeslotDto(LocalTime.of(8, 0), LocalTime.of(9, 30), true)))
        );


        when(enrollmentService.getSelectedTimetable()).thenReturn(timetables);
        when(enrollmentService.getGroupAmount()).thenReturn(3);

        this.groupingAlgorithm = new GroupingAlgorithm(studentService, enrollmentService);
    }

    private int getNumberOfStudentsAssignedToSlot(Map<PreferredTimeslot, List<StudentDto>> assignments, PreferredTimeslot slot) {
        return assignments.get(slot).size();
    }

    private List<StudentDto> getStudentsAssignedToSlot(Map<PreferredTimeslot, List<StudentDto>> assignments, PreferredTimeslot slot) {
        return assignments.get(slot);
    }

    @Test
    public void testSelectedSlots() {
        Map<PreferredTimeslot, List<StudentDto>> resultAssignments = groupingAlgorithm.groupStudents();

        assertTrue(resultAssignments.containsKey(new PreferredTimeslot(Weekday.Monday, LocalTime.of(8, 0), LocalTime.of(9, 30)))); // Slot poniedziałkowy 8:00-9:30
        assertTrue(resultAssignments.containsKey(new PreferredTimeslot(Weekday.Monday, LocalTime.of(9, 45), LocalTime.of(11, 15)))); // Slot poniedziałkowy 9:45-11:15
        assertTrue(resultAssignments.containsKey(new PreferredTimeslot(Weekday.Tuesday, LocalTime.of(8, 0), LocalTime.of(9, 30)))); // Slot wtorkowy 8:00-9:30
    }

    @Test
    public void testNumberOfStudentsAssignedToSlot() {
        Map<PreferredTimeslot, List<StudentDto>> resultAssignments = groupingAlgorithm.groupStudents();

        assertEquals(2, getNumberOfStudentsAssignedToSlot(resultAssignments, new PreferredTimeslot(Weekday.Monday, LocalTime.of(8, 0), LocalTime.of(9, 30))));
        assertEquals(2, getNumberOfStudentsAssignedToSlot(resultAssignments, new PreferredTimeslot(Weekday.Tuesday, LocalTime.of(8, 0), LocalTime.of(9, 30))));
        assertEquals(2, getNumberOfStudentsAssignedToSlot(resultAssignments, new PreferredTimeslot(Weekday.Tuesday, LocalTime.of(8, 0), LocalTime.of(9, 30))));
    }

    @Test
    public void testStudentsAssignedToSlot() {
        Map<PreferredTimeslot, List<StudentDto>> resultAssignments = groupingAlgorithm.groupStudents();

        List<StudentDto> studentsForMondaySlot1 = getStudentsAssignedToSlot(resultAssignments, new PreferredTimeslot(Weekday.Monday, LocalTime.of(8, 0), LocalTime.of(9, 30)));
        List<StudentDto> studentsForMondaySlot2 = getStudentsAssignedToSlot(resultAssignments, new PreferredTimeslot(Weekday.Monday, LocalTime.of(9, 45), LocalTime.of(11, 15)));
        List<StudentDto> studentsForTuesdaySlot = getStudentsAssignedToSlot(resultAssignments, new PreferredTimeslot(Weekday.Tuesday, LocalTime.of(8, 0), LocalTime.of(9, 30)));


        assertTrue(studentsForMondaySlot1.contains(new StudentDto(1L, "student1@example.com")));
        assertTrue(studentsForMondaySlot1.contains(new StudentDto(6L, "student6@example.com")) || studentsForMondaySlot1.contains(new StudentDto(4L, "student4@example.com")));
        assertTrue(studentsForMondaySlot2.contains(new StudentDto(3L, "student3@example.com")));
        assertTrue(studentsForMondaySlot2.contains(new StudentDto(4L, "student4@example.com")) || studentsForMondaySlot2.contains(new StudentDto(6L, "student6@example.com")));
        assertTrue(studentsForTuesdaySlot.contains(new StudentDto(2L, "student2@example.com")));
        assertTrue(studentsForTuesdaySlot.contains(new StudentDto(5L, "student5@example.com")));
    }


    @Test
    public void testGetSlotsFrequency() {
        Map<PreferredTimeslot, Integer> slotsFrequency = groupingAlgorithm.getSlotsFrequency();

        assertEquals(3, slotsFrequency.size());
        PreferredTimeslot testSlot = new PreferredTimeslot(Weekday.Monday, LocalTime.of(8, 0), LocalTime.of(9, 30));
        assertEquals(2, slotsFrequency.get(testSlot));
        testSlot = new PreferredTimeslot(Weekday.Monday, LocalTime.of(9, 45), LocalTime.of(11, 15));
        assertEquals(1, slotsFrequency.get(testSlot));
        testSlot = new PreferredTimeslot(Weekday.Tuesday, LocalTime.of(8, 0), LocalTime.of(9, 30));
        assertEquals(3, slotsFrequency.get(testSlot));
    }

    @Test
    public void testGetBestSlot() {
        Map<PreferredTimeslot, Integer> slotsFrequency = groupingAlgorithm.getSlotsFrequency();
        PreferredTimeslot bestSlot = groupingAlgorithm.getBestSlot(slotsFrequency);
        assertEquals(new PreferredTimeslot(Weekday.Tuesday, LocalTime.of(8, 0), LocalTime.of(9, 30)), bestSlot);
    }

}