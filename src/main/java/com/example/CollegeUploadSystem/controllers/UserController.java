package com.example.CollegeUploadSystem.controllers;

import com.example.CollegeUploadSystem.dto.UserDto;
import com.example.CollegeUploadSystem.dto.input.FullNameInput;
import com.example.CollegeUploadSystem.dto.input.ProfileLoginInput;
import com.example.CollegeUploadSystem.dto.input.ProfilePasswordInput;
import com.example.CollegeUploadSystem.mappers.UserMapper;
import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.models.Views;
import com.example.CollegeUploadSystem.services.GroupService;
import com.example.CollegeUploadSystem.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final GroupService groupService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, GroupService groupService, UserMapper userMapper) {
        this.userService = userService;
        this.groupService = groupService;
        this.userMapper = userMapper;
    }

    /*** GET ***/

    @GetMapping("/users/me")
    @JsonView(Views.FullProfile.class)
    public UserDto getMyProfile(@AuthenticationPrincipal User currentUser) {
        return this.userMapper.toDto(currentUser);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users/{userId}")
    @JsonView(Views.FullProfile.class)
    public UserDto getOne(@PathVariable("userId") Long userId) {
        User user = this.userService.findById(userId);
        return this.userMapper.toDto(user);
    }

    @GetMapping("/groups/{groupId}/users")
    @JsonView(Views.IdName.class)
    public List<UserDto> list(@PathVariable("groupId") Long groupId) {
        return this.userService.findByGroupIdOrderByLastName(groupId)
                .stream()
                .map(this.userMapper::toDto)
                .collect(Collectors.toList());
    }

    /*** GENERATE ***/

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("groups/{groupId}/users")
    @JsonView(Views.IdName.class)
    public ResponseEntity<Void> generate(@PathVariable("groupId") Long groupId, @RequestParam("csvFile") MultipartFile csvFile, @AuthenticationPrincipal User admin) throws IOException {
        // check the correctness of the file extension.
        String fileExt = FilenameUtils.getExtension(csvFile.getOriginalFilename());

        if (fileExt != null && !fileExt.equals("csv")) {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Not appropriate file format! Must be CSV!");
        }

        // generate the new users.
        Group group = this.groupService.findById(groupId);
        this.userService.createNewUsers(csvFile, group, admin);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /*** DEACTIVATE ***/

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/users/{userId}/status")
    public ResponseEntity<Void> deactivate(@PathVariable("userId") Long userId) {
        User user = this.userService.findById(userId);
        this.userService.deactivate(user);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("groups/{groupId}/users/status")
    public ResponseEntity<Void> deactivateByGroup(@PathVariable("groupId") Long groupId) {
        Group group = this.groupService.findById(groupId);
        this.userService.deactivateAllByGroup(group);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /*** UPDATE ***/

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/users/{userId}/full-name")
    public ResponseEntity<Void> updateFullName(@PathVariable("userId") Long userId, @Valid @RequestBody FullNameInput fullNameInput) {
        User user = this.userService.findById(userId);
        this.userService.updateFullName(user, fullNameInput);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/users/{userId}/login")
    public ResponseEntity<Void> updateLogin(@PathVariable("userId") Long userId, @Valid @RequestBody ProfileLoginInput profileLoginInput) {
        User user = this.userService.findById(userId);
        this.userService.updateLogin(user, profileLoginInput);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/users/{userId}/password")
    public ResponseEntity<Void> updatePassword(@AuthenticationPrincipal User currentUser, @PathVariable("userId") Long userId, @Valid @RequestBody ProfilePasswordInput profilePasswordInput) {
        User user = this.userService.findById(userId);
        this.userService.updatePassword(currentUser, user, profilePasswordInput);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /*** DELETE ***/

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("groups/{groupId}/users")
    public ResponseEntity<Void> deleteByGroup(@PathVariable("groupId") Long groupId) {
        this.userService.deleteAllByGroupId(groupId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
