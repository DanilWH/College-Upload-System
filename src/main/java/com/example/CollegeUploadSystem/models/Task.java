package com.example.CollegeUploadSystem.models;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

// TODO: remove JsonView annotations.
@Entity
@Table(name = "tasks")
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.Id.class)
    private Long id;

    @NotBlank(message = "Поле не должно быть пустым")
    @Size(max = 255)
    @JsonView(Views.IdName.class)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    @JsonView(Views.FullProfile.class)
    private Group group;

    @JsonView(Views.FullProfile.class)
    private LocalDateTime creationDateTime;

    @Size(max = 255)
    @JsonView(Views.IdName.class)
    private String descriptionFile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getDescriptionFile() {
        return descriptionFile;
    }

    public void setDescriptionFile(String descriptionFile) {
        this.descriptionFile = descriptionFile;
    }
}
