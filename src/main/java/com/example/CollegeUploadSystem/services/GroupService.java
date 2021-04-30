package com.example.CollegeUploadSystem.services;

import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.repos.GroupRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepo groupRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Group> getAll() {
        return this.groupRepo.findAll();
    }

    public Group getById(Long groupId) {
        return this.groupRepo.findById(groupId).orElseThrow(NoResultException::new);
    }

    public void saveGroup(Group groupForm, User admin) {
        // save the group.
        Group group = this.groupRepo.save(groupForm);

        // save the students that belongs to the group.
        this.userService.addAllStudents(group.getStudents(), group, admin);
    }
}
