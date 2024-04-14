package com.company.project.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.mapstruct.Mapper;
import org.springframework.web.client.HttpClientErrorException.Conflict;

import com.company.project.dto.preferences.PreferredTimeslot;
import com.company.project.dto.preferences.SinglePreference;
import com.company.project.dto.preferences.StudentPreferencesDto;
import com.company.project.entity.StudentPreference;
import com.company.project.entity.Timeslot;
import com.company.project.entity.users.Student;
import com.company.project.exception.implementations.ConflictException;

@Mapper(componentModel = "spring")
public class StudentPreferencesMapper {
    public List<StudentPreferencesDto> mapToStudentPreferencesDto(Collection<StudentPreference> preferenceCollection){
        HashMap<Long, List<StudentPreference>> mapPreferences = new HashMap<>();
        List<StudentPreference> preferenceList = new ArrayList<>(preferenceCollection);
        List<StudentPreferencesDto> studentPreferencesDtos = new ArrayList<>();
        for(var m: preferenceList){
            Long studentId = m.getStudent().getId();
            List<StudentPreference> currentPreferenes = mapPreferences.get(studentId);
            if(currentPreferenes==null){
                currentPreferenes = new ArrayList<>();
                mapPreferences.put(studentId, currentPreferenes);
            }
            currentPreferenes.add(m);
        }
        for(var entrySetPreferences: mapPreferences.entrySet()){
            List<SinglePreference> prefTmp = entrySetPreferences.getValue().stream()
            .map(
                    preference -> {
                        Timeslot timeslot = preference.getTimeslot();

                        PreferredTimeslot preferredTimeslot = new PreferredTimeslot(
                                timeslot.getWeekday(),
                                timeslot.getStartTime(),
                                timeslot.getEndTime()
                        );


                        String note = preference.getNote();
                        boolean selected = preference.isSelected();

                        return new SinglePreference(preferredTimeslot, selected, note);
                    }
            ).toList();
            studentPreferencesDtos.add(
                new StudentPreferencesDto(entrySetPreferences.getKey(),
                entrySetPreferences.getValue().get(0).getStudent().getEmail(), prefTmp)
            );
        }
        return studentPreferencesDtos;
    }
}
