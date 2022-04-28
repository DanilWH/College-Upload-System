package com.example.CollegeUploadSystem.dto;

import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.models.UserRoles;
import com.example.CollegeUploadSystem.models.Views;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.Serializable;
import java.util.HashSet;
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

    @JsonView(Views.IdName.class)
    @JsonProperty(value = "group")
    private GroupDto groupDto;

    public UserDto() {
    }

    public UserDto(User user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User is null");
        }

        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.fatherName = user.getFatherName();
        this.login = user.getLogin();
        this.roles = new HashSet<>(user.getUserRoles());

        if (user.getGroup() != null) {
            this.groupDto = new GroupDto(user.getGroup());
        } else {
            this.groupDto = null;
        }
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

    public Set<UserRoles> getRoles() {
        return roles;
    }

    public UserDto setRoles(Set<UserRoles> roles) {
        this.roles = roles;
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
