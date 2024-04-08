package com.company.project.mapper;

import com.company.project.dto.StudentDto;
import com.company.project.dto.StudentPreferencesDto;
import com.company.project.entity.users.Student;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = TimeslotMapper.class)
public interface StudentMapper {
    StudentDto mapToStudentDto(Student student);

    Student mapToStudent(StudentDto studentDto);

    @Mapping(source = "preferences", target = "timetables")
    StudentPreferencesDto mapToStudentPreferencesDto(Student student);

    @InheritInverseConfiguration
    Student mapToStudent(StudentPreferencesDto studentPreferencesDto);
}
