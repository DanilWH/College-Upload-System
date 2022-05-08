package com.example.CollegeUploadSystem.dto;

import com.example.CollegeUploadSystem.models.UserRoles;
import com.example.CollegeUploadSystem.models.Views;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private Set<UserRoles> userRoles;

    @JsonView(Views.FullProfile.class)
    @JsonProperty(value = "group")
    private GroupDto groupDto;

    public UserDto() {
    }

    public UserDto(Long id, String firstName, String lastName, String fatherName, String login, Set<UserRoles> userRoles, GroupDto groupDto) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fatherName = fatherName;
        this.login = login;
        this.userRoles = userRoles;
        this.groupDto = groupDto;
    }

    public Long getId() {
        return id;
    }

    public UserDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserDto setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserDto setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getFatherName() {
        return fatherName;
    }

    public UserDto setFatherName(String fatherName) {
        this.fatherName = fatherName;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public UserDto setLogin(String login) {
        this.login = login;
        return this;
    }

    public Set<UserRoles> getUserRoles() {
        return userRoles;
    }

    public UserDto setUserRoles(Set<UserRoles> userRoles) {
        this.userRoles = userRoles;
        return this;
    }

    public GroupDto getGroupDto() {
        return groupDto;
    }

    public UserDto setGroupDto(GroupDto groupDto) {
        this.groupDto = groupDto;
        return this;
    }
}
