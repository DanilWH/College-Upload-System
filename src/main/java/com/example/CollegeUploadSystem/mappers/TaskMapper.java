package com.example.CollegeUploadSystem.mappers;

import com.example.CollegeUploadSystem.dto.TaskDto;
import com.example.CollegeUploadSystem.models.Task;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { GroupMapper.class })
public interface TaskMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name")
    Task toEntity(TaskDto taskDto);

    @Mapping(target = "groupDto", source = "task.group")
    TaskDto toDto(Task task);

}
