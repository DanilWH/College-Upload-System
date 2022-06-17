package com.example.CollegeUploadSystem.services;

import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.models.Task;
import com.example.CollegeUploadSystem.repos.GroupRepo;
import com.example.CollegeUploadSystem.repos.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {

    private final GroupRepo groupRepo;
    private final TaskRepo taskRepo;

    @Autowired
    public GroupService(GroupRepo groupRepo, TaskRepo taskRepo) {
        this.groupRepo = groupRepo;
        this.taskRepo = taskRepo;
    }

    public Group findById(Long id) {
        return this.groupRepo.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "The group with the ID of " + id + " was not found."
        ));
    }

    public List<Group> findAll(boolean isActive) {
        return this.groupRepo.findAll()
                .stream()
                .filter(group -> group.isActive() == isActive)
                .collect(Collectors.toList());
    }

    public Group create(Group group) throws IOException {
        group.setCreationDate(ZonedDateTime.now());
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
        List<Task> tasks = this.taskRepo.findByGroupId(groupId);
        if (tasks != null && !tasks.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "There are tasks that were added to this group, thus, the group can not be deleted." +
                    "To make that possible, delete all the tasks in the group."
            );
        }

        Group group = this.groupRepo.findById(groupId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such group."));
        this.groupRepo.delete(group);
    }
}
