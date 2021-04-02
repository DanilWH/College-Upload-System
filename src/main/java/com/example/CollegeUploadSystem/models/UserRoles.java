package com.example.CollegeUploadSystem.models;

import org.springframework.security.core.GrantedAuthority;

public enum UserRoles implements GrantedAuthority {
    STUDENT,
    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
