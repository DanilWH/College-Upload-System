package com.example.CollegeUploadSystem.controllers;

import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.models.Views;
import com.example.CollegeUploadSystem.services.GroupService;
import com.example.CollegeUploadSystem.services.TaskService;
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

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class GroupController {
    private final GroupService groupService;
    private final TaskService taskService;
    private final UserService userService;
    private final Validator validator;

    @Autowired
    public GroupController(GroupService groupService, TaskService taskService, UserService userService) {
        this.groupService = groupService;
        this.taskService = taskService;
        this.userService = userService;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @JsonView(Views.IdName.class)
    @GetMapping("/groups")
    public List<Group> list() {
        return this.groupService.findAllActive();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @JsonView(Views.IdName.class)
    @PostMapping("/groups")
    public ResponseEntity<Group>  create(
            @AuthenticationPrincipal User admin,
            @RequestParam("file") MultipartFile file,
            @RequestPart("group") String rawGroup
    ) throws IOException {
        // TODO divide the requests.
        Group group = this.groupService.convertToJson(rawGroup);
        Set<ConstraintViolation<Group>> constraintViolations = this.validator.validate(group);

        // check the correctness of the file extension.
        String fileExt = FilenameUtils.getExtension(file.getOriginalFilename());

        if (!fileExt.equals("csv") || !constraintViolations.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, constraintViolations.toString());
        }

        Group createdGroup = this.groupService.create(file, group, admin);
        return new ResponseEntity<>(createdGroup, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @JsonView(Views.IdName.class)
    @PutMapping("/groups/{id}")
    public Group update(@PathVariable("id") Group groupFromDb, @RequestBody Group group) {
        return this.groupService.update(groupFromDb, group);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/groups/{id}")
    public ResponseEntity delete(@PathVariable("id") Group group) {
        this.groupService.delete(group);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        // TODO implement deactivation of students.
    }
}
