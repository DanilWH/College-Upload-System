package com.example.CollegeUploadSystem.dto;

import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.models.UserRoles;
import com.example.CollegeUploadSystem.models.Views;
import com.fasterxml.jackson.annotation.JsonView;

import java.io.Serializable;
import java.util.Set;

public class UserDto implements Serializable {
    @JsonView(Views.Id.class)
    private Long id;

    @JsonView(Views.IdName.class)
    private String firstName;
    @JsonView(Views.IdName.class)
    private String lastName;
    @JsonView(Views.FullProfile.class)
    private String fatherName;

    @JsonView(Views.FullProfile.class)
    private String login;

    @JsonView(Views.FullProfile.class)
    private Set<UserRoles> roles;

    @JsonView(Views.FullProfile.class)
    private Long groupId;

    public UserDto(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.fatherName = user.getFatherName();
        this.login = user.getLogin();
        this.roles = user.getUserRoles();
        this.groupId = (user.getGroup() == null)? null : user.getGroup().getId();
    }

    public UserDto setId(Long id) {
        this.id = id;
        return this;
    }

    public UserDto setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserDto setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserDto setFatherName(String fatherName) {
        this.fatherName = fatherName;
        return this;
    }

    public UserDto setLogin(String login) {
        this.login = login;
        return this;
    }

    public UserDto setRoles(Set<UserRoles> roles) {
        this.roles = roles;
        return this;
    }

    public UserDto setGroupId(Long groupId) {
        this.groupId = groupId;
        return this;
    }
}
