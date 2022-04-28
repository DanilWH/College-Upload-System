package com.example.CollegeUploadSystem.services;

import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.repos.GroupRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {
    private final GroupRepo groupRepo;

    public GroupService(GroupRepo groupRepo) {
        this.groupRepo = groupRepo;
    }

    public Group findById(Long id) {
        return this.groupRepo.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "The group with the ID of " + id + " was not found."
        ));
    }

    public List<Group> findAllActive() {
        // we list the active groups only.
        return this.groupRepo.findAll()
                .stream()
                .filter(Group::getActive)
                .collect(Collectors.toList());
    }

    public Group create(Group group) throws IOException {
        group.setCreationDate(LocalDate.now());
        group.setActive(true);

        // save the group in the database.
        return this.groupRepo.save(group);
    }

    public Group update(Group groupFromDb, Group group) {
        groupFromDb.setName(group.getName());
        return this.groupRepo.save(groupFromDb);
    }

    public void deactivate(Long groupId) {
        Group group = this.groupRepo.findById(groupId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such group"));
        group.setActive(false);
        this.groupRepo.save(group);
    }

    public void delete(Long groupId) {
        Group group = this.groupRepo.findById(groupId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such group."));
        this.groupRepo.delete(group);
    }
}
