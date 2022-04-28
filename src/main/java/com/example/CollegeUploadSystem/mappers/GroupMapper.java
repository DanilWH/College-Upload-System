package com.example.CollegeUploadSystem.mappers;

import com.example.CollegeUploadSystem.dto.GroupDto;
import com.example.CollegeUploadSystem.models.Group;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    Group groupDtoToGroup(GroupDto groupDto);
    GroupDto groupToGroupDto(Group group);

}
