package com.example.CollegeUploadSystem.services;

import com.example.CollegeUploadSystem.models.Group;
import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.models.UserRoles;
import com.example.CollegeUploadSystem.repos.GroupRepo;
import com.example.CollegeUploadSystem.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepo groupRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Group> getAll() {
        return this.groupRepo.findAll();
    }

    public Group getById(Long groupId) {
        return this.groupRepo.findById(groupId).orElseThrow(NoResultException::new);
    }

    public void saveGroup(Group groupForm) {
        // save the group.
        Group group = this.groupRepo.save(groupForm);
        List<User> processedStudents = new ArrayList<>();

        // prepare the students of the new group for storing in the database.
        groupForm.getStudents().forEach((student) -> {
            if (student != null && !(student.getFirstName() + student.getLastName()).isEmpty()) {
                student.setPassword(passwordEncoder.encode(student.getPassword()));
                student.setUserRoles(Collections.singleton(UserRoles.STUDENT));
                student.setGroup(group);
                processedStudents.add(student);
            }
        });

        // save the new students of the new group.
        this.userRepo.saveAll(processedStudents);
    }
}
