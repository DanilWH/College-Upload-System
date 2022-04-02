package com.example.CollegeUploadSystem.dto.input;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ProfileLoginInput {

    @NotNull
    @Size(min = 1, max = 60)
    @Pattern(regexp = "^[A-Za-zА-Яа-я0-9@_\\-+./]*$", message = "The login contains forbidden symbols.")
    private String login;

    public String getLogin() {
        return login;
    }

    public ProfileLoginInput setLogin(String login) {
        this.login = login;
        return this;
    }
}
