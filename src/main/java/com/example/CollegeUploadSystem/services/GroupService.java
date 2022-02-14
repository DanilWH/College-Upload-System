package com.example.CollegeUploadSystem.services;

import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.repos.GroupRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {
    private final GroupRepo groupRepo;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public GroupService(GroupRepo groupRepo, UserService userService, PasswordEncoder passwordEncoder) {
        this.groupRepo = groupRepo;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
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
        // the csv format is supposed to be in the following format "LastName,FirstName,FatherName".
        // save the group in the database.
        Group savedGroup = this.groupRepo.save(group);

        return savedGroup;
    }

    public Group update(Group groupFromDb, Group group) {
        groupFromDb.setName(group.getName());
        return this.groupRepo.save(groupFromDb);
    }

    public void deactivate(Group group) {
        group.setActive(false);
        this.groupRepo.save(group);
    }
}
