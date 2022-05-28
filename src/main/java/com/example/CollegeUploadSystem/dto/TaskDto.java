package com.example.CollegeUploadSystem.dto;

import com.example.CollegeUploadSystem.models.Views;
import com.fasterxml.jackson.annotation.JsonView;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

public class TaskDto {
    @JsonView(Views.Id.class)
    private Long id;

    @NotBlank(message = "The field must not be blank")
    @Size(max = 255)
    @JsonView(Views.IdName.class)
    private String name;

    @JsonView(Views.FullProfile.class)
    private GroupDto groupDto;

    @JsonView(Views.FullProfile.class)
    private ZonedDateTime creationDateTime;

    @Size(max = 255)
    @JsonView(Views.IdName.class)
    private String descriptionFileLocation;

    public Long getId() {
        return id;
    }

    public TaskDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TaskDto setName(String name) {
        this.name = name;
        return this;
    }

    public GroupDto getGroupDto() {
        return groupDto;
    }

    public TaskDto setGroupDto(GroupDto groupDto) {
        this.groupDto = groupDto;
        return this;
    }

    public ZonedDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public TaskDto setCreationDateTime(ZonedDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
        return this;
    }

    public String getDescriptionFileLocation() {
        return descriptionFileLocation;
    }

    public TaskDto setDescriptionFileLocation(String descriptionFileLocation) {
        this.descriptionFileLocation = descriptionFileLocation;
        return this;
    }
}
