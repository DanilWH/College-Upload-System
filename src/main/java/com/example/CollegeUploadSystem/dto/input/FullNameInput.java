package com.example.CollegeUploadSystem.dto.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class FullNameInput {

    @NotBlank
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 50)
    private String lastName;

    @NotBlank
    @Size(min = 2, max = 50)
    private String fatherName;

    public String getFirstName() {
        return firstName;
    }

    public FullNameInput setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public FullNameInput setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getFatherName() {
        return fatherName;
    }

    public FullNameInput setFatherName(String fatherName) {
        this.fatherName = fatherName;
        return this;
    }
}
