package com.example.CollegeUploadSystem.mappers;

import com.example.CollegeUploadSystem.dto.StudentResultDto;
import com.example.CollegeUploadSystem.models.StudentResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { UserMapper.class, TaskMapper.class })
public interface StudentResultMapper {

    @Mapping(target = "id", ignore = true)
    StudentResult toEntity(StudentResultDto studentResultDto);

    @Mapping(target = "userDto", source = "studentResult.user")
    @Mapping(target = "taskDto", source = "studentResult.task")
    StudentResultDto toDto(StudentResult studentResult);

}
