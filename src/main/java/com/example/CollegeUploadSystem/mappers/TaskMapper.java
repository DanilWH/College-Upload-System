package com.example.CollegeUploadSystem.mappers;

import com.example.CollegeUploadSystem.dto.TaskDto;
import com.example.CollegeUploadSystem.models.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { GroupMapper.class })
public interface TaskMapper {

    @Mapping(target = "id", ignore = true)
    Task toEntity(TaskDto taskDto);

    @Mapping(target = "groupDto", source = "task.group")
    TaskDto toDto(Task task);

}
