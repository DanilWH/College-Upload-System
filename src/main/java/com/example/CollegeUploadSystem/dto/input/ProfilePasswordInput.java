package com.example.CollegeUploadSystem.dto.input;

import com.example.CollegeUploadSystem.validation.constrains.PasswordsMatch;
import com.example.CollegeUploadSystem.validation.constrains.ValidPassword;

@PasswordsMatch
public class ProfilePasswordInput {

    private String oldPassword;
    @ValidPassword
    private String password;
    private String confirmPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public ProfilePasswordInput setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public ProfilePasswordInput setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public ProfilePasswordInput setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }
}
