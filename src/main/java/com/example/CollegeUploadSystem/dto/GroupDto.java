package com.example.CollegeUploadSystem.dto;

import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.models.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

public class GroupDto {
    @JsonView(Views.Id.class)
    private Long id;

    @NotBlank(message = "Fill the field")
    @JsonView(Views.IdName.class)
    private String name;

    @JsonView(Views.FullProfile.class)
    private LocalDate creationDate;

    @JsonView(Views.FullProfile.class)
    private Boolean isActive = true;

    public GroupDto() {
    }

    public GroupDto(Group group) {
        if (group == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Group is null");
        }

        this.id = group.getId();
        this.name = group.getName();
        this.creationDate = group.getCreationDate();
        this.isActive = group.getActive();
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

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public GroupDto setCreationDate(LocalDate creationDate) {
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
