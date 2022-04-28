package com.example.CollegeUploadSystem.mappers;

import com.example.CollegeUploadSystem.dto.UserDto;
import com.example.CollegeUploadSystem.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { GroupMapper.class })
public interface UserMapper {

    @Mapping(target = "groupDto", source = "user.group")
    UserDto userToUserDto(User user);

}
