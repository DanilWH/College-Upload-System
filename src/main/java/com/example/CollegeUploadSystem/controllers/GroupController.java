package com.example.CollegeUploadSystem.controllers;

import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.models.Views;
import com.example.CollegeUploadSystem.services.GroupService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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
    private final Validator validator;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
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
    public Group  create(@RequestBody Group group) throws IOException {
        Set<ConstraintViolation<Group>> constraintViolations = this.validator.validate(group);

        if (!constraintViolations.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, constraintViolations.toString());
        }
        return this.groupService.create(group);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @JsonView(Views.IdName.class)
    @PutMapping("/groups/{id}")
    public Group update(@PathVariable("id") Group groupFromDb, @RequestBody Group group) {
        return this.groupService.update(groupFromDb, group);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/groups/{id}")
    public ResponseEntity delete(@PathVariable("id") Group group) {
        this.groupService.deactivate(group);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
