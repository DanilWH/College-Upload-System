package com.example.CollegeUploadSystem.mappers;

import com.example.CollegeUploadSystem.dto.GroupDto;
import com.example.CollegeUploadSystem.models.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    @Mapping(target = "id", ignore = true)
    Group toEntity(GroupDto groupDto);

    GroupDto toDto(Group group);

}
