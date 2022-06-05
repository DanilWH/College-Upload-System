package com.example.CollegeUploadSystem.dto;

import com.example.CollegeUploadSystem.models.Views;
import com.fasterxml.jackson.annotation.*;

import java.time.ZonedDateTime;

public class StudentResultDto {

    @JsonView(Views.Id.class)
    private Long id;

    @JsonView(Views.IdName.class)
    private String filename;

    @JsonView(Views.IdName.class)
    private String filepath;

    @JsonView(Views.IdName.class)
    private ZonedDateTime dateTime;

    @JsonView(Views.IdName.class)
    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @JsonProperty("userId")
    private UserDto userDto;

    @JsonView(Views.IdName.class)
    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @JsonProperty("taskId")
    private TaskDto taskDto;

    public Long getId() {
        return id;
    }

    public StudentResultDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFilename() {
        return filename;
    }

    public StudentResultDto setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public String getFilepath() {
        return filepath;
    }

    public StudentResultDto setFilepath(String filepath) {
        this.filepath = filepath;
        return this;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public StudentResultDto setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public StudentResultDto setUserDto(UserDto userDto) {
        this.userDto = userDto;
        return this;
    }

    public TaskDto getTaskDto() {
        return taskDto;
    }

    public StudentResultDto setTaskDto(TaskDto taskDto) {
        this.taskDto = taskDto;
        return this;
    }
}
