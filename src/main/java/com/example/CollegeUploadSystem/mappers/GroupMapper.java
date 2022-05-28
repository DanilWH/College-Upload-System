package com.example.CollegeUploadSystem.mappers;

import com.example.CollegeUploadSystem.dto.GroupDto;
import com.example.CollegeUploadSystem.models.Group;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name")
    Group toEntity(GroupDto groupDto);

    GroupDto toDto(Group group);

}
