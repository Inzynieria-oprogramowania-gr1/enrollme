import com.company.project.algorithm.GroupingAlgorithm;
import com.company.project.dto.StudentDto;
import com.company.project.dto.StudentPreferencesDto;
import com.company.project.dto.timetable.TimeSLotDto;
import com.company.project.dto.timetable.TimetableDto;
import com.company.project.entity.UserRole;
import com.company.project.entity.Weekday;
import com.company.project.service.StudentService;
import com.company.project.service.TimetableService;
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
    private TimetableService timetableService;

    private GroupingAlgorithm groupingAlgorithm;

    @BeforeEach
    public void setup() {
        List<StudentDto> students = Arrays.asList(
                new StudentDto(1L, "student1@example.com",UserRole.STUDENT),
                new StudentDto(2L, "student2@example.com",UserRole.STUDENT),
                new StudentDto(3L, "student3@example.com",UserRole.STUDENT),
                new StudentDto(4L, "student4@example.com",UserRole.STUDENT),
                new StudentDto(5L, "student5@example.com",UserRole.STUDENT),
                new StudentDto(6L, "student6@example.com",UserRole.STUDENT)
        );
        when(studentService.getAllStudents()).thenReturn(students);

        when(studentService.getPreferences(1L)).thenReturn(new StudentPreferencesDto(1L, "student1@example.com", List.of(new TimetableDto(Weekday.Monday, List.of(new TimeSLotDto(LocalTime.of(8, 0), LocalTime.of(9, 30), true))))));
        when(studentService.getPreferences(2L)).thenReturn(new StudentPreferencesDto(2L, "student2@example.com", List.of(new TimetableDto(Weekday.Monday, List.of(new TimeSLotDto(LocalTime.of(8, 0), LocalTime.of(9, 30), true))), new TimetableDto(Weekday.Tuesday, List.of(new TimeSLotDto(LocalTime.of(8, 0), LocalTime.of(9, 30), true))))));
        when(studentService.getPreferences(3L)).thenReturn(new StudentPreferencesDto(3L, "student3@example.com", List.of(new TimetableDto(Weekday.Monday, List.of(new TimeSLotDto(LocalTime.of(9, 45), LocalTime.of(11, 15), true))), new TimetableDto(Weekday.Tuesday, List.of(new TimeSLotDto(LocalTime.of(8, 0), LocalTime.of(9, 30), true))))));
        when(studentService.getPreferences(4L)).thenReturn(new StudentPreferencesDto(4L, "student4@example.com", new ArrayList<>()));
        when(studentService.getPreferences(5L)).thenReturn(new StudentPreferencesDto(5L, "student5@example.com", List.of(new TimetableDto(Weekday.Tuesday, List.of(new TimeSLotDto(LocalTime.of(8, 0), LocalTime.of(9, 30), true))), new TimetableDto(Weekday.Tuesday, List.of(new TimeSLotDto(LocalTime.of(9, 45), LocalTime.of(11, 15), true))))));
        when(studentService.getPreferences(6L)).thenReturn(new StudentPreferencesDto(6L, "student6@example.com", new ArrayList<>()));

        List<TimetableDto> timetables = Arrays.asList(
                new TimetableDto(Weekday.Monday, List.of(new TimeSLotDto(LocalTime.of(8, 0), LocalTime.of(9, 30), true))),
                new TimetableDto(Weekday.Monday, List.of(new TimeSLotDto(LocalTime.of(9, 45), LocalTime.of(11, 15), true))),
                new TimetableDto(Weekday.Tuesday, List.of(new TimeSLotDto(LocalTime.of(8, 0), LocalTime.of(9, 30), true))),
                new TimetableDto(Weekday.Tuesday, List.of(new TimeSLotDto(LocalTime.of(9, 45), LocalTime.of(11, 15), true)))
        );
        when(timetableService.getTimetable()).thenReturn(timetables);

        this.groupingAlgorithm = new GroupingAlgorithm(studentService, timetableService);
    }

    private int getNumberOfStudentsAssignedToSlot(Map<TimetableDto, List<StudentDto>> assignments, TimetableDto slot) {
        return assignments.get(slot).size();
    }

    private List<StudentDto> getStudentsAssignedToSlot(Map<TimetableDto, List<StudentDto>> assignments, TimetableDto slot) {
        return assignments.get(slot);
    }

    @Test
    public void testSelectedSlots() {
        Map<TimetableDto, List<StudentDto>> resultAssignments = groupingAlgorithm.groupStudents();

        assertTrue(resultAssignments.containsKey(new TimetableDto(Weekday.Monday,
                List.of(new TimeSLotDto(LocalTime.of(8, 0), LocalTime.of(9, 30), true))))); // Slot poniedziałkowy 8:00-9:30
        assertTrue(resultAssignments.containsKey(new TimetableDto(Weekday.Monday,
                List.of(new TimeSLotDto(LocalTime.of(9, 45), LocalTime.of(11, 15), true))))); // Slot poniedziałkowy 9:45-11:15
        assertTrue(resultAssignments.containsKey(new TimetableDto(Weekday.Tuesday,
                List.of(new TimeSLotDto(LocalTime.of(8, 0), LocalTime.of(9, 30), true))))); // Slot wtorkowy 8:00-9:30
    }

    @Test
    public void testNumberOfStudentsAssignedToSlot() {
        Map<TimetableDto, List<StudentDto>> resultAssignments = groupingAlgorithm.groupStudents();

        assertEquals(2, getNumberOfStudentsAssignedToSlot(resultAssignments, new TimetableDto(Weekday.Monday,
                List.of(new TimeSLotDto(LocalTime.of(8, 0), LocalTime.of(9, 30), true)))));
        assertEquals(2, getNumberOfStudentsAssignedToSlot(resultAssignments, new TimetableDto(Weekday.Monday,
                List.of(new TimeSLotDto(LocalTime.of(9, 45), LocalTime.of(11, 15), true)))));
        assertEquals(2, getNumberOfStudentsAssignedToSlot(resultAssignments, new TimetableDto(Weekday.Tuesday,
                List.of(new TimeSLotDto(LocalTime.of(8, 0), LocalTime.of(9, 30), true)))));
    }

    @Test
    public void testStudentsAssignedToSlot() {
        Map<TimetableDto, List<StudentDto>> resultAssignments = groupingAlgorithm.groupStudents();

        List<StudentDto> studentsForMondaySlot1 = getStudentsAssignedToSlot(resultAssignments, new TimetableDto(Weekday.Monday,
                List.of(new TimeSLotDto(LocalTime.of(8, 0), LocalTime.of(9, 30), true))));
        List<StudentDto> studentsForMondaySlot2 = getStudentsAssignedToSlot(resultAssignments, new TimetableDto(Weekday.Monday,
                List.of(new TimeSLotDto(LocalTime.of(9, 45), LocalTime.of(11, 15), true))));
        List<StudentDto> studentsForTuesdaySlot = getStudentsAssignedToSlot(resultAssignments, new TimetableDto(Weekday.Tuesday,
                List.of(new TimeSLotDto(LocalTime.of(8, 0), LocalTime.of(9, 30), true))));


        assertTrue(studentsForMondaySlot1.contains(new StudentDto(1L, "student1@example.com",UserRole.STUDENT)));
        assertTrue(studentsForMondaySlot1.contains(new StudentDto(2L, "student2@example.com",UserRole.STUDENT)));
        assertTrue(studentsForMondaySlot2.contains(new StudentDto(3L, "student3@example.com",UserRole.STUDENT)));
        assertTrue(studentsForTuesdaySlot.contains(new StudentDto(5L, "student5@example.com",UserRole.STUDENT)));
        assertTrue(studentsForMondaySlot2.contains(new StudentDto(4L, "student4@example.com",UserRole.STUDENT)) ||
                studentsForTuesdaySlot.contains(new StudentDto(4L, "student4@example.com",UserRole.STUDENT)));
        assertTrue(studentsForMondaySlot2.contains(new StudentDto(6L, "student6@example.com",UserRole.STUDENT)) ||
                studentsForTuesdaySlot.contains(new StudentDto(6L, "student6@example.com",UserRole.STUDENT)));

    }

}