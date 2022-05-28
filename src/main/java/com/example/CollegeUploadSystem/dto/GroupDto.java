package com.example.CollegeUploadSystem.dto;

import com.example.CollegeUploadSystem.models.Views;
import com.fasterxml.jackson.annotation.JsonView;

import javax.validation.constraints.NotBlank;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class GroupDto {
    @JsonView(Views.Id.class)
    private Long id;

    @NotBlank(message = "The field must not be blank")
    @JsonView(Views.IdName.class)
    private String name;

    @JsonView(Views.FullProfile.class)
    private ZonedDateTime creationDate;

    @JsonView(Views.FullProfile.class)
    private Boolean isActive;

    public GroupDto() {
    }

    public GroupDto(Long id, @NotBlank(message = "Fill the field") String name, ZonedDateTime creationDate, Boolean isActive) {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public GroupDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public GroupDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getCreationDate() {
        return creationDate.format(DateTimeFormatter.ISO_OFFSET_DATE);
    }

    public GroupDto setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public Boolean getActive() {
        return isActive;
    }

    public GroupDto setActive(Boolean active) {
        isActive = active;
        return this;
    }
}
