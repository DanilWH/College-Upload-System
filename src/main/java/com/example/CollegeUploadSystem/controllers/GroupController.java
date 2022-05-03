package com.example.CollegeUploadSystem.controllers;

import com.example.CollegeUploadSystem.dto.GroupDto;
import com.example.CollegeUploadSystem.mappers.GroupMapper;
import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.models.Views;
import com.example.CollegeUploadSystem.services.GroupService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class GroupController {

    private final GroupService groupService;
    private final GroupMapper groupMapper;

    @Autowired
    public GroupController(GroupService groupService, GroupMapper groupMapper) {
        this.groupService = groupService;
        this.groupMapper = groupMapper;
    }

    @JsonView(Views.IdName.class)
    @GetMapping("/groups")
    public List<GroupDto> list() {
        // find all the needed groups and map the list of them to their DTO.
        return this.groupService.findAllActive()
                .stream()
                .map(this.groupMapper::toDto)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @JsonView(Views.IdName.class)
    @PostMapping("/groups")
    public GroupDto  create(@Valid @RequestBody GroupDto groupDto) throws IOException {
        // convert the dto to the entity.
        Group group = this.groupMapper.toEntity(groupDto);

        // save the new group in the database.
        Group createdGroup = this.groupService.create(group);

        // convert the saved group to its DTO and return it.
        return this.groupMapper.toDto(createdGroup);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @JsonView(Views.IdName.class)
    @PutMapping("/groups/{groupId}")
    public GroupDto update(@PathVariable("groupId") Long groupId, @Valid @RequestBody GroupDto groupDto) {
        // find the original group copy in the database.
        Group groupFromDb = this.groupService.findById(groupId);

        // convert the received group DTO to the entity.
        Group group = this.groupMapper.toEntity(groupDto);

        // apply the changes and save them in the database.
        Group updatedGroup = this.groupService.update(groupFromDb, group);

        // convert the updated group entity to its DTO and return it.
        return this.groupMapper.toDto(updatedGroup);
    }

    // TODO: change patch to post.
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/groups/{groupId}/status")
    public ResponseEntity<Void> deactivate(@PathVariable("groupId") Long groupId) {
        this.groupService.deactivate(groupId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/groups/{groupId}")
    public ResponseEntity<Void> delete(@PathVariable("groupId") Long groupId) {
        this.groupService.delete(groupId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
