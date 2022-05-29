package com.example.CollegeUploadSystem.controllers;

import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/invalidate-tokens")
    public ResponseEntity<Void> invalidateTokens(@AuthenticationPrincipal User currentUser) {
        this.userService.invalidateTokens(currentUser);
        return new ResponseEntity<>(HttpStatus.RESET_CONTENT);
    }

}
